package com.xxtreasurekingxx.plinko.ECS.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import com.xxtreasurekingxx.plinko.World.AnimationType;

public class AnimationComponent implements Pool.Poolable, Component {
    public AnimationType type;
    public float animationTimer;

    @Override
    public void reset() {
        type = null;
        animationTimer = 0;
    }
}
