package com.fri.musicbook;

import com.fri.musicbook.GenresBean;
import com.kumuluz.ee.logs.cdi.Log;
import com.kumuluz.ee.logs.cdi.LogParams;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.metrics.annotation.Timed;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@RequestScoped
@Path("/genres")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Log(LogParams.METRICS)
public class GenresResource {



    @Context
    protected UriInfo uriInfo;

    @Inject
    private GenresBean genresBean;

    @Metered(name = "getGenres")
    @GET
    public Response getGenres() {
System.out.println("getVsiGen");
        List<Genre> genres = genresBean.getGenres();

        return Response.ok(genres).build();
    }

    @Metered(name = "getGenreId")
    @GET
    @Path("/{genreId}")
    public Response getGenre(@PathParam("genreId") String genreId) {
        System.out.println("get1Gen");
        Genre genre = genresBean.getGenre(genreId);

        if (genre == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(genre).build();
    }

    @Timed(name = "getGenresFiltered_time")
    @Metered(name = "getGenresFiltered")
    @GET
    @Path("/filtered")
    public Response getGenresFiltered() {

        List<Genre> genres;

        genres = genresBean.getGenresFilter(uriInfo);

        return Response.status(Response.Status.OK).entity(genres).build();
    }

    @Metered(name = "createGenre")
    @POST
    public Response createGenre(Genre genre) {

        if ((genre.getName() == null || genre.getName().isEmpty())) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            genre = genresBean.createGenre(genre);
        }

        if (genre.getId() != null) {
            return Response.status(Response.Status.CREATED).entity(genre).build();
        } else {
            return Response.status(Response.Status.CONFLICT).entity(genre).build();
        }
    }

    @Metered(name = "putGenre")
    @PUT
    @Path("{genreId}")
    public Response putGenre(@PathParam("genreId") String genreId, Genre genre) {

        genre = genresBean.putGenre(genreId, genre);

        if (genre == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            if (genre.getId() != null)
                return Response.status(Response.Status.OK).entity(genre).build();
            else
                return Response.status(Response.Status.NOT_MODIFIED).build();
        }
    }

    @Metered(name = "deleteGenre")
    @DELETE
    @Path("{genreId}")
    public Response deleteGenre(@PathParam("genreId") String genreId) {

        boolean deleted = genresBean.deleteGenre(genreId);

        if (deleted) {
            return Response.status(Response.Status.GONE).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}