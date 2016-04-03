package com.massisframework.massis.remote.commands;

import com.massisframework.massis.model.location.Location;
import com.massisframework.massis.remote.MassisServer;
import com.massisframework.massis.remote.commands.MoveToCommand.MoveToResponse;
import com.massisframework.massis.remote.highlevel.HighLevelCommandController;

public class MoveRandomCommand implements AbstractCommand<MoveToResponse> {

	private int agentId;

	public MoveRandomCommand(int agentId) {
		this.agentId = agentId;
	}

	@Override
	public void runCommand(MassisServer server,
			CommandResponseCallback<MoveToResponse> callback) {
		HighLevelCommandController hlc = server.getHighLevelController(agentId);
		if (hlc == null) {
			callback.error(server.getTick(), null,
					"Unable to control remote object");
		}
		Location loc = hlc.getLowLevelAgent().getLocation().getFloor()
				.getRandomRoom().getRandomLoc();
		MoveToCommand cmd = new MoveToCommand(this.agentId, loc.getX(),
				loc.getY(), loc.getFloor().getID());
		cmd.runCommand(server, callback);
	}

}
