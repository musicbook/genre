package com.fri.musicbook;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@ConfigBundle("rest-properties")
public class RestProperties {

    @ConfigValue(value = "external-dependencies.artist-service.enabled", watch = true)
    private boolean artistServiceEnabled;

    public boolean isArtistServiceEnabled() {
        return artistServiceEnabled;
    }

    public void setArtistServiceEnabled(boolean artistServiceEnabled) {
        this.artistServiceEnabled = artistServiceEnabled;
    }

}