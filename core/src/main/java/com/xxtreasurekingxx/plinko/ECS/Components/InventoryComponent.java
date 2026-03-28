package com.xxtreasurekingxx.plinko.ECS.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.ObjectIntMap;
import com.badlogic.gdx.utils.Pool;
import com.xxtreasurekingxx.plinko.Map.Item;

public class InventoryComponent implements Component, Pool.Poolable {

    public ObjectIntMap<Item> inventory;
    public Item selectedItem;
    public boolean updateInventoryUI;

    @Override
    public void reset() {
        inventory = null;
        selectedItem = null;
        updateInventoryUI = false;
    }
}
