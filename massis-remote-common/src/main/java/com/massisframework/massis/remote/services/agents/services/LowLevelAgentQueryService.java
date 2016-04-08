package com.massisframework.massis.remote.services.agents.services;

import java.awt.Point;
import java.util.List;

import com.massisframework.jsoninvoker.annotations.JsonMethodParam;
import com.massisframework.jsoninvoker.annotations.JsonServiceMethod;
import com.massisframework.jsoninvoker.reflect.JsonServiceResponseHandler;

public interface LowLevelAgentQueryService {

	/**
	 * 
	 * 
	 * @param range
	 *            the radius of the circle.
	 * @return the ids of the agents in the range provided
	 */
	@JsonServiceMethod(name = "getAgentsIdsInRange", description =
	//
	"Returns the agents' id lying in the area defined by the "
			+ "location of the agent and the radius defined by the "
			+ "range provided. It is an alternative method to "
			+ "getAgentsInVisionRadio. The former is preferred to be used, "
			+ "but this method can be useful for creating new low-level "
			+ "operations.")
	public void getAgentsIdsInRange(
			@JsonMethodParam("objectId") int objectId,
			@JsonMethodParam("range") double range,
			JsonServiceResponseHandler<List<Integer>> handler);

	/**
	 * Returns the <i>visible</i> agents in the vision radio of this agent.
	 *
	 * @return the agents in the vision radio of this agent.
	 */
	@JsonServiceMethod(name = "getAgentsInVisionRadio", description = "")
	public void getAgentsInVisionRadio(
			@JsonMethodParam("objectId") int objectId,
			JsonServiceResponseHandler<List<Integer>> handler);

	/**
	 * Returns the current room id of the agent
	 *
	 * @return The room where the agent is
	 */
	@JsonServiceMethod(name = "getRoomId", description = "")
	public void getRoomId(@JsonMethodParam("objectId") int objectId,
			JsonServiceResponseHandler<Integer> handler);

	/**
	 * Retrieves the agents in the current room of the agent. Equivalent method
	 * to the call {@link SimRoom#getPeopleIn()}
	 *
	 * @return the agents ids in the current room of the agent.
	 */
	@JsonServiceMethod(name = "getAgentsIdsInRoom", description = "")
	public void getAgentsIdsInRoom(
			@JsonMethodParam("objectId") int objectId,
			JsonServiceResponseHandler<List<Integer>> handler);

	/**
	 * Retrieves the vision radio of this agent
	 *
	 * @return the vision radio of this agent
	 */
	@JsonServiceMethod(name = "getVisionRadio", description = "")
	public void getVisionRadio(@JsonMethodParam("objectId") int objectId,
			JsonServiceResponseHandler<Double> handler);

	/**
	 * Retrieves the shape that defines the vision radio shape of the agent as
	 * an array of points. It is different from
	 * {@link #getAgentsInVisionRadio() } due to performance reasons. If the
	 * vision area of the agent is uniform (e.g not <i>directed</i>), the formed
	 * method is preferred.
	 *
	 * @return the points forming the vision radio polygon
	 */
	@JsonServiceMethod(name = "getVisionRadioPoints", description = "")
	public void getVisionRadioPoints(
			@JsonMethodParam("objectId") int objectId,
			JsonServiceResponseHandler<Point[]> handler);

	/**
	 * Returns if the agent it is in a door or not. Mostly used for performance
	 * reasons.
	 * <p>
	 * Due to SweetHome3D internal representation of the building, if the agent
	 * is under a door, exists the possibility that the agent it is not located
	 * in any room. Although the method {@link #getRoom() } will return the last
	 * room where the agent was, this method it is used in some low-level
	 * operations
	 * </p>
	 *
	 * @return if the agent is in a door or not
	 */
	@JsonServiceMethod(name = "isInDoorArea", description = "")
	public void isInDoorArea(@JsonMethodParam("objectId") int objectId,
			JsonServiceResponseHandler<Boolean> handler);

	/**
	 * Returns if another low level agent is in the vision radio of this agent.
	 * <p>
	 * This method is equivalent to calling {@link #getAgentsInVisionRadio() }
	 * and checking if it is contained in that collection, but this method is
	 * faster.
	 * </p>
	 * 
	 * @param objectId
	 * @param otherObjectId
	 *            the id of another low level agent
	 * @return if the agent provided lies in the vision radio of this agent and
	 *         is in the same room of this agent
	 */
	@JsonServiceMethod(name = "isObjectPerceived", description = "")
	public void isObjectPerceived(@JsonMethodParam("objectId") int objectId,
			@JsonMethodParam("objectId") int otherObjectId,
			JsonServiceResponseHandler<Boolean> handler);

