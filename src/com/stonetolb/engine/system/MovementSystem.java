package com.stonetolb.engine.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.stonetolb.engine.component.movement.Rotation;
import com.stonetolb.engine.component.movement.Velocity;
import com.stonetolb.engine.component.position.Position;

public class MovementSystem extends EntityProcessingSystem {
	private @Mapper ComponentMapper<Position> positionMap;
	private @Mapper ComponentMapper<Velocity> velocityMap;
	private @Mapper ComponentMapper<Rotation> rotationMap;
	
	public MovementSystem() {
		super(Aspect.getAspectForAll(Position.class, Velocity.class, Rotation.class));
	}

	@Override
	protected void process(Entity arg0) {
		Position pos = positionMap.get(arg0);
		Velocity vel = velocityMap.get(arg0);
		Rotation rot = rotationMap.get(arg0);

		pos.setX(pos.getX() + (float)((((world.getDelta() * (vel.getVelocity())) / 1000)) * Math.cos(Math.toRadians(rot.getRotation()))));
		pos.setY(pos.getY() + (float)((((world.getDelta() * (vel.getVelocity())) / 1000)) * Math.sin(Math.toRadians(rot.getRotation()))));
	}

}
