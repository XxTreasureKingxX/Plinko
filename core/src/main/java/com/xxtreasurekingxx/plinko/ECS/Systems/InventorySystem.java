package com.xxtreasurekingxx.plinko.ECS.Systems;


import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.utils.ObjectIntMap;
import com.xxtreasurekingxx.plinko.ECS.Components.InventoryComponent;
import com.xxtreasurekingxx.plinko.Map.Balls;
import com.xxtreasurekingxx.plinko.Map.Item;

public class InventorySystem extends EntitySystem {
    //selected item
    //inventory
    //balldisplaylocked

    @Override
    public void addedToEngine(Engine engine) {
        Entity inventoryEntity = engine.createEntity();
        InventoryComponent inventoryComponent = engine.createComponent(InventoryComponent.class);

        inventoryComponent.inventory = new ObjectIntMap<>();
        inventoryComponent.inventory.getAndIncrement(Balls.BASE, 0, 5);
        inventoryComponent.inventory.getAndIncrement(Balls.SOUL, 0, 1);
        inventoryComponent.updateInventoryUI = true;

        inventoryEntity.add(inventoryComponent);
        engine.addEntity(inventoryEntity);
    }
}
