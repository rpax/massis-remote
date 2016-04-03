package com.massisframework.massis.remote.commands;

import com.massisframework.massis.remote.MassisServer;
import com.massisframework.massis.remote.highlevel.HighLevelCommandController;

public class SayHelloCommand implements AbstractCommand<String> {

	private int agentId;

	public SayHelloCommand(int agentId) {
		this.agentId = agentId;
	}

	public void runCommand(MassisServer server,
			CommandResponseCallback<String> callback) {
		callback.received(server.getTick(), "Received");
		HighLevelCommandController hlc = server.getHighLevelController(agentId);
		if (hlc == null) {
			callback.error(server.getTick(), null,"Unable to control remote object");
		} else {
			hlc.enqueueCommand((agent) -> {
				int agentId = agent.getLowLevelAgent().getID();
				callback.finished(server.getTick(),
						"Hello! I am agent #" + agentId);
				return true;
			});
		}
	}
}
