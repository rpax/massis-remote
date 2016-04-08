package com.massisframework.massis.remote.jsoninvoker.lowlevel;

import java.util.function.Supplier;

import com.massisframework.jsoninvoker.reflect.JsonServiceResponseHandler;
import com.massisframework.jsoninvoker.reflect.JsonServiceResponse.ResponseType;
import com.massisframework.jsoninvoker.services.JsonMethod.JsonMethod1;
import com.massisframework.massis.model.agents.LowLevelAgent;
import com.massisframework.massis.remote.jsoninvoker.AbstractServerJsonMethod;
import com.massisframework.massis.sim.Simulation;

public class GetRoomId extends AbstractServerJsonMethod
		implements JsonMethod1<Integer, Integer> {

	public GetRoomId(Simulation simulation, Supplier<Long> methodIdSupplier) {
		super(simulation, methodIdSupplier);
	}

	@Override
	public void invoke(final Integer objectId,
			JsonServiceResponseHandler<Integer> handler) {
		this.simulation.schedule.scheduleOnce((ss) -> {
			LowLevelAgent lla = this.getLowLevelAgent(objectId);
			if (lla == null) {
				this.sendMessage(handler, ResponseType.ERROR, null);
			} else {
				this.sendMessage(handler, ResponseType.EXECUTING, -1);
				int roomId = lla.getRoom().getID();
				this.sendMessage(handler, ResponseType.FINISHED, roomId);
			}
		});
	}

}
