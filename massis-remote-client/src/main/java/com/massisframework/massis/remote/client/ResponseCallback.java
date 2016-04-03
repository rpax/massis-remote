package com.massisframework.massis.remote.client;

import com.massisframework.massis.remote.common.CommandResponse;

public interface ResponseCallback<T> {

	public void onResponse(CommandResponse<T> response);
}
