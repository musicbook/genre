package com.fri.musicbook;

//import com.kumuluz.ee.discovery.annotations.RegisterService;

import com.kumuluz.ee.discovery.annotations.RegisterService;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/v1")
@RegisterService
public class GenreApplication extends Application {
}