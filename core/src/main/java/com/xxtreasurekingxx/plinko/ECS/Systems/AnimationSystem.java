package com.xxtreasurekingxx.plinko.ECS.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.xxtreasurekingxx.plinko.ECS.Components.AnimationComponent;
import com.xxtreasurekingxx.plinko.ECS.ECSEngine;

public class AnimationSystem extends IteratingSystem {

    public AnimationSystem() {
        super(Family.all(AnimationComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final AnimationComponent animationComponent = ECSEngine.aniCmpMpr.get(entity);
        animationComponent.animationTimer += deltaTime;

    }
}
