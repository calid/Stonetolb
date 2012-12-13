package com.stonetolb.engine.physics;

import java.awt.Rectangle;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * PhysicsManager is an object that manages all physical objects in
 * a game world. 
 * @author james.baiera
 *
 */
public class PhysicsManager {
	private Deque<RigidBody> pool;
	private Map<Integer, RigidBody> active;
	private Set<Integer> updated;
	private int runningIndex;
	private int managedObjects;
	
	private static int DEFAULT_POOL_SIZE = 20;
	
	/**
	 * Construct manager with base pool size
	 */
	public PhysicsManager() {
		this(DEFAULT_POOL_SIZE);
	}
	
	/**
	 * Construct manager with given pool size
	 * 
	 * @param pStartingPoolSize
	 */
	public PhysicsManager(int pStartingPoolSize) {
		pool = new LinkedList<RigidBody>();
		active = new HashMap<Integer, RigidBody>();
		updated = new HashSet<Integer>();
		runningIndex = 0;
		
		if(pStartingPoolSize > 0) {
			managedObjects = pStartingPoolSize;
		}
		else {
			managedObjects = DEFAULT_POOL_SIZE;
		}
		
		fillPool(managedObjects);
	}
	
	/**
	 * Create the given amount of objects and add them to the
	 * object pool. Only if we're out of objects though.
	 * 
	 * @param pHowMuch
	 */
	private void fillPool(int pHowMuch) {
		if (pool.isEmpty()) {
			for(int count = 0; count < pHowMuch; count++) {
				pool.addLast(new RigidBody());
			}
		}
	}
	
	/**
	 * Submits a request for a {@link RigidBody} object that is registered to
	 * this world instance. Method returns an {@link Integer} that is used as
	 * a key to grab the actual {@link RigidBody}
	 * 
	 * @param pBounds
	 * @param pEvent
	 * @return
	 */
	public Integer requestRigidBody(Rectangle pBounds, CollisionEvent pEvent) {
		if(pool.isEmpty()) {
			fillPool(managedObjects);
			managedObjects *= 2;
		}
		
		Integer key = new Integer(runningIndex);
		runningIndex++;
		
		RigidBody toBeAdded = pool.removeLast();
		toBeAdded.init(pBounds, pEvent);
		active.put(key, toBeAdded);
		
		return key;
	}
	
	/**
	 * Given the key to a {@link RigidBody}, the manager removes it from the active
	 * set of objects and returns it to the object pool. Once this is called, the 
	 * {@link RigidBody} keyed by the given key should no longer be used by any created
	 * pointers to it.
	 * 
	 * @param key
	 */
	public void purgeRigidBody(Integer key) {
		RigidBody purged = active.remove(key);
		if(purged != null) {
			purged.clear();
			pool.addLast(purged);
		}
	}
	
	/**
	 * Returns the {@link RigidBody} that is bound to this key value. Users should be
	 * careful to not store {@link RigidBody} objects locally since once they are 
	 * returned to the pool, they will be utilized by a different object at some point
	 * 
	 * @param key
	 * @return
	 */
	public RigidBody getRigidBody(Integer key) {
		return active.get(key);
	}
	
	/**
	 * Given a key to a {@link RigidBody}, the world updates it's position and makes note
	 * that it has changed position recently
	 * 
	 * @param key
	 * @param x
	 * @param y
	 */
	public void updateObjectPosition(Integer key, int x, int y) {
		RigidBody toUpdate = active.get(key);
		if (toUpdate != null) {
			toUpdate.updatePosition(x, y);
			updated.add(key);
		}
	}
	
	/**
	 * Iterates over each {@link RigidBody} that and checks for collisions
	 * <p>
	 * Brute force. Must reiterate on
	 */
	public void update() {
		Set<CollisionEvent> collisions = new HashSet<CollisionEvent>();
		
		//Check Everything to see if they collided.
		for(Integer key : updated) {
			for(Integer otherKey : active.keySet()) {
				if(!key.equals(otherKey)) {
					CollisionEvent event = active.get(key).collidesWith(active.get(otherKey));

					if(event != null) {
						collisions.add(event);
					}
				}
			}
		}
		
		//Dispatch Collision Events
		for(CollisionEvent eachEvent : collisions) {
			eachEvent.onCollision();
		}
		
		updated.clear();
	}
}