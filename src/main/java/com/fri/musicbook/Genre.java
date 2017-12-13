package com.fri.musicbook;

import org.eclipse.persistence.annotations.UuidGenerator;

import javax.persistence.*;
import java.util.List;

@Entity(name = "genre")
@NamedQueries(value =
        {
                @NamedQuery(name = "Genre.getAll", query = "SELECT c FROM genre c")
        })
@UuidGenerator(name = "idGenerator")
public class Genre {

    @Id
    @GeneratedValue(generator = "idGenerator")
    private String id;

    @Column(name = "name")
    private String name;

    @Transient
    private List<Artist> artists;


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

}
