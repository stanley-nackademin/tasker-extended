package se.group.backendgruppuppgift.tasker.resource;

import org.springframework.stereotype.Component;
import se.group.backendgruppuppgift.tasker.model.Issue;
import se.group.backendgruppuppgift.tasker.model.Task;
import se.group.backendgruppuppgift.tasker.model.web.IssueWeb;
import se.group.backendgruppuppgift.tasker.model.web.TaskWeb;
import se.group.backendgruppuppgift.tasker.resource.filter.AuthToken;
import se.group.backendgruppuppgift.tasker.resource.filter.Cors;
import se.group.backendgruppuppgift.tasker.service.TaskService;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.sse.OutboundSseEvent;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseBroadcaster;
import javax.ws.rs.sse.SseEventSink;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static javax.ws.rs.core.MediaType.SERVER_SENT_EVENTS;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Component
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Path("tasks")
public final class TaskResource {

    @Context
    private UriInfo uriInfo;

    @Context
    private Sse sse;
    private SseBroadcaster broadcaster;

    private final TaskService taskService;

    public TaskResource(TaskService taskService) {
        this.taskService = taskService;
    }

    @POST
    @AuthToken
    public Response createTask(TaskWeb taskWeb) {
        Task result = taskService.createTask(convertToTask(taskWeb));
        TaskWeb webResult = convertToTaskWeb(result);

        taskNotify(webResult);

        return Response.created(URI.create(uriInfo
                .getAbsolutePathBuilder()
                .path(webResult.getId().toString())
                .toString()))
                .build();
    }

    @GET
    @Path("{id}")
    public Response findTask(@PathParam("id") Long id) {
        return taskService.findTask(id)
                .map(t -> Response.ok(convertToTaskWeb(t)))
                .orElse(Response.status(NOT_FOUND))
                .build();
    }

    @GET
    public List<TaskWeb> findTasksByParams(
            @QueryParam("status") @DefaultValue("") String status,
            @QueryParam("team") @DefaultValue("") String team,
            @QueryParam("user") @DefaultValue("") String user,
            @QueryParam("text") @DefaultValue("") String text,
            @QueryParam("issue") @DefaultValue("") String issue,
            @QueryParam("page") @DefaultValue("") String page,
            @QueryParam("startdate") @DefaultValue("") String startDate,
            @QueryParam("enddate") @DefaultValue("") String endDate) {

        List<TaskWeb> result = new ArrayList<>();
        taskService.findTasksByParams(status, team, user, text, issue, page, startDate, endDate)
                .forEach(t -> result.add(convertToTaskWeb(t)));

        return result;
    }

    @GET
    @Cors
    @Produces(SERVER_SENT_EVENTS)
    @Path("events")
    public void listenToBroadcast(@Context SseEventSink eventSink) {
        if (broadcaster == null) {
            broadcaster = sse.newBroadcaster();
        }

        broadcaster.register(eventSink);
    }

    @PUT
    @Path("{id}")
    public Response updateTask(@PathParam("id") Long id, TaskWeb taskWeb) {
        return taskService.updateTask(id, convertToTask(taskWeb))
                .map(t -> Response.ok(convertToTaskWeb(t)))
                .orElse(Response.status(NOT_FOUND))
                .build();
    }

    @PUT
    @Path("{id}/issue")
    public Response assignIssue(@PathParam("id") Long id, IssueWeb issueWeb) {
        return taskService.assignIssue(id, convertToIssue(issueWeb))
                .map(t -> Response.ok(convertToTaskWeb(t)))
                .orElse(Response.status(NOT_FOUND))
                .build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteTask(@PathParam("id") Long id) {
        return taskService.deleteTask(id)
                .map(t -> Response.noContent())
                .orElse(Response.status(NOT_FOUND))
                .build();
    }

    private void taskNotify(TaskWeb task) {
        final OutboundSseEvent event = sse.newEventBuilder()
                .name("message")
                .mediaType(APPLICATION_JSON_TYPE)
                .data(TaskWeb.class, task)
                .build();

        if (broadcaster == null) {
            broadcaster = sse.newBroadcaster();
        }

        broadcaster.broadcast(event);
    }

    private Task convertToTask(TaskWeb taskWeb) {
        return new Task(taskWeb.getDescription(), taskWeb.getStatus());
    }

    private Issue convertToIssue(IssueWeb issueWeb) {
        return new Issue(issueWeb.getDescription());
    }

    private TaskWeb convertToTaskWeb(Task task) {
        return new TaskWeb(task.getId(), task.getDescription(), task.getStatus(), convertToIssueWeb(task.getIssue()));
    }

    private IssueWeb convertToIssueWeb(Issue issue) {
        return (issue != null) ? new IssueWeb(issue.getDescription()) : null;
    }
}
