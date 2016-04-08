package com.massisframework.massis.remote.jsoninvoker;

import java.awt.Point;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.massisframework.jsoninvoker.annotations.JsonService;
import com.massisframework.jsoninvoker.reflect.JsonResponseHelper;
import com.massisframework.jsoninvoker.reflect.JsonResponseHelper.JsonResponseSender;
import com.massisframework.jsoninvoker.reflect.JsonServiceResponse.ResponseType;
import com.massisframework.jsoninvoker.reflect.JsonServiceResponseHandler;
import com.massisframework.massis.model.agents.LowLevelAgent;
import com.massisframework.massis.model.building.Floor;
import com.massisframework.massis.model.building.SimulationObject;
import com.massisframework.massis.model.location.Location;
import com.massisframework.massis.model.managers.movement.ApproachCallback;
import com.massisframework.massis.pathfinding.straightedge.FindPathResult.PathFinderErrorReason;
import com.massisframework.massis.remote.highlevel.HighLevelCommandController;
import com.massisframework.massis.remote.services.agents.services.LowLevelAgentQueryService;
import com.massisframework.massis.sim.Simulation;
import com.massisframework.massis.util.Indexable;

import straightedge.geom.KPoint;

@JsonService(name = "LowLevelAgentQueryService", description = "")
public class LowLevelAgentQueryServiceServerImpl
		implements LowLevelAgentQueryService {

	private final Simulation simulation;
	private final JsonResponseHelper jsonResponseHelper;
	private final ConcurrentLinkedQueue<Runnable> workQueue;

	public LowLevelAgentQueryServiceServerImpl(Simulation sim) {
		this.simulation = sim;
		this.jsonResponseHelper = new JsonResponseHelper();
		this.workQueue = new ConcurrentLinkedQueue<>();
		sim.schedule.scheduleRepeating((ss) -> {
			while (!this.workQueue.isEmpty()) {
				this.workQueue.poll().run();
			}
		});
	}

	private void enqueue(Runnable r) {
		this.workQueue.add(r);
	}

	@Override
	public void getAgentsIdsInRange(int objectId, final double range,
			final JsonServiceResponseHandler<List<Integer>> handler) {
		this.llaSend(objectId, (lla, sender) -> {
			sender.send(ResponseType.FINISHED,
					getIDs(lla.getAgentsInRange(range)));
		} , handler);
	}

	@Override
	public void getAgentsInVisionRadio(int objectId,
			JsonServiceResponseHandler<List<Integer>> handler) {
		this.llaSend(objectId, (lla, sender) -> {
			sender.send(ResponseType.FINISHED,
					getIDs(lla.getAgentsInVisionRadio()));
		} , handler);
	}

	@Override
	public void getRoomId(int objectId,
			JsonServiceResponseHandler<Integer> handler) {
		this.llaSend(objectId, (lla, sender) -> {
			sender.send(ResponseType.FINISHED, lla.getRoom().getID());
		} , handler);
	}

	@Override
	public void getAgentsIdsInRoom(int objectId,
			JsonServiceResponseHandler<List<Integer>> handler) {

		this.llaSend(objectId, (lla, sender) -> {
			sender.send(ResponseType.FINISHED, getIDs(lla.getAgentsInRoom()));
		} , handler);

	}

	@Override
	public void getVisionRadio(int objectId,
			JsonServiceResponseHandler<Double> handler) {
		this.llaSend(objectId, (lla, sender) -> {
			sender.send(ResponseType.FINISHED, lla.getVisionRadio());
		} , handler);
	}

	@Override
	public void getVisionRadioPoints(int objectId,
			JsonServiceResponseHandler<Point[]> handler) {
		/*
		 * Creation of the sender
		 */
		JsonResponseSender<Point[]> sender = this.jsonResponseHelper
				.createSender(handler);
		/*
		 * Send to the handler a "received" notification
		 */
		sender.sendReceived();
		// Not implemented
		sender.send(ResponseType.ERROR, null);

	}

	@Override
	public void isInDoorArea(int objectId,
			JsonServiceResponseHandler<Boolean> handler) {
		this.llaSend(objectId, (lla, sender) -> {
			sender.send(ResponseType.FINISHED, lla.isInDoorArea());
		} , handler);
	}

	@Override
	public void isObjectPerceived(int objectId, int otherObjectId,
			JsonServiceResponseHandler<Boolean> handler) {
		this.llaSend(objectId, (lla, sender) -> {
			LowLevelAgent other = getLowLevelAgent(otherObjectId);
			if (other == null) {
				sender.send(ResponseType.ERROR, null);
			} else {
				sender.send(ResponseType.FINISHED,
						lla.isObjectPerceived(other));
			}

		} , handler);
	}

	@Override
	public void isPointContainedInVisionArea(int objectId, Point point,
			JsonServiceResponseHandler<Boolean> handler) {
		this.llaSend(objectId, (lla, sender) -> {
			sender.send(ResponseType.FINISHED, lla.isPointContainedInVisionArea(
					new KPoint(point.x, point.y)));

		} , handler);
	}

	@Override
	public void isObstacle(int objectId,
			JsonServiceResponseHandler<Boolean> handler) {
		this.llaSend(objectId, (lla, sender) -> {
			sender.send(ResponseType.FINISHED, lla.isObstacle());
		} , handler);
	}

	@Override
	public void isDynamic(int objectId,
			JsonServiceResponseHandler<Boolean> handler) {
		this.llaSend(objectId, (lla, sender) -> {
			sender.send(ResponseType.FINISHED, lla.isDynamic());
		} , handler);
	}

	@Override
	public void getBodyRadius(int objectId,
			JsonServiceResponseHandler<Double> handler) {
		this.llaSend(objectId, (lla, sender) -> {
			sender.send(ResponseType.FINISHED, lla.getBodyRadius());
		} , handler);
	}

	@Override
	public void isInNamedLocation(int objectId, final String name,
			final int radiusWithin,
			JsonServiceResponseHandler<Boolean> handler) {
		this.llaSend(objectId, (lla, sender) -> {
			sender.send(ResponseType.FINISHED,
					lla.isInNamedLocation(name, radiusWithin));
		} , handler);
	}

	@Override
	public void getProperty(int objectId, String propertyName,
			JsonServiceResponseHandler<Object> handler) {

		this.llaSend(objectId, (lla, sender) -> {
			sender.send(ResponseType.FINISHED,
					lla.getProperty(propertyName));
		} , handler);
	}

	@Override
	public void hasProperty(int objectId, String propertyName,
			JsonServiceResponseHandler<Boolean> handler) {
		this.llaSend(objectId, (lla, sender) -> {
			sender.send(ResponseType.FINISHED,
					lla.hasProperty(propertyName));
		} , handler);
	}

	private LowLevelAgent getLowLevelAgent(int objectId) {
		SimulationObject simObj = this.simulation.getBuilding()
				.getSimulationObject(objectId);
		if (simObj == null)
			return null;
		if (simObj instanceof LowLevelAgent)
			return (LowLevelAgent) simObj;
		return null;
	}

	private List<Integer> getIDs(Iterable<? extends Indexable> iterable) {
		return StreamSupport.stream(iterable.spliterator(), false)
				.map(Indexable::getID)
				.collect(Collectors.toList());
	}

	private <T> void llaSend(int objectId,
			BiConsumer<LowLevelAgent, JsonResponseSender<T>> ifExists,
			JsonServiceResponseHandler<T> handler) {
		JsonResponseSender<T> sender = this.jsonResponseHelper
				.createSender(handler);
		sender.sendReceived();
		LowLevelAgent lla = getLowLevelAgent(objectId);
		if (lla == null) {
			sender.send(ResponseType.ERROR, null);

		} else {
			this.enqueue(() -> ifExists.accept(lla, sender));
		}
	}

	@Override
	public void allLowLevelAgentsIds(
			JsonServiceResponseHandler<List<Integer>> handler) {
		JsonResponseSender<List<Integer>> sender = this.jsonResponseHelper
				.createSender(handler);
		sender.sendReceived();
		this.enqueue(() -> {
			sender.send(ResponseType.EXECUTING, null);
			List<Integer> list = this.simulation.getBuilding().getFloors()
					.stream()
					.map(Floor::getAgents)
					.map(Iterable::spliterator)
					.flatMap(i -> StreamSupport.stream(i, false))
					.map(Indexable::getID)
					.collect(Collectors.toList());
			sender.send(ResponseType.FINISHED, list);
		});
	}

	@Override
	public void moveTo(int objectId, Point coordinates, int floorId,
			JsonServiceResponseHandler<Point> handler) {
		LowLevelAgent lla = getLowLevelAgent(objectId);
		JsonResponseSender<Point> sender = this.jsonResponseHelper
				.createSender(handler);
		double x = coordinates.x;
		double y = coordinates.y;
		final AtomicBoolean finished = new AtomicBoolean(false);
		Floor floor = this.simulation.getBuilding().getFloors().get(0);// .getFloorById(floorId);
		sender.sendReceived();
		if (lla != null && lla
				.getHighLevelData() instanceof HighLevelCommandController) {
			this.enqueue(() -> {
				((HighLevelCommandController) lla.getHighLevelData())
						.enqueueCommand((a) -> {
					a.getLowLevelAgent()
							.approachTo(new Location(x, y, floor),
									new ApproachCallback() {

						@Override
						public void onTargetReached(LowLevelAgent agent) {
							sender.send(ResponseType.FINISHED,
									new Point(
											(int)agent.getLocation().getX(),
											(int)agent.getLocation().getY()));
							 finished.set(true);
						}

						@Override
						public void onSucess(
								LowLevelAgent agent) {
							sender.send(ResponseType.EXECUTING,
									new Point(
											(int)agent.getLocation().getX(),
											(int)agent.getLocation().getY()));

						}

						@Override
						public void onPathFinderError(
								PathFinderErrorReason reason) {
							sender.send(ResponseType.ERROR, null);

						}
					});
					return finished.get();
				});
			});

		} else {
			sender.send(ResponseType.ERROR, null);
		}
	}

	@Override
	public void getRandomLoc(JsonServiceResponseHandler<Point> handler) {
		JsonResponseSender<Point> sender = this.jsonResponseHelper
				.createSender(handler)
				.sendReceived();

		this.enqueue(() -> {
			sender.send(ResponseType.EXECUTING, null);
			Location loc = this.simulation.getBuilding().getRandomRoom()
					.getRandomLoc();
			sender.send(ResponseType.FINISHED,
					new Point((int) loc.getX(), (int) loc.getY()));
		});

	}

}
