package com.massisframework.massis.remote.client.imp;

import java.awt.Point;
import java.util.List;

import com.massisframework.jsoninvoker.reflect.JsonClientMessage;
import com.massisframework.jsoninvoker.reflect.JsonServiceResponseHandler;
import com.massisframework.massis.remote.services.agents.services.LowLevelAgentQueryService;

/**
 * TODO automatic code generation
 * 
 * @author rpax
 *
 */
@SuppressWarnings("unchecked")
public class LowLevelAgentQueryClient implements LowLevelAgentQueryService {

	private MassisServiceSender sender;

	public LowLevelAgentQueryClient(MassisServiceSender sender) {
		this.sender = sender;

	}

	public void getAgentsIdsInRange(int objectId, double range,
			JsonServiceResponseHandler<List<Integer>> handler) {

		this.sender.send(new JsonClientMessage("getAgentsIdsInRange", handler,
				List.class, objectId, range));

		// this.sender.send(
		// new JsonClientMessage<>(REMOTE_SERVICE_NAME,
		// "getAgentsIdsInRange", handler, List.class)
		// .addParam("objectId", objectId)
		// .addParam("range", range));
	}

	public void getAgentsInVisionRadio(int objectId,
			JsonServiceResponseHandler<List<Integer>> handler) {
		sender.send(new JsonClientMessage("getAgentsInVisionRadio", handler,
				List.class, objectId));

	}

	public void getRoomId(int objectId,
			JsonServiceResponseHandler<Integer> handler) {
		sender.send(new JsonClientMessage<>("getRoomId", handler, Integer.class,
				objectId));

	}

	public void getAgentsIdsInRoom(int objectId,
			JsonServiceResponseHandler<List<Integer>> handler) {

		// this.sender.send(
		// new JsonClientMessage<>(REMOTE_SERVICE_NAME,
		// "getAgentsIdsInRoom", handler, List.class)
		// .addParam("objectId", objectId));

	}

	public void getVisionRadio(int objectId,
			JsonServiceResponseHandler<Double> handler) {
		// this.sender.send(
		// new JsonClientMessage<>(REMOTE_SERVICE_NAME,
		// "getAgentsIdsInRoom", handler, Double.class)
		// .addParam("objectId", objectId));
	}

	public void getVisionRadioPoints(int objectId,
			JsonServiceResponseHandler<Point[]> handler) {
		throw new UnsupportedOperationException();

	}

	public void isInDoorArea(int objectId,
			JsonServiceResponseHandler<Boolean> handler) {
		// this.sender.send(
		// new JsonClientMessage<>(REMOTE_SERVICE_NAME, "isInDoorArea",
		// handler, Boolean.class)
		// .addParam("objectId", objectId));
	}

	public void isObjectPerceived(int objectId, int otherObjectId,
			JsonServiceResponseHandler<Boolean> handler) {
		// this.sender.send(
		// new JsonClientMessage<>(REMOTE_SERVICE_NAME, "isInDoorArea",
		// handler, Boolean.class)
		// .addParam("objectId", objectId)
		// .addParam("otherObjectId", otherObjectId));

	}

	public void isPointContainedInVisionArea(int objectId, Point point,
			JsonServiceResponseHandler<Boolean> handler) {
		// this.sender.send(
		// new JsonClientMessage<>(REMOTE_SERVICE_NAME,
		// "isPointContainedInVisionArea", handler, Boolean.class)
		// .addParam("objectId", objectId)
		// .addParam("point", point));
	}

	public void isObstacle(int objectId,
			JsonServiceResponseHandler<Boolean> handler) {
		// this.sender.send(
		// new JsonClientMessage<>(REMOTE_SERVICE_NAME, "isObstacle",
		// handler, Boolean.class)
		// .addParam("objectId", objectId));

	}

	public void isDynamic(int objectId,
			JsonServiceResponseHandler<Boolean> handler) {
		// this.sender.send(
		// new JsonClientMessage<>(REMOTE_SERVICE_NAME, "isObstacle",
		// handler, Boolean.class)
		// .addParam("objectId", objectId));

	}

	public void getBodyRadius(int objectId,
			JsonServiceResponseHandler<Double> handler) {
		// this.sender.send(
		// new JsonClientMessage<>(REMOTE_SERVICE_NAME, "getBodyRadius",
		// handler, Double.class)
		// .addParam("objectId", objectId));

	}

	public void isInNamedLocation(int objectId, String name, int radiusWithin,
			JsonServiceResponseHandler<Boolean> handler) {
		// this.sender.send(
		// new JsonClientMessage<>(REMOTE_SERVICE_NAME,
		// "isInNamedLocation", handler, Boolean.class)
		// .addParam("name", name)
		// .addParam("radiusWithin", radiusWithin)
		// .addParam("objectId", objectId));

	}

	public void getProperty(int objectId, String propertyName,
			JsonServiceResponseHandler<Object> handler) {
		// this.sender.send(
		// new JsonClientMessage<>(REMOTE_SERVICE_NAME, "getProperty",
		// handler, Object.class)
		// .addParam("propertyName", propertyName)
		// .addParam("objectId", objectId));

	}

	public void hasProperty(int objectId, String propertyName,
			JsonServiceResponseHandler<Boolean> handler) {
		// this.sender.send(
		// new JsonClientMessage<>(REMOTE_SERVICE_NAME, "hasProperty",
		// handler, Boolean.class)
		// .addParam("propertyName", propertyName)
		// .addParam("objectId", objectId));

	}

	public void allLowLevelAgentsIds(
			JsonServiceResponseHandler<List<Integer>> handler) {
		this.sender.send(new JsonClientMessage("allLowLevelAgentsIds", handler,
				List.class, 0));
	}

	@Override
	public void moveTo(int objectId, Point coordinates, int floorId,
			JsonServiceResponseHandler<Point> handler) {
		this.sender.send(
				new JsonClientMessage("moveTo", handler, Point.class, objectId,
						coordinates, floorId));
	}

	@Override
	public void getRandomLoc(
			JsonServiceResponseHandler<Point> handler) {
		this.sender.send(
				new JsonClientMessage<>("getRandomLoc", handler, Point.class,
						0));
	}

	@Override
	public void getCurrentLocation(int objectId,
			JsonServiceResponseHandler<Point> handler) {
		this.sender.send(new JsonClientMessage<>("getLocation", handler,
				Point.class, objectId));
	}

	@Override
	public void setProperty(int objectId, String propertyName,
			Object propertyVal, JsonServiceResponseHandler<Object> handler) {

		this.sender.send(
				new JsonClientMessage<>("setProperty", handler, Object.class,
						objectId, propertyName, propertyVal));

	}
}