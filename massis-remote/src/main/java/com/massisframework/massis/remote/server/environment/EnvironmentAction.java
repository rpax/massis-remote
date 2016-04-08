package com.massisframework.massis.remote.server.environment;

import com.massisframework.massis.sim.AbstractSimulation;

public interface EnvironmentAction {

	public void execute(AbstractSimulation simulation, MassisResponseHandler responseHandler);
}
