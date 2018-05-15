package se.group.backendgruppuppgift.tasker.resource;

import org.springframework.stereotype.Component;
import se.group.backendgruppuppgift.tasker.model.Issue;
import se.group.backendgruppuppgift.tasker.model.Task;
import se.group.backendgruppuppgift.tasker.model.web.IssueWeb;
import se.group.backendgruppuppgift.tasker.model.web.TaskWeb;
import se.group.backendgruppuppgift.tasker.service.MasterService;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Component
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Path("tasks")
public final class TaskResource {

    @Context
    private UriInfo uriInfo;

    private final MasterService service;

    public TaskResource(MasterService service) {
        this.service = service;
    }

    @POST
    public Response createTask(TaskWeb taskWeb) {
        Task result = service.getTaskService().createTask(convertToTaskObject(taskWeb));
        TaskWeb webResult = convertToWeb(result);

        return Response.created(URI.create(uriInfo
                .getAbsolutePathBuilder()
                .path(webResult.getId().toString())
                .toString()))
                .build();
    }

    @GET
    @Path("{id}")
    public Response findTask(@PathParam("id") Long id) {
        return service.getTaskService().findTask(id)
                .map(t -> Response.ok(convertToWeb(t)))
                .orElse(Response.status(NOT_FOUND))
                .build();
    }

    @GET
    public List<TaskWeb> findTasksByParams(
            @QueryParam("status") String status,
            @QueryParam("team") String team,
            @QueryParam("user") String user,
            @QueryParam("text") String text,
            @QueryParam("issue") String value) {

        List<TaskWeb> result = new ArrayList<>();
        service.getTaskService().findTasksByParams(status, team, user, text, value)
                .forEach(t -> result.add(convertToWeb(t)));

        return result;
    }

    @PUT
    @Path("{id}")
    public Response updateTask(@PathParam("id") Long id, TaskWeb taskWeb) {
        return service.getTaskService().updateTask(id, convertToTaskObject(taskWeb))
                .map(t -> Response.ok(convertToWeb(t)))
                .orElse(Response.status(NOT_FOUND))
                .build();
    }

    @PUT
    @Path("{id}/issue")
    public Response assignIssue(@PathParam("id") Long id, IssueWeb issueWeb) {
        return service.getTaskService().assignIssue(id, convertToIssueObject(issueWeb))
                .map(t -> Response.ok(convertToWeb(t)))
                .orElse(Response.status(NOT_FOUND))
                .build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteTask(@PathParam("id") Long id) {
        return service.getTaskService().deleteTask(id)
                .map(t -> Response.noContent())
                .orElse(Response.status(NOT_FOUND))
                .build();
    }

    private Task convertToTaskObject(TaskWeb taskWeb) {
        return new Task(taskWeb.getDescription(), taskWeb.getStatus());
    }

    private Issue convertToIssueObject(IssueWeb issueWeb) {
        return new Issue(issueWeb.getDescription());
    }

    private TaskWeb convertToWeb(Task task) {
        return new TaskWeb(task.getId(), task.getDescription(), task.getStatus(), convertIssueToWeb(task.getIssue()));
    }

    private IssueWeb convertIssueToWeb(Issue issue) {
        return issue != null ? new IssueWeb(issue.getDescription(), issue.getIsDone()) : null;
    }
}
