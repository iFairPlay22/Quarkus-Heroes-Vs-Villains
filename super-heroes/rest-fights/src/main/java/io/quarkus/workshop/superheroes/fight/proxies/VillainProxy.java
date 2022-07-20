package io.quarkus.workshop.superheroes.fight.proxies;

import io.quarkus.workshop.superheroes.fight.urls._Villain;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/api/villains")
@Produces(MediaType.APPLICATION_JSON)
@RegisterRestClient
public interface VillainProxy {

    @GET
    @Path("/random")
    _Villain findRandomVillain();
}
