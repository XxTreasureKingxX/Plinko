package com.xxtreasurekingxx.plinko.ECS.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.xxtreasurekingxx.plinko.ECS.Components.B2DComponent;
import com.xxtreasurekingxx.plinko.ECS.Components.TransformComponent;

import static com.xxtreasurekingxx.plinko.Core.toPixels;
import static com.xxtreasurekingxx.plinko.ECS.ECSEngine.b2dCmpMpr;
import static com.xxtreasurekingxx.plinko.ECS.ECSEngine.trnCmpMpr;

public class BodySyncSystem extends IteratingSystem {

    public BodySyncSystem() {
        super(Family.all(TransformComponent.class, B2DComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent trnCmp = trnCmpMpr.get(entity);
        B2DComponent b2dCmp = b2dCmpMpr.get(entity);

        trnCmp.renderPosition.set(toPixels(b2dCmp.body.getPosition()));
        trnCmp.angle = b2dCmp.body.getAngle();
    }
}
