package com.fri.musicbook;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RequestScoped
public class GenresBean {

    private ObjectMapper objectMapper;

    private HttpClient httpClient;

   // @Inject
   // @DiscoverService(value = "artis-service", environment = "dev", version = "*")
    //private Optional<String> basePath;

    @Inject
    @DiscoverService("artist-service")
    private Optional<String> basePath;

    //private String basePath = "http://172.17.0.1:8084";

    @Inject
    private GenresBean genresBean;

	@PostConstruct
    private void init() {
		httpClient = HttpClientBuilder.create().build();
		objectMapper = new ObjectMapper();
        //objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
	}

    @Inject
    private RestProperties restProperties;


    @PersistenceContext(unitName = "genres-jpa")
    private EntityManager em;

    public List<Genre> getGenres(){

        Query query = em.createNamedQuery("Genre.getAll", Genre.class);

        return query.getResultList();

    }

    public boolean switchEnabled(){
        restProperties.setArtistServiceEnabled(restProperties.isArtistServiceEnabled());
        return restProperties.isArtistServiceEnabled();
    }

    public Genre getGenre(String genreId) {
        System.out.println("Bean:getGenre");
        Genre genre = em.find(Genre.class, genreId);

        if (genre == null) {
            throw new NotFoundException();
        }
        /*
        if (!restProperties.isArtistServiceEnabled()) {
            List<Artist> artists = genresBean.getArtists(genreId);
            System.out.print(artists);
            genre.setArtists(artists);
        }
        */

        //MOGOČE JE KRIV /HEALTH SERVLET V config

        return genre;

    }

    public List<Genre> getGenresFilter(UriInfo uriInfo) {

        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery()).defaultOffset(0)
                .build();

        List<Genre> genres = JPAUtils.queryEntities(em, Genre.class, queryParameters);

        return genres;
    }

    public List<Artist> getArtists(String genreId) {
/*
        try {
            System.out.println("try"+basePath+"|..|"+genreId);
            HttpGet request = new HttpGet(basePath + "/v1/artists/filtered?filter=genreId:EQ:"+genreId);
            if (request==null)System.out.println("req je null");
	    else System.out.println("req ni null");
	    
	    HttpResponse response = httpClient.execute(request);
		if (response==null)System.out.println("resp je null");
		else System.out.println("resp ni null");
            int status = response.getStatusLine().getStatusCode();
	    System.out.println("stat je "+status);
            if (status >= 200 && status < 300) {
                System.out.println("v status");
                HttpEntity entity = response.getEntity();
                System.out.println(entity+"||"+entity.getContent()+"||"+entity.toString()+"||");
                if (entity != null) {
                    //System.out.println(EntityUtils.toString(entity));
                    List<Artist> ars = objectMapper.readValue(EntityUtils.toString(entity), new TypeReference<List<Artist>>(){});
                    System.out.println("json:"+ars);
                    return ars;
                    //getObjects(EntityUtils.toString(entity));
                }
                System.out.println("ent=null");
            } else {
                System.out.println("else");
                String msg = "Remote server '" + basePath + "' is responded with status " + status + ".";
                //log.error(msg);
                throw new InternalServerErrorException(msg);
            }

        } catch (IOException e) {
            System.out.println("Exc"+e.getStackTrace()+
                    "\n"+e.getCause()+"\n"
                    +e.getLocalizedMessage()+"\n"
                    +e.getClass()+"\n"+e.getMessage());

            String msg = e.getClass().getName() + " occured: " + e.getMessage();
            //log.error(msg);
            throw new InternalServerErrorException(msg);
        }
        */
        return new ArrayList<>();

    }


    public Genre createGenre(Genre genre) {

        try {
            beginTx();
            em.persist(genre);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return genre;
    }

    public Genre putGenre(String genreId, Genre genre) {

        Genre c = em.find(Genre.class, genreId);

        if (c == null) {
            return null;
        }

        try {
            beginTx();
            genre.setId(c.getId());
            genre = em.merge(genre);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return genre;
    }

    public boolean deleteGenre(String genreId) {

        Genre genre = em.find(Genre.class, genreId);

        if (genre != null) {
            try {
                beginTx();
                em.remove(genre);
                commitTx();
            } catch (Exception e) {
                rollbackTx();
            }
        } else
            return false;

        return true;
    }

    private List<Artist> getObjects(String json) throws IOException {
        return json == null ? new ArrayList<>() : objectMapper.readValue(json,
                objectMapper.getTypeFactory().constructCollectionType(List.class, Artist.class));
    }

    private void beginTx() {
        if (!em.getTransaction().isActive())
            em.getTransaction().begin();
    }

    private void commitTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().commit();
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().rollback();
    }

}
