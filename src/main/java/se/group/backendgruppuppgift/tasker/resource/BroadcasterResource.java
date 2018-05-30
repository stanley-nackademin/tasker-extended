package se.group.backendgruppuppgift.tasker.resource;

import se.group.backendgruppuppgift.tasker.model.web.TaskWeb;
import se.group.backendgruppuppgift.tasker.resource.filter.Cors;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.sse.OutboundSseEvent;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseBroadcaster;
import javax.ws.rs.sse.SseEventSink;

import static javax.ws.rs.core.MediaType.*;

@Singleton
@Path("tasks")
public class BroadcasterResource {

    private Sse sse;
    private SseBroadcaster broadcaster;

    public BroadcasterResource(@Context final Sse sse) {
        this.sse = sse;
        this.broadcaster = sse.newBroadcaster();
    }

    @POST
    @Consumes(TEXT_PLAIN)
    @Produces(TEXT_PLAIN)
    @Path("events")
    public String broadcastMessage(String message) {
        final OutboundSseEvent event = sse.newEventBuilder()
                .name("message")
                .mediaType(TEXT_PLAIN_TYPE)
                .data(String.class, message)
                .build();

        broadcaster.broadcast(event);

        return "Message '" + message + "' has been broadcast.";
    }

    @GET
    @Cors
    @Produces(SERVER_SENT_EVENTS)
    @Path("events")
    public void listenToBroadcast(@Context SseEventSink eventSink) {
        this.broadcaster.register(eventSink);
    }

    public void taskNotify(TaskWeb task) {
        final OutboundSseEvent event = sse.newEventBuilder()
                .name("message")
                .mediaType(APPLICATION_JSON_TYPE)
                .data(TaskWeb.class, task)
                .build();

        broadcaster.broadcast(event);
    }
}
