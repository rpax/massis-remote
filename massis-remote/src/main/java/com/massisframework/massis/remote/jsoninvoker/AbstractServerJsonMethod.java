package com.massisframework.massis.remote.jsoninvoker;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.massisframework.jsoninvoker.reflect.JsonServiceResponse;
import com.massisframework.jsoninvoker.reflect.JsonServiceResponse.ResponseType;
import com.massisframework.jsoninvoker.reflect.JsonServiceResponseHandler;
import com.massisframework.massis.model.agents.LowLevelAgent;
import com.massisframework.massis.model.building.SimulationObject;
import com.massisframework.massis.sim.Simulation;
import com.massisframework.massis.util.Indexable;

public class AbstractServerJsonMethod /* implements JsonMethod<R> */ {

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

	protected LowLevelAgent getLowLevelAgent(int objectId) {
		SimulationObject simObj = this.simulation.getBuilding()
				.getSimulationObject(objectId);
		if (simObj == null)
			return null;
		if (simObj instanceof LowLevelAgent)
			return (LowLevelAgent) simObj;
		return null;
	}

	protected static List<Integer> getIDs(
			Iterable<? extends Indexable> iterable) {
		return StreamSupport.stream(iterable.spliterator(), false)
				.map(Indexable::getID)
				.collect(Collectors.toList());
	}

}