	/**
	 * @param p
	 *            the point to check
	 * @return if the point is contained in the vision area
	 */
	@JsonServiceMethod(name = "isPointContainedInVisionArea", description = "")
	public void isPointContainedInVisionArea(
			@JsonMethodParam("objectId") int objectId,
			@JsonMethodParam("point") Point point,
			JsonServiceResponseHandler<Boolean> handler);

	/**
	 * Returns if this agent it is an obstacle or not.
	 * <p>
	 * MASSIS does not take into account the elevation of the agents, so it is
	 * possible that other agents try to avoid some elements of the environment
	 * that they are not an obstacle (e.g a video camera)
	 * </p>
	 *
	 * @return if this agent it is an obstacle or not.
	 */
	@JsonServiceMethod(name = "isObstacle", description = "")
	public void isObstacle(@JsonMethodParam("objectId") int objectId,
			JsonServiceResponseHandler<Boolean> handler);

	/**
	 * Returns if this agent can move or not.
	 * <p>
	 * Used for performance reasons. If the agent is marked in its metadata with
	 * the flag {@link SimObjectProperty#IS_DYNAMIC} to {@code false},
	 * pathfinding algorithms will consider this agent as an <i>static</i>
	 * obstacle, increasing performance.
	 * </p>
	 *
	 * @return
	 */
	@JsonServiceMethod(name = "isDynamic", description = "")
	public void isDynamic(@JsonMethodParam("objectId") int objectId,
			JsonServiceResponseHandler<Boolean> handler);

	/**
	 * Retrieves the body radius of the agent.
	 * <p>
	 * That is, the radio of the polygon representing this agent
	 * </p>
	 *
	 * @see Occluder
	 * @see PolygonHolder
	 * @see KPolygon
	 * @return the body radius of this agent
	 */
	@JsonServiceMethod(name = "getBodyRadius", description = "")
	public void getBodyRadius(@JsonMethodParam("objectId") int objectId,
			JsonServiceResponseHandler<Double> handler);

	/**
	 * Returns if the agent is around a named location.
	 * <p>
	 * A named location is an special element of the environment, that
	 * represents an special point. (For example, an emergency exit). For
	 * creating a Named location, the metadata of an element of the building
	 * should contain the key {@link SimObjectProperty#POINT_OF_INTEREST}
	 * </p>
	 *
	 * @param name
	 *            the name of this location
	 * @param radiusWithin
	 *            the radio for considering this agent in that location. If the
	 *            {@link SimLocation#distance2D(straightedge.geom.KPoint)} is
	 *            lower than {@code radiusWithIn}, the result will be
	 *            {@code true}, being {@code false} otherwise.
	 * @return
	 */
	@JsonServiceMethod(name = "isInNamedLocation", description = "")
	public void isInNamedLocation(@JsonMethodParam("objectId") int objectId,
			@JsonMethodParam("name") String name,
			@JsonMethodParam("radiusWithin") int radiusWithin,
			JsonServiceResponseHandler<Boolean> handler);

	/**
	 * Returns a dynamic property of the agent.
	 * <p>
	 * This properties can be used for High-Level operations.
	 * </p>
	 * .
	 *
	 * @param propertyName
	 *            a property of the agent
	 * @return Its value. If the property is not present, {@code null} is
	 *         returned.
	 */
	@JsonServiceMethod(name = "getProperty", description = "")
	public void getProperty(@JsonMethodParam("objectId") int objectId,
			@JsonMethodParam("propertyName") String propertyName,
			JsonServiceResponseHandler<Object> handler);

	/**
	 * Returns if the agent contains the provided property
	 *
	 * @param propertyName
	 *            the property to be checked
	 * @return if the agent contains that property or not.
	 */
	@JsonServiceMethod(name = "hasProperty", description = "")
	public void hasProperty(
			//
			@JsonMethodParam("objectId") int objectId,
			@JsonMethodParam("propertyName") String propertyName,
			JsonServiceResponseHandler<Boolean> handler);

	@JsonServiceMethod(name = "allLowLevelAgentsIds", description = "")
	public void allLowLevelAgentsIds(
			JsonServiceResponseHandler<List<Integer>> handler);

	@JsonServiceMethod(name = "moveTo", description = "")
	public void moveTo(@JsonMethodParam("objectId") int objectId,
			@JsonMethodParam("coordinates") Point coordinates,
			@JsonMethodParam("floorId") int floorId,
			JsonServiceResponseHandler<Point> handler);

	@JsonServiceMethod(name = "getRandomLoc", description = "")
	public void getRandomLoc(JsonServiceResponseHandler<Point> handler);

}
