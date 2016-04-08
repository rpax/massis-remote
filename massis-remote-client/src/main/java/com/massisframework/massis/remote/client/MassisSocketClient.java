package com.massisframework.massis.remote.client;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.massisframework.jsoninvoker.reflect.JsonClientMessage;
import com.massisframework.jsoninvoker.reflect.JsonServiceResponse;
import com.massisframework.jsoninvoker.reflect.JsonServiceResponseHandler;
import com.massisframework.jsoninvoker.reflect.JsonServiceResponse.ResponseType;
import com.massisframework.massis.remote.client.imp.MassisServiceSender;

public class MassisSocketClient extends WebSocketClient
		implements MassisServiceSender {

	private Gson gson;
	private Logger logger = Logger
			.getLogger(MassisSocketClient.class.getName());
	private static final String TAG = "TAG";
	private static final String CONTENT = "CONTENT";
	private HashMap<String, JsonServiceResponseHandler<?>> messageMemory;
	private Map<String, Class<?>> dataClasses;

	public MassisSocketClient(URI uri) {
		super(uri, new Draft_17());
		this.gson = new GsonBuilder().setPrettyPrinting().create();
		this.messageMemory = new HashMap<>();
		this.dataClasses = new HashMap<>();
	}

	public void setLogLevel(Level lvl) {
		logger.setLevel(lvl);
	}

	@Override
	public void onMessage(String json) {
		JsonObject message = gson.fromJson(json, JsonObject.class);
		logger.info(() -> "<<RESPONSE:\n  " + gson.toJson(message));
		String tag = message.get(TAG).getAsString();
		JsonObject content = message.get(CONTENT).getAsJsonObject();
		JsonServiceResponse response = gson.fromJson(content,
				JsonServiceResponse.class);

		Class<?> dataClass = dataClasses.get(tag);
		JsonServiceResponseHandler<?> handler = messageMemory.get(tag);

		response.setData(gson.fromJson(content.get("data"), dataClass));

		if (response.getResponseType() == ResponseType.ERROR
				|| response.getResponseType() == ResponseType.FINISHED) {
			dataClasses.remove(tag);
			messageMemory.remove(tag);

		}
		handler.handle(response);
	}

	@Override
	public void onOpen(ServerHandshake handshake) {
		System.out.println("opened connection");
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		System.out.println("closed connection");
	}

	@Override
	public void onError(Exception ex) {
		ex.printStackTrace();
	}

	@Override
	public <T> void send(JsonClientMessage<T> item) {
		HashMap<Object, Object> callObj = new HashMap<>();
		String tag = UUID.randomUUID().toString();
		this.messageMemory.put(tag, item.getHandler());
		this.dataClasses.put(tag, item.getDataClass());
		callObj.put(TAG, tag);
		callObj.put(CONTENT, item);
		logger.info(() -> ">>REQUEST:\n  " + gson.toJson(callObj));
		this.send(gson.toJson(callObj));
	}

}
