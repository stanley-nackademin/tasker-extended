package se.group.backendgruppuppgift.tasker.resource.mapper;

import se.group.backendgruppuppgift.tasker.service.exception.InvalidTeamException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static java.util.Collections.singletonMap;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

@Provider
public final class InvalidTeamMapper implements ExceptionMapper<InvalidTeamException>{

    @Override
    public Response toResponse(InvalidTeamException exception){
        return Response.status(BAD_REQUEST).entity(singletonMap("error", exception.getMessage())).build();
    }
}
