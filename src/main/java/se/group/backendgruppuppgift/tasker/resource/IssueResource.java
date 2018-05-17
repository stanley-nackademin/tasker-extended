package se.group.backendgruppuppgift.tasker.resource;

import org.springframework.stereotype.Component;
import se.group.backendgruppuppgift.tasker.model.Issue;
import se.group.backendgruppuppgift.tasker.model.web.IssueWeb;
import se.group.backendgruppuppgift.tasker.service.IssueService;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import java.util.ArrayList;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.*;

@Component
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Path("issues")
public final class IssueResource {

    @Context
    private UriInfo uriInfo;

    private final IssueService service;

    public IssueResource(IssueService service) {
        this.service = service;
    }

    @GET
    public List<IssueWeb> getAllIssues() {
        List<IssueWeb> result = new ArrayList<>();
        service.getAll().forEach(i -> result.add(convertToIssueWeb(i)));

        return result;
    }

    @GET
    @Path("{id}")
    public Response findIssue(@PathParam("id") Long id) {
        return service.findIssue(id)
                .map(i -> Response.ok(convertToIssueWeb(i)))
                .orElse(Response.status(NOT_FOUND))
                .build();
    }

    @PUT
    @Path("{id}")
    public Response updateIssue(@PathParam("id") Long id, IssueWeb issueWeb) {
        return service.updateIssue(id, convertToIssue(issueWeb))
                .map(i -> Response.ok(convertToIssueWeb(i)))
                .orElse(Response.status(NOT_FOUND))
                .build();
    }

    private Issue convertToIssue(IssueWeb issueWeb) {
        return new Issue(issueWeb.getDescription());
    }

    private IssueWeb convertToIssueWeb(Issue issue) {
        return new IssueWeb(issue.getDescription());
    }
}
