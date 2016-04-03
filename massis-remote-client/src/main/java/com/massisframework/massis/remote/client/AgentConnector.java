package com.massisframework.massis.remote.client;

import com.massisframework.massis.remote.common.CommandResponse;

public interface AgentConnector {

	public void connect(String connectionURL, ConnectionCallback callback);

	public <RData> void sendCommand(AgentRemoteCommand cmd,
			ResponseCallback<RData> response);
	/**
	 * <pre>
	 * c.sendCommand(new MoveToCommand(agentId, x, y, floorId),
	 * 		new ResponseCallback<MoveToCommandResponseData>() {
	 * 
	 * 			public void onResponse(
	 * 					CommandResponse<MoveToCommandResponseData> response) {
	 * 	
	 * 			}
	 * 		});
	 * </pre>
	 */
}
