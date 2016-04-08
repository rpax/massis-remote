package com.massisframework.jsoninvoker.reflect;

import java.util.concurrent.atomic.AtomicLong;

import com.massisframework.jsoninvoker.reflect.JsonServiceResponse.ResponseType;

public class JsonResponseHelper {

	private AtomicLong responseIdCounter = new AtomicLong(0);

	public <T> JsonResponseSender<T> createSender(
			JsonServiceResponseHandler<T> handler) {
		return new JsonResponseSenderImpl<>(handler,
				this.responseIdCounter.getAndIncrement());
	}

	public interface JsonResponseSender<T> {
		public void send(ResponseType responseType, T data);

		public default JsonResponseSender<T> sendReceived() {
			this.send(ResponseType.RECEIVED, null);
			return this;
		}
	}

	private static class JsonResponseSenderImpl<T>
			implements JsonResponseSender<T> {
		private final long responseId;
		private final AtomicLong responseNumberCounter;
		private JsonServiceResponseHandler<T> handler;

		public JsonResponseSenderImpl(JsonServiceResponseHandler<T> handler,
				long responseId) {
			this.responseId = responseId;
			this.handler = handler;
			this.responseNumberCounter = new AtomicLong(0);
		}

		public void send(ResponseType responseType, T data) {
			long responseNumber = responseNumberCounter.getAndIncrement();
			handler.handle(new JsonServiceResponse<T>(responseId,
					responseNumber, responseType, data));
		}
	}
}
