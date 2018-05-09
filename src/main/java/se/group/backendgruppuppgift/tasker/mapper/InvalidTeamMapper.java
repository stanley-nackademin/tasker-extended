package se.group.backendgruppuppgift.tasker.mapper;

import se.group.backendgruppuppgift.tasker.service.InvalidTeamException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static java.util.Collections.*;
import static javax.ws.rs.core.Response.Status.*;

@Provider
public final class InvalidTeamMapper implements ExceptionMapper<InvalidTeamException> {

    @Override
    public Response toResponse(InvalidTeamException e){
        return Response.status(BAD_REQUEST).entity(singletonMap("error", e.getMessage())).build();
    }

}
