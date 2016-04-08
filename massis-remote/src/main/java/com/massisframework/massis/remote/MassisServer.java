package com.massisframework.massis.remote;

import static spark.Spark.init;
import static spark.Spark.webSocket;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.massisframework.gui.DrawableLayer;
import com.massisframework.jsoninvoker.reflect.JsonClientMessage;
import com.massisframework.jsoninvoker.reflect.JsonServiceResponseHandler;
import com.massisframework.jsoninvoker.reflect.ServiceMethodInvoker;
import com.massisframework.jsoninvoker.services.JsonMethod.JsonMethod1;
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
import com.massisframework.massis.remote.jsoninvoker.LowLevelAgentQueryServiceServerImpl;
import com.massisframework.massis.remote.jsoninvoker.lowlevel.GetRoomId;
import com.massisframework.massis.remote.services.agents.services.LowLevelAgentQueryService;
import com.massisframework.massis.sim.Simulation;
import com.massisframework.massis.sim.SimulationWithUI;

import sim.display.Console;

public class MassisServer {

	public MassisServer() {

	}

	public static void main(String[] args) {

		webSocket("/massis", MassisServerWebSocket.class);
		init();

	}

}
