package com.massisframework.massis.remote.commands;

import com.massisframework.massis.remote.MassisServer;

public interface AbstractCommand<CommandResult> {

	public void runCommand(MassisServer massisServer,
			CommandResponseCallback<CommandResult> callback);

}
