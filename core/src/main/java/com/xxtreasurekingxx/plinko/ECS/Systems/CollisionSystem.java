package com.xxtreasurekingxx.plinko.ECS.Systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.xxtreasurekingxx.plinko.Core;
import com.xxtreasurekingxx.plinko.ECS.Components.B2DComponent;
import com.xxtreasurekingxx.plinko.ECS.Components.BallComponent;
import com.xxtreasurekingxx.plinko.ECS.Components.ExitZoneComponent;
import com.xxtreasurekingxx.plinko.ECS.Components.TransformComponent;import com.xxtreasurekingxx.plinko.ECS.ECSEngine;
import com.xxtreasurekingxx.plinko.World.WorldContactListener;

public class CollisionSystem extends EntitySystem implements WorldContactListener.CollisionListener {
    private final ECSEngine engine;
    private final Core core;
    private final GameEventSystem gameEventSystem;
    private PhysicsSystem physicsSystem;

    public CollisionSystem(final Core core, final ECSEngine engine, final WorldContactListener contactListener, final GameEventSystem gameEventSystem) {
        this.engine = engine;
        this.core = core;
        this.gameEventSystem = gameEventSystem;
        contactListener.addCollisionListener(this);
    }

    @Override
    public void addedToEngine(Engine engine) {
        physicsSystem = engine.getSystem(PhysicsSystem.class);
    }

    @Override
    public void startCollision(Entity entityA, Entity entityB) {
        final B2DComponent BCA = ECSEngine.b2dCmpMpr.get(entityA);
        final B2DComponent BCB = ECSEngine.b2dCmpMpr.get(entityB);

        final TransformComponent TCA = ECSEngine.trnCmpMpr.get(entityA);
        final TransformComponent TCB = ECSEngine.trnCmpMpr.get(entityB);

        final BallComponent ballCmpA = ECSEngine.ballCmpMpr.get(entityA);
        final BallComponent ballCmpB = ECSEngine.ballCmpMpr.get(entityB);

        final ExitZoneComponent exitCmpA = ECSEngine.exitCmpMpr.get(entityA);
        final ExitZoneComponent exitCmpB = ECSEngine.exitCmpMpr.get(entityB);

        if((ballCmpA != null && exitCmpB != null) || (ballCmpB != null && exitCmpA != null)) {
            //ball collides with exit so delete ball and init phase for next drop
            if(ballCmpA != null) {
                gameEventSystem.initDropPhase();
            } else if(ballCmpB != null) {
                gameEventSystem.initDropPhase();
            }
        }
        //somewhere here: if ball collides with exit zones (and balls are left to drop): initialize drop phase (gameEventSystem.initDropPhase();)
    }

    @Override
    public void endCollision(Entity entityA, Entity entityB) {
    }
}
