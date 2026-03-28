package com.xxtreasurekingxx.plinko.ECS.Systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.xxtreasurekingxx.plinko.ECS.Components.*;
import com.xxtreasurekingxx.plinko.ECS.ECSEngine;
import com.xxtreasurekingxx.plinko.ECS.EntityCreationFactory;
import com.xxtreasurekingxx.plinko.Map.Balls;

public class BallSystem extends EntitySystem {
    private final ECSEngine engine;
    private final EntityCreationFactory factory;
    private final GameEventSystem gameEventSystem;
    private final FitViewport viewport;
    private MapComponent mapComponent;
    private InventoryComponent inventoryComponent;
    private DataComponent dataComponent;
    private Entity ballEntity;

    public static Balls activeBall;

    public BallSystem(final ECSEngine engine, final GameEventSystem gameEventSystem, final FitViewport viewport) {
        this.engine = engine;
        factory = engine.getFactory();
        this.gameEventSystem = gameEventSystem;
        this.viewport = viewport;
    }

    @Override
    public void addedToEngine(Engine engine) {
        mapComponent = engine.getEntitiesFor(Family.one(MapComponent.class).get()).first().getComponent(MapComponent.class);
        inventoryComponent = engine.getEntitiesFor(Family.all(InventoryComponent.class).get()).first().getComponent(InventoryComponent.class);
        dataComponent = engine.getEntitiesFor(Family.all(DataComponent.class).get()).first().getComponent(DataComponent.class);
    }

    @Override
    public void update(float deltaTime) {

    }

    public void dropBall() {
        if(inventoryComponent.selectedItem == null || !(inventoryComponent.inventory.get(inventoryComponent.selectedItem, 0) > 0)) {
            return;
        }

        Vector2 ballPos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        ballPos.set(viewport.unproject(ballPos));

        float minX = mapComponent.dropArea.x;
        float maxX = mapComponent.dropArea.x + mapComponent.dropArea.width;
        float minY = mapComponent.dropArea.y;
        float maxY = mapComponent.dropArea.y + mapComponent.dropArea.height;
        ballPos.x = Math.max(minX, ballPos.x);
        ballPos.x = Math.min(maxX, ballPos.x);
        ballPos.y = Math.max(minY, ballPos.y);
        ballPos.y = Math.min(maxY, ballPos.y);

        factory.createBall(ballPos, 12, 12, (Balls) inventoryComponent.selectedItem);

        if(inventoryComponent.selectedItem == Balls.SOUL) {
            dataComponent.health--;
            return;
        }

        inventoryComponent.inventory.getAndIncrement(inventoryComponent.selectedItem, 0, -1);
        inventoryComponent.updateInventoryUI = true;
    }
}
