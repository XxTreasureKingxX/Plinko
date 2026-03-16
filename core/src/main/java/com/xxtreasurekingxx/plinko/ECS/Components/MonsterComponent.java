package com.xxtreasurekingxx.plinko.ECS.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;
import com.xxtreasurekingxx.plinko.Map.Monsters;

public class MonsterComponent implements Component, Pool.Poolable {
    public Entity peg;
    public Monsters type;

    @Override
    public void reset() {
        peg = null;
        type = null;
    }
}
