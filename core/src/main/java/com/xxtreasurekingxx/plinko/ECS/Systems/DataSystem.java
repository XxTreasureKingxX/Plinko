package com.xxtreasurekingxx.plinko.ECS.Systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.xxtreasurekingxx.plinko.Core;
import com.xxtreasurekingxx.plinko.ECS.Components.DataComponent;
import com.xxtreasurekingxx.plinko.GameData;

public class DataSystem extends EntitySystem {
    private final Core game;
    private final GameData data;

    //TODO split into download and upload systems in the future?
    public DataSystem(final Core game) {
        this.game = game;
        data = game.getData();
    }

    @Override
    public void addedToEngine(Engine engine) {
        Entity dataEntity = engine.createEntity();
        DataComponent dataComponent = engine.createComponent(DataComponent.class);

        dataComponent.health = data.getHealth();


        dataEntity.add(dataComponent);
        engine.addEntity(dataEntity);
    }

}
