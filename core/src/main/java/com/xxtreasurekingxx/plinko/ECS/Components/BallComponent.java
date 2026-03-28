package com.xxtreasurekingxx.plinko.ECS.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import com.xxtreasurekingxx.plinko.Map.Balls;

public class BallComponent implements Component, Pool.Poolable {
    public Balls type;
    public int damage = 1;

    @Override
    public void reset() {
        type = null;
        damage = 0;
    }
}
