package com.massisframework.massis.remote.commands;

import java.util.concurrent.atomic.AtomicBoolean;

import com.massisframework.massis.model.agents.LowLevelAgent;
import com.massisframework.massis.model.location.Location;
import com.massisframework.massis.model.managers.movement.ApproachCallback;
import com.massisframework.massis.pathfinding.straightedge.FindPathResult.PathFinderErrorReason;
import com.massisframework.massis.remote.MassisServer;
import com.massisframework.massis.remote.highlevel.HighLevelCommandController;

public class MoveToCommand
		implements AbstractCommand<MoveToCommand.MoveToResponse> {

	private int agentId;
	private double x, y;
	private int floorId;
	private transient Location loc;

	public MoveToCommand(int agentId, double x, double y, int floorId) {
		this.agentId = agentId;
		this.x = x;
		this.y = y;
		this.floorId = floorId;

	}

	@Override
	public void runCommand(MassisServer server,
			CommandResponseCallback<MoveToResponse> callback) {
		callback.received(server.getTick());

		HighLevelCommandController hlc = server.getHighLevelController(agentId);
		if (hlc == null) {
			callback.error(server.getTick(), null,
					"Unable to control remote object");
		} else {
			if (this.loc == null) {
				this.loc = new Location(x, y, server.getSimulation()
						.getBuilding().getFloorById(floorId));
			}
			final AtomicBoolean finished = new AtomicBoolean(false);
			hlc.enqueueCommand((a) -> {
				a.getLowLevelAgent().approachTo(loc, new ApproachCallback() {
					@Override
					public void onTargetReached(LowLevelAgent agent) {
						callback.finished(server.getTick(),
								new MoveToResponse(agent),
								"Target reached");
						finished.set(true);

					}

					@Override
					public void onSucess(LowLevelAgent agent) {
						callback.running(server.getTick(),
								new MoveToResponse(agent));
						// finished.set(false);
					}

					@Override
					public void onPathFinderError(
							PathFinderErrorReason reason) {
						callback.error(server.getTick(),
								new MoveToResponse(a.getLowLevelAgent()),
								"Error: " + reason);
						finished.set(true);

					}
				});
				return finished.get();
			});
		}

	}

	public static class MoveToResponse {
		private Integer agentId = null;
		private Float x = null, y = null;
		private Integer floorId = null;

		public MoveToResponse(LowLevelAgent agent) {
			this(agent.getID(), (float) agent.getLocation().getX(),
					(float) agent.getLocation().getY(),
					agent.getLocation().getFloor().getID());
		}

		public MoveToResponse(Integer agentId, Float x, Float y,
				Integer floorId) {
			super();
			this.agentId = agentId;
			this.x = x;
			this.y = y;
			this.floorId = floorId;
		}

		public Integer getAgentId() {
			return agentId;
		}

		public Float getX() {
			return x;
		}

		public Float getY() {
			return y;
		}

		public Integer getFloorId() {
			return floorId;
		}

	}

}
