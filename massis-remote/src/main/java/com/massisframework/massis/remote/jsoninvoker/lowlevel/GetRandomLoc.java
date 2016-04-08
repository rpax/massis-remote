package com.massisframework.massis.remote.jsoninvoker.lowlevel;

import java.awt.geom.Point2D;
import java.util.function.Supplier;

import com.massisframework.jsoninvoker.reflect.JsonServiceResponseHandler;
import com.massisframework.jsoninvoker.reflect.JsonServiceResponse.ResponseType;
import com.massisframework.jsoninvoker.services.JsonMethod.JsonMethod1;
import com.massisframework.massis.model.location.Location;
import com.massisframework.massis.remote.jsoninvoker.AbstractServerJsonMethod;
import com.massisframework.massis.sim.Simulation;

public class GetRandomLoc extends AbstractServerJsonMethod
		implements JsonMethod1<Integer, Point2D> {

	public GetRandomLoc(Simulation simulation,
			Supplier<Long> methodIdSupplier) {
		super(simulation, methodIdSupplier);
	}

	@Override
	public void invoke(Integer objectId,
			JsonServiceResponseHandler<Point2D> handler) {

		this.sendMessage(handler, ResponseType.RECEIVED, null);
		this.simulation.schedule.scheduleOnce((ss) -> {
			this.sendMessage(handler, ResponseType.EXECUTING, null);

			Location loc = this.simulation.getBuilding().getRandomRoom()
					.getRandomLoc();
			this.sendMessage(handler, ResponseType.FINISHED,
					new Point2D.Double(loc.getX(), loc.getY()));

			this.sendMessage(handler, ResponseType.FINISHED, null);
		});
	}

}
