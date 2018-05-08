package se.group.backendgruppuppgift.tasker.resource;

import org.springframework.stereotype.Component;
import se.group.backendgruppuppgift.tasker.model.User;
import se.group.backendgruppuppgift.tasker.service.UserService;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.Optional;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;


import static javax.ws.rs.core.Response.Status.*;
@Component
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Path("users")
public final class UserResource {

    @Context
    private UriInfo uriInfo;

    private final UserService service;

    public UserResource(UserService service) {
        this.service = service;
    }

    @POST
    public Response createUser(User user) {
        User result = service.createUser(user);

        return Response.created(URI.create(uriInfo
                .getAbsolutePathBuilder()
                .path(result.getId().toString())
                .toString()))
                .build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteUser(@PathParam ("id") Long id){
        Optional<User> result = service.deleteUser(id);
        return result.map(r -> Response.status(NO_CONTENT)).orElse(Response.status(NOT_FOUND)).build();
    }
}
