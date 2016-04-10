package com.massisframework.massis.remote.jsoninvoker.lowlevel;

import java.awt.geom.Point2D;
import java.util.function.Supplier;

import com.massisframework.jsoninvoker.reflect.JsonServiceResponseHandler;
import com.massisframework.jsoninvoker.services.JsonMethod.JsonMethod1;
import com.massisframework.massis.model.building.SimulationObject;
import com.massisframework.massis.remote.jsoninvoker.AbstractServerJsonMethod;
import com.massisframework.massis.sim.Simulation;

public class GetCurrentLocation extends AbstractServerJsonMethod<Point2D>
		implements JsonMethod1<Integer, Point2D> {

	public GetCurrentLocation(Simulation simulation,
			Supplier<Long> methodIdSupplier) {
		super(simulation, methodIdSupplier);
	}

	@Override
	public void invoke(Integer simObjId,
			JsonServiceResponseHandler<Point2D> handler) {

		this.sendReceived(handler);

		this.simulation.schedule.scheduleOnce((ss) -> {
			this.sendExecuting(handler);
			SimulationObject simObj = this.simulation.getBuilding()
					.getSimulationObject(simObjId);

			if (simObj != null) {
				this.sendFinished(handler, toPoint2D(simObj));
			} else {
				this.sendError(handler);
			}
		});

	}

}
