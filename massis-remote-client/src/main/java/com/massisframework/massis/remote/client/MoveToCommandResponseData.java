package com.massisframework.massis.remote.client;

public class MoveToCommandResponseData {
	private Integer agentId = null;
	private Float x = null, y = null;
	private Integer floorId = null;

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
