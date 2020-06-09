package dev.appsody.microservice;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/hello")
public class JavaMicroServiceResource {

    @GET
    public String sayHello(){
        return "Hello secured World!!!";
    }


}