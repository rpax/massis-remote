package com.massisframework.massis.remote;

import java.io.IOException;
import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.massisframework.gui.DrawableLayer;
import com.massisframework.jsoninvoker.reflect.JsonClientMessage;
import com.massisframework.jsoninvoker.services.JsonMethodInvoker;
import com.massisframework.massis.displays.floormap.layers.ConnectionsLayer;
import com.massisframework.massis.displays.floormap.layers.CrowdDensityLayer;
import com.massisframework.massis.displays.floormap.layers.DoorLayer;
import com.massisframework.massis.displays.floormap.layers.DrawableFloor;
import com.massisframework.massis.displays.floormap.layers.PathFinderLayer;
import com.massisframework.massis.displays.floormap.layers.PathLayer;
import com.massisframework.massis.displays.floormap.layers.PeopleIDLayer;
import com.massisframework.massis.displays.floormap.layers.PeopleLayer;
import com.massisframework.massis.displays.floormap.layers.QTLayer;
import com.massisframework.massis.displays.floormap.layers.RadioLayer;
import com.massisframework.massis.displays.floormap.layers.RoomsLabelLayer;
import com.massisframework.massis.displays.floormap.layers.RoomsLayer;
import com.massisframework.massis.displays.floormap.layers.VisibleAgentsLines;
import com.massisframework.massis.displays.floormap.layers.VisionRadioLayer;
import com.massisframework.massis.displays.floormap.layers.WallLayer;
import com.massisframework.massis.remote.jsoninvoker.lowlevel.GetAgentsInVisionRadio;
import com.massisframework.massis.remote.jsoninvoker.lowlevel.GetAllLowLevelAgentIds;
import com.massisframework.massis.remote.jsoninvoker.lowlevel.GetRoomId;
import com.massisframework.massis.sim.Simulation;
import com.massisframework.massis.sim.SimulationWithUI;

import sim.display.Console;

@WebSocket
public class MassisServerWebSocket {

	private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();

	private Gson gson;
	private final Simulation simState;
	private final JsonMethodInvoker invoker;
	private final AtomicLong globalMessageCount = new AtomicLong(0);

	public MassisServerWebSocket() {
		this.gson = new GsonBuilder().setPrettyPrinting().create();
		// TODO fich config
		this.simState = prepareSim("simplehouse.sh3d");
		this.invoker = new JsonMethodInvoker();
		this.prepareMethods();
	}

	private void prepareMethods() {
		Supplier<Long> counterSup = () -> globalMessageCount.getAndIncrement();

		this.invoker.register("getRoomId", new GetRoomId(simState, counterSup));
		this.invoker.register("getAgentsInVisionRadio",
				new GetAgentsInVisionRadio(simState, counterSup));
		this.invoker.register("allLowLevelAgentsIds",
				new GetAllLowLevelAgentIds(simState, counterSup));

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
		final JsonObject contentObj = request.get("CONTENT").getAsJsonObject();
		String methodName = contentObj.get("method").getAsString();
		JsonArray params = contentObj.get("params").getAsJsonArray();
		this.invoker.invoke(methodName, params, (content) -> {
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

	private static Simulation prepareSim(String buildingFilePath) {

		/*
		 * Not needed, in this example. We are not going to load any kind of
		 * resources during the simulation.
		 */
		final String resourceFolderPath = "";

		final Simulation simState = new Simulation(System.currentTimeMillis(),
				buildingFilePath, resourceFolderPath, null);
		/**
		 * Basic Layers. Can be added more, or removed.
		 */
		@SuppressWarnings("unchecked")
		final DrawableLayer<DrawableFloor>[] floorMapLayers = new DrawableLayer[] {
				new RoomsLayer(true), new RoomsLabelLayer(false),
				new VisionRadioLayer(false), new CrowdDensityLayer(false),
				new WallLayer(true), new DoorLayer(true),
				new ConnectionsLayer(false), new PathLayer(false),
				new PeopleLayer(true), new RadioLayer(true),
				new PathFinderLayer(false), new PeopleIDLayer(false),
				new VisibleAgentsLines(false), new QTLayer(false) };

		final SimulationWithUI vid = new SimulationWithUI(simState,
				floorMapLayers);

		final Console c = new Console(vid);

		c.setIncrementSeedOnStop(false);
		//
		c.pressPlay();
		c.pressPause();
		c.setVisible(true);
		return simState;
	}

}
