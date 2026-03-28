package com.xxtreasurekingxx.plinko.ECS.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectIntMap;
import com.badlogic.gdx.utils.Pool;

public class DataComponent implements Component, Pool.Poolable {

    public int health;

    @Override
    public void reset() {
        health = 0;
    }
}
