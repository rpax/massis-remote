package com.massisframework.massis.remote;

import static spark.Spark.init;
import static spark.Spark.webSocket;

public class MassisServer {

	public MassisServer() {

	}

	public static void main(String[] args) {

		webSocket("/massis", MassisServerWebSocket.class);
		init();

	}

}
