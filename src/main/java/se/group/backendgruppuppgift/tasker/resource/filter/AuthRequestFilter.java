package se.group.backendgruppuppgift.tasker.resource.filter;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

import static java.util.Collections.singletonMap;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

@Provider
@AuthToken
@Priority(Priorities.AUTHORIZATION)
public final class AuthRequestFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String token = requestContext.getHeaderString("auth-token");

        if (!"secret".equals(token)) {
            requestContext.abortWith(Response
                    .status(UNAUTHORIZED)
                    .entity(singletonMap("error", "Authorization failed"))
                    .build());
        }
    }
}
