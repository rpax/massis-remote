package com.massisframework.massis.remote;

import java.io.IOException;
import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.massisframework.jsoninvoker.reflect.JsonClientMessage;

@WebSocket
public class MassisServerWebSocket {

	private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();

	private Gson gson;

	public MassisServerWebSocket() {
		this.gson = new GsonBuilder().setPrettyPrinting().create();
	}

	public void init() {

	}

	@OnWebSocketConnect
	public void connected(Session session) {
		sessions.add(session);
		System.err.println("CONNECTED!");
	}

	@OnWebSocketClose
	public void closed(Session session, int statusCode, String reason) {
		sessions.remove(session);
	}

	@OnWebSocketMessage
	public void message(Session session, String message) throws IOException {
		JsonObject request = gson.fromJson(message, JsonObject.class);
		final String tag = request.get("TAG").getAsString();
		JsonClientMessage msg = gson.fromJson(request.get("CONTENT"),
				JsonClientMessage.class);
		MassisServer.invoke(msg, (content) -> {
			try {
				HashMap<Object, Object> response = new HashMap<>();
				response.put("TAG", tag);
				response.put("CONTENT", content);
				sendString(session, gson.toJson(response));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}

	private static synchronized void sendString(Session session, String msg)
			throws IOException {
		if (session.isOpen())
			session.getRemote().sendString(msg);
	}

}
