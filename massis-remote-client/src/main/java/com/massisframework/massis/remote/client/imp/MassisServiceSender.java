package com.massisframework.massis.remote.client.imp;

import com.massisframework.jsoninvoker.reflect.JsonClientMessage;

public interface MassisServiceSender {

	public <T> void send(JsonClientMessage<T> item);

}
