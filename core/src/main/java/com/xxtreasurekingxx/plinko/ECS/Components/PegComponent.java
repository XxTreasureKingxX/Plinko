package com.xxtreasurekingxx.plinko.ECS.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.xxtreasurekingxx.plinko.Map.Pegs;

public class PegComponent implements Pool.Poolable, Component {
    public Pegs type;
    public Entity heldMonster;
    public Array<Entity> nextPegs;

    @Override
    public void reset() {
        type = null;
        heldMonster = null;
        nextPegs = null;
    }
}
