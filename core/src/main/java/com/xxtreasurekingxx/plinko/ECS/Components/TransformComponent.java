package com.xxtreasurekingxx.plinko.ECS.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class TransformComponent implements Component, Pool.Poolable {

    public Vector2 renderPosition;
    public Vector2 transformPosition;
    public float angle;

    @Override
    public void reset() {
        renderPosition = new Vector2(0, 0);
        transformPosition = new Vector2(0, 0);
        angle = 0;
    }
}
