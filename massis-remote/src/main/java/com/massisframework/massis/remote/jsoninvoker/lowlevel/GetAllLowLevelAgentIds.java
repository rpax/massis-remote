package com.massisframework.massis.remote.jsoninvoker.lowlevel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.massisframework.jsoninvoker.reflect.JsonServiceResponseHandler;
import com.massisframework.jsoninvoker.reflect.JsonServiceResponse.ResponseType;
import com.massisframework.jsoninvoker.services.JsonMethod.JsonMethod1;
import com.massisframework.massis.model.building.Floor;
import com.massisframework.massis.remote.jsoninvoker.AbstractServerJsonMethod;
import com.massisframework.massis.sim.Simulation;

public class GetAllLowLevelAgentIds extends AbstractServerJsonMethod<List<Integer>>
		implements JsonMethod1<Integer, List<Integer>> {

	public GetAllLowLevelAgentIds(Simulation simulation,
			Supplier<Long> methodIdSupplier) {
		super(simulation, methodIdSupplier);
	}

	@Override
	public void invoke(Integer p0,
			JsonServiceResponseHandler<List<Integer>> handler) {
		this.sendMessage(handler, ResponseType.RECEIVED, null);
		this.simulation.schedule.scheduleOnce((ss) -> {
			this.sendMessage(handler, ResponseType.EXECUTING, null);
			List<Integer> ids = this.simulation.getBuilding().getFloors()
					.stream()
					.map(Floor::getAgents)
					.map(ags -> getIDs(ags))
					.flatMap(Collection::stream)
					.collect(Collectors.toList());
			this.sendMessage(handler, ResponseType.FINISHED, ids);
		});

	}

}
