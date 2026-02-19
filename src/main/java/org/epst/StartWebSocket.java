package org.epst;

import io.vertx.core.json.JsonObject;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.Json;
import jakarta.websocket.EncodeException;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.requireNonNull;

@ServerEndpoint("/signaling/{roomId}/{clientId}")
@ApplicationScoped
public class StartWebSocket {
    // Un set pour garder une trace des sessions WebSocket actives
    Map<String, Session> sessions = new ConcurrentHashMap<>();
    Map<String, String> broadcasters = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session,
                       @PathParam("roomId") String roomId,
                       @PathParam("clientId") String clientId) {
        sessions.put(clientId, session);
        broadcastMessage(roomId, "user-connected", clientId);
    }

    @OnClose
    public void onClose(Session session,
                        @PathParam("roomId") String roomId,
                        @PathParam("clientId") String clientId) {
        sessions.remove(clientId);
        broadcastMessage(roomId, "user-disconnected", clientId);
    }

    @OnError
    public void onError(Session session,
                        @PathParam("roomId") String roomId,
                        @PathParam("clientId") String clientId,
                        Throwable throwable) {
        sessions.remove(clientId);
        System.err.println("Error for client " + clientId + ": " + throwable.getMessage());
    }

    @OnMessage
    public void onMessage(String message,
                          @PathParam("roomId") String roomId,
                          @PathParam("clientId") String clientId) {
        JsonObject json = (JsonObject) Json.createReader(new StringReader(message)).readObject();

        String type = json.getString("type");
        String targetClientId = json.getString("targetClientId");

        switch (type) {
            case "broadcaster":
                broadcasters.put(roomId, clientId);
                break;
            case "offer":
            case "answer":
            case "candidate":
                sendToClient(targetClientId, message);
                break;
            default:
                System.err.println("Unknown message type: " + type);
        }
    }

    private void sendToClient(String clientId, String message) {
        Session session = sessions.get(clientId);
        if (session != null) {
            session.getAsyncRemote().sendText(message);
        }
    }

    private void broadcastMessage(String roomId, String type, String message) {
        sessions.values().forEach(s -> {
            s.getAsyncRemote().sendText(
                    Json.createObjectBuilder()
                            .add("type", type)
                            .add("message", message)
                            .build()
                            .toString()
            );
        });
    }
}
