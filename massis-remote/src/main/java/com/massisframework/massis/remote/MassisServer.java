package com.massisframework.massis.remote;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.massisframework.gui.DrawableLayer;
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
import com.massisframework.massis.model.agents.LowLevelAgent;
import com.massisframework.massis.model.building.Building;
import com.massisframework.massis.model.building.SimulationObject;
import com.massisframework.massis.remote.commands.AbstractCommand;
import com.massisframework.massis.remote.commands.CommandParser;
import com.massisframework.massis.remote.commands.CommandResponseCallback;
import com.massisframework.massis.remote.commands.MoveRandomCommand;
import com.massisframework.massis.remote.highlevel.HighLevelCommandController;
import com.massisframework.massis.sim.AbstractSimulation;
import com.massisframework.massis.sim.Simulation;
import com.massisframework.massis.sim.SimulationWithUI;

import sim.display.Console;

public class MassisServer {

	private Simulation simState;
	private ExecutorService executor;
	private Gson gson;

	public MassisServer(Simulation simState) {
		this.simState = simState;
		this.gson = new GsonBuilder().setPrettyPrinting().create();
	}

	public void start() {
		this.executor = Executors.newSingleThreadExecutor();

	}

	private void stop() {
		executor.shutdown();
		try {
			executor.awaitTermination(30, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			System.err.println("Interrupted while shutting down");
		}
	}

	public static void main(String[] args) {

		// Open file, read command array.

		MassisServer server = prepareSim();
		server.start();
		// sleep(10000);
		// System.out.println("Start sending commands...");
		// Arrays.asList(12, 18, 19, 20).forEach((id) -> {
		// sleep(1000);
		// server.submit(new MoveRandomCommand(id), (cr) -> {
		// System.out.println(cr.getInfo());
		// });
		// });
		try (Scanner sc = new Scanner(System.in)) {
			while (true) {
				String line = sc.nextLine();
				if ("q".equals(line))
					break;
				AbstractCommand<?> command = CommandParser.getInstance()
						.parseCommand(line);
				server.submit(command, (cr) -> {
					String json = server.gson.toJson(cr);
					System.out.println(json);
				});
			}
		}

		server.stop();
	}

	private <CR> void submit(AbstractCommand<CR> sayHelloCommand,
			CommandResponseCallback<CR> callback) {
		this.executor.submit(() -> sayHelloCommand.runCommand(this, callback));
	}

	private static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public HighLevelCommandController getHighLevelController(int id) {
		final Building building = this.getSimulation().getBuilding();
		SimulationObject simObj = building.getSimulationObject(id);
		if ((simObj instanceof LowLevelAgent)) {
			final LowLevelAgent lla = (LowLevelAgent) simObj;
			if (lla.getHighLevelData() instanceof HighLevelCommandController) {
				return (HighLevelCommandController) lla.getHighLevelData();
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	private static MassisServer prepareSim() {

		String buildingFilePath = null;

		buildingFilePath = "simplehouse.sh3d";
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

		return new MassisServer(simState);
	}

	public AbstractSimulation getSimulation() {
		return this.simState;
	}

	public long getTick() {
		return this.simState.schedule.getSteps();
	}

}
