package dev.appsody.starter;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/resource")
public class StarterResource {

    @GET
    public String getRequest() {
        return "StarterResource response";
    }

    @GET
    @Path("/getBalance")
    public String getBalance(){
        return "56789";
    }
}



