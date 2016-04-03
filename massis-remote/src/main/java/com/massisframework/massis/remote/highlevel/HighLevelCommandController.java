package com.massisframework.massis.remote.highlevel;

import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.massisframework.massis.model.agents.HighLevelController;
import com.massisframework.massis.model.agents.LowLevelAgent;

public class HighLevelCommandController extends HighLevelController {

	private static final long serialVersionUID = 1L;
	private Queue<HighLevelAction> pending;

	public HighLevelCommandController(LowLevelAgent agent,
			Map<String, String> metadata, String resourcesFolder) {
		super(agent, metadata, resourcesFolder);
		this.agent.setHighLevelData(this);
		this.pending = new ConcurrentLinkedQueue<>();
	}

	public void enqueueCommand(HighLevelAction cmd) {
		this.pending.add(cmd);
	}

	@Override
	public void stop() {
		/*
		 * Clean resources, threads...etc
		 */
	}

	@Override
	public void step() {
		for (Iterator<HighLevelAction> it = pending.iterator(); it.hasNext();) {
			HighLevelAction action = it.next();
			if (action.execute(this)) {
				it.remove();
			}
		}
	}

	public LowLevelAgent getLowLevelAgent() {
		return this.agent;
	}
}