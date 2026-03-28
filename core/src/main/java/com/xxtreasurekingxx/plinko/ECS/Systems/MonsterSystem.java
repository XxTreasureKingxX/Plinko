package com.xxtreasurekingxx.plinko.ECS.Systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.utils.ObjectIntMap;
import com.xxtreasurekingxx.plinko.ECS.Components.B2DComponent;
import com.xxtreasurekingxx.plinko.ECS.Components.InventoryComponent;
import com.xxtreasurekingxx.plinko.ECS.Components.MonsterComponent;
import com.xxtreasurekingxx.plinko.ECS.ECSEngine;
import com.xxtreasurekingxx.plinko.Map.Item;

public class MonsterSystem extends IteratingSystem {
    private InventoryComponent inventoryComponent;

    public MonsterSystem() {
        super(Family.all(MonsterComponent.class).get());
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);

        Entity inventoryEntity = engine.getEntitiesFor(Family.all(InventoryComponent.class).get()).first();
        inventoryComponent = inventoryEntity.getComponent(InventoryComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        MonsterComponent monsterComponent = ECSEngine.msrCmpMpr.get(entity);
        B2DComponent b2DComponent = ECSEngine.b2dCmpMpr.get(entity);

        if(monsterComponent.health <= 0) {
            inventoryComponent.inventory.getAndIncrement(monsterComponent.type.getDrop(), 0, 1);
            inventoryComponent.updateInventoryUI = true;
            b2DComponent.needsDelete = true;
        }
    }
}
