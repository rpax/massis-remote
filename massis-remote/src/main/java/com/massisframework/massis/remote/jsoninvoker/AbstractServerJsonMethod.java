package com.massisframework.massis.remote.jsoninvoker;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.massisframework.jsoninvoker.reflect.JsonServiceResponse;
import com.massisframework.jsoninvoker.reflect.JsonServiceResponse.ResponseType;
import com.massisframework.jsoninvoker.reflect.JsonServiceResponseHandler;
import com.massisframework.jsoninvoker.services.JsonMethod;
import com.massisframework.massis.model.agents.LowLevelAgent;
import com.massisframework.massis.model.building.LocationHolder;
import com.massisframework.massis.model.building.SimulationObject;
import com.massisframework.massis.model.location.Location;
import com.massisframework.massis.sim.Simulation;
import com.massisframework.massis.util.Indexable;

public class AbstractServerJsonMethod<R>  implements JsonMethod<R>  {

	protected Simulation simulation;
	private final AtomicLong responseNumberCounter = new AtomicLong(0);
	private final long methodId;

	public AbstractServerJsonMethod(Simulation simulation,
			Supplier<Long> methodIdSupplier) {
		this.simulation = simulation;
		this.methodId = methodIdSupplier.get();
	}

	protected <T> void sendMessage(JsonServiceResponseHandler<T> handler,
			ResponseType rtype, T data) {
		handler.handle(new JsonServiceResponse<T>(this.methodId,
				this.responseNumberCounter.getAndIncrement(), rtype, data));
	}

	protected <T> void sendReceived(JsonServiceResponseHandler<T> handler) {
		this.sendMessage(handler, ResponseType.RECEIVED, null);
	}

	protected <T> void sendError(JsonServiceResponseHandler<T> handler) {
		this.sendMessage(handler, ResponseType.ERROR, null);
	}

	protected <T> void sendExecuting(JsonServiceResponseHandler<T> handler) {
		this.sendExecuting(handler, null);
	}

	protected <T> void sendExecuting(JsonServiceResponseHandler<T> handler,
			T data) {
		this.sendMessage(handler, ResponseType.EXECUTING, data);
	}

	protected <T> void sendFinished(JsonServiceResponseHandler<T> handler,
			T data) {
		this.sendMessage(handler, ResponseType.FINISHED, data);
	}

	protected LowLevelAgent getLowLevelAgent(int objectId) {
		SimulationObject simObj = this.simulation.getBuilding()
				.getSimulationObject(objectId);
		if (simObj == null)
			return null;
		if (simObj instanceof LowLevelAgent)
			return (LowLevelAgent) simObj;
		return null;
	}

	protected static Point2D toPoint2D(Location loc) {
		return new Point2D.Double(loc.getX(), loc.getY());
	}

	protected static Point2D toPoint2D(LocationHolder lh) {
		return toPoint2D(lh.getLocation());
	}

	protected static List<Integer> getIDs(
			Iterable<? extends Indexable> iterable) {
		return StreamSupport.stream(iterable.spliterator(), false)
				.map(Indexable::getID)
				.collect(Collectors.toList());
	}

}
