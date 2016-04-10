package com.massisframework.massis.remote.jsoninvoker.lowlevel;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import com.massisframework.jsoninvoker.reflect.JsonServiceResponse.ResponseType;
import com.massisframework.jsoninvoker.reflect.JsonServiceResponseHandler;
import com.massisframework.jsoninvoker.services.JsonMethod.JsonMethod1;
import com.massisframework.massis.model.agents.LowLevelAgent;
import com.massisframework.massis.remote.jsoninvoker.AbstractServerJsonMethod;
import com.massisframework.massis.sim.Simulation;

public class GetAgentsInVisionRadio
		extends AbstractServerJsonMethod<List<Integer>>
		implements JsonMethod1<Integer, List<Integer>> {

	public GetAgentsInVisionRadio(Simulation simulation,
			Supplier<Long> methodIdSupplier) {
		super(simulation, methodIdSupplier);
	}

	@Override
	public void invoke(Integer objectId,
			JsonServiceResponseHandler<List<Integer>> handler) {
		this.simulation.schedule.scheduleOnce((ss) -> {
			LowLevelAgent lla = this.getLowLevelAgent(objectId);
			if (lla == null) {
				this.sendMessage(handler, ResponseType.ERROR,
						Collections.emptyList());
			} else {
				this.sendMessage(handler, ResponseType.EXECUTING,
						Collections.emptyList());
				List<Integer> ids = getIDs(lla.getAgentsInVisionRadio());
				this.sendMessage(handler, ResponseType.FINISHED, ids);
			}
		});
	}

}
