package com.massisframework.massis.remote.highlevel;

@FunctionalInterface
public interface HighLevelAction {
		
	public boolean execute(HighLevelCommandController controller);
}
