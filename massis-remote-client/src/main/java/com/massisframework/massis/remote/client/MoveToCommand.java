package com.massisframework.massis.remote.client;

public class MoveToCommand implements AgentRemoteCommand {

	private int agentId;
	private double x, y;
	private int floorId;

	public MoveToCommand(int agentId, double x, double y, int floorId) {
		super();
		this.agentId = agentId;
		this.x = x;
		this.y = y;
		this.floorId = floorId;

	}
}
