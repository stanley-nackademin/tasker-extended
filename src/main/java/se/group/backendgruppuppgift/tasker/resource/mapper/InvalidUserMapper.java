package se.group.backendgruppuppgift.tasker.resource.mapper;

import se.group.backendgruppuppgift.tasker.service.exception.InvalidUserException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.*;

import static java.util.Collections.singletonMap;
import static javax.ws.rs.core.Response.Status.*;

@Provider
public class InvalidUserMapper implements ExceptionMapper<InvalidUserException> {

    @Override
    public Response toResponse(InvalidUserException e) {
        return Response.status(BAD_REQUEST).entity(singletonMap("error", e.getMessage())).build();
    }
}
