package com.massisframework.massis.remote.client.test;

import java.net.URI;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.logging.Level;

import com.massisframework.jsoninvoker.reflect.JsonServiceResponse;
import com.massisframework.jsoninvoker.reflect.JsonServiceResponse.ResponseType;
import com.massisframework.jsoninvoker.reflect.JsonServiceResponseHandler;
import com.massisframework.massis.remote.client.MassisSocketClient;
import com.massisframework.massis.remote.client.imp.LowLevelAgentQueryClient;

public class TestClientSend {

	public static void main(String[] args) {

		try {
			MassisSocketClient socket = new MassisSocketClient(
					new URI("ws://127.0.0.1:4567/massis"));

			socket.setLogLevel(Level.FINE);

			if (socket.connectBlocking()) {

				LowLevelAgentQueryClient q = new LowLevelAgentQueryClient(
						socket);

				Semaphore sem = new Semaphore(0);

				ArrayList<Integer> ids = new ArrayList<>();
				q.allLowLevelAgentsIds((res) -> {
					if (res.getResponseType() == ResponseType.FINISHED) {
						ids.addAll(res.getData());
						sem.release();
					}
				});
				sem.acquire();
				
				
				System.out.println("ALL AGENTS: "+ids);
				
				
				q.getAgentsInVisionRadio(18, (res) -> {
					if (res.getData() != null)
						System.out.println("Agents in vision radio for: "
								+ 18 + ": " + res.getData());
				});

				//
				//
				// ArrayList<Integer> ids = new ArrayList<>();
				//
				// q.allLowLevelAgentsIds((resp) -> {
				// if (resp.getResponseType() == ResponseType.FINISHED) {
				// for (Number i : resp.getData()) {
				// ids.add(i.intValue());
				// }
				// sem.release();
				// }
				// });
				//
				// sem.acquire();
				//
				// ids.forEach(id -> q.getRandomLoc((resp) -> {
				// if (resp.getResponseType() == ResponseType.FINISHED) {
				// q.moveTo(id, resp.getData(), 0, (r2) -> {
				// visionRadioPrinter.accept(id);
				// // if (r2.getResponseType() ==
				// // ResponseType.FINISHED) {
				// // // sem.release();
				// // }
				// });
				// }
				// }));

				// sem.acquire(ids.size());
				//
				// socket.close();

			}

		} catch (Exception e)

		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static <T> CompletableFuture<T> fut(
			BiConsumer<CompletableFuture<T>, JsonServiceResponseHandler<T>> biconsumer) {
		CompletableFuture<T> cf = new CompletableFuture<T>();
		FutureRespHandler<T> handler = new FutureRespHandler<T>(cf);
		biconsumer.accept(cf, handler);
		return cf;
	}

	public static class FutureRespHandler<T>
			implements JsonServiceResponseHandler<T> {

		private CompletableFuture<T> cf;

		public FutureRespHandler(CompletableFuture<T> cf) {
			this.cf = cf;
		}

		@Override
		public void handle(JsonServiceResponse<T> obj) {
			switch (obj.getResponseType()) {
			case ERROR:
				cf.completeExceptionally(new RuntimeException(
						"Error when handling request: " + obj));
				break;
			case EXECUTING:
				break;
			case FINISHED:
				cf.complete(obj.getData());
				break;
			case RECEIVED:
				break;
			default:
				break;

			}
		}

	}

}
