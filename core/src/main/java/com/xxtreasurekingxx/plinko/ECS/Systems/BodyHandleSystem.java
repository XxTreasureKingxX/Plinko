package com.xxtreasurekingxx.plinko.ECS.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.xxtreasurekingxx.plinko.ECS.Components.B2DComponent;
import com.xxtreasurekingxx.plinko.ECS.Components.TransformComponent;
import com.xxtreasurekingxx.plinko.ECS.ECSEngine;

import static com.xxtreasurekingxx.plinko.Core.*;

public class BodyHandleSystem extends IteratingSystem {
    private final World world;
    private final ECSEngine engine;

    public BodyHandleSystem(final ECSEngine engine, final World world) {
        super(Family.all(B2DComponent.class).get());
        this.world = world;
        this.engine = engine;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        B2DComponent b2DComponent = ECSEngine.b2dCmpMpr.get(entity);
        TransformComponent transformComponent = ECSEngine.trnCmpMpr.get(entity);

        if(b2DComponent.needsDelete) {
            destroyBody(b2DComponent, entity);
            return;
        }

        if(b2DComponent.needsBody) {
            createBody(entity, b2DComponent, transformComponent);
        }

        if(b2DComponent.needsTransform) {
            transformBody(b2DComponent, transformComponent);
        }

        if(b2DComponent.updateCollideStatus) {
            updateCollideStatus(b2DComponent);
        }
    }

    private void createBody(final Entity entity, final B2DComponent b2DComponent, final TransformComponent transformComponent) {
        b2DComponent.needsBody = false;

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(toMeters(transformComponent.renderPosition.x), toMeters(transformComponent.renderPosition.y));
        bodyDef.type = b2DComponent.bodyType;
        bodyDef.angle = transformComponent.angle;
        bodyDef.fixedRotation = false;

        b2DComponent.body = world.createBody(bodyDef);
        b2DComponent.body.setUserData(entity);
        b2DComponent.body.setLinearDamping(1);
        b2DComponent.body.setAngularDamping(1);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = b2DComponent.bodyShape;
        fixtureDef.filter.categoryBits = BIT_OBJECT;
        fixtureDef.filter.maskBits = -1;
        fixtureDef.restitution = 0.5f;
        fixtureDef.density = 1f;

        b2DComponent.body.createFixture(fixtureDef);
        b2DComponent.bodyShape.dispose();
    }

    private void destroyBody(final B2DComponent b2DComponent, final Entity entity) {
        b2DComponent.needsDelete = false;
        b2DComponent.body.getWorld().destroyBody(b2DComponent.body);
        b2DComponent.body = null;
        getEngine().removeEntity(entity);
    }

    private void transformBody(final B2DComponent b2DComponent, final TransformComponent transformComponent) {
        b2DComponent.needsTransform = false;
        b2DComponent.body.setTransform(toMeters(transformComponent.transformPosition.x), toMeters(transformComponent.transformPosition.y), 0);
        b2DComponent.body.setLinearVelocity(0, 0);
        b2DComponent.body.setAngularVelocity(0);
    }

    private void updateCollideStatus(final B2DComponent b2DComponent) {
        b2DComponent.updateCollideStatus = false;
        for(Fixture fixture : b2DComponent.body.getFixtureList()) {
            Filter filter = fixture.getFilterData();
            if(b2DComponent.shouldCollide) {
                filter.maskBits = -1;
            } else {
                filter.maskBits = 0;
            }
            fixture.setFilterData(filter);
        }
    }
}
