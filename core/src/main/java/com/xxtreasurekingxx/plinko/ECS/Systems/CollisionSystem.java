package com.xxtreasurekingxx.plinko.ECS.Systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.utils.ObjectIntMap;
import com.xxtreasurekingxx.plinko.Core;
import com.xxtreasurekingxx.plinko.ECS.Components.*;
import com.xxtreasurekingxx.plinko.ECS.ECSEngine;
import com.xxtreasurekingxx.plinko.Map.Item;
import com.xxtreasurekingxx.plinko.Map.Pegs;
import com.xxtreasurekingxx.plinko.World.WorldContactListener;

import static com.xxtreasurekingxx.plinko.Map.Resources.METAL_HUNK;

public class CollisionSystem extends EntitySystem implements WorldContactListener.CollisionListener {
    private final ECSEngine engine;
    private final Core core;
    private final GameEventSystem gameEventSystem;
    private PhysicsSystem physicsSystem;

    private InventoryComponent inventoryComponent;

    public CollisionSystem(final Core core, final ECSEngine engine, final WorldContactListener contactListener, final GameEventSystem gameEventSystem) {
        this.engine = engine;
        this.core = core;
        this.gameEventSystem = gameEventSystem;
        contactListener.addCollisionListener(this);
    }

    @Override
    public void addedToEngine(Engine engine) {
        physicsSystem = engine.getSystem(PhysicsSystem.class);

        Entity inventoryEntity = engine.getEntitiesFor(Family.all(InventoryComponent.class).get()).first();
        inventoryComponent = inventoryEntity.getComponent(InventoryComponent.class);
    }

    @Override
    public void startCollision(Entity entityA, Entity entityB) {
        final B2DComponent BCA = ECSEngine.b2dCmpMpr.get(entityA);
        final B2DComponent BCB = ECSEngine.b2dCmpMpr.get(entityB);

        final TransformComponent TCA = ECSEngine.trnCmpMpr.get(entityA);
        final TransformComponent TCB = ECSEngine.trnCmpMpr.get(entityB);

        final BallComponent ballCmpA = ECSEngine.ballCmpMpr.get(entityA);
        final BallComponent ballCmpB = ECSEngine.ballCmpMpr.get(entityB);

        final ExitZoneComponent exitCmpA = ECSEngine.exitCmpMpr.get(entityA);
        final ExitZoneComponent exitCmpB = ECSEngine.exitCmpMpr.get(entityB);

        final MonsterComponent MCA = ECSEngine.msrCmpMpr.get(entityA);
        final MonsterComponent MCB = ECSEngine.msrCmpMpr.get(entityB);

        final PegComponent PCA = ECSEngine.pegCmpMpr.get(entityA);
        final PegComponent PCB = ECSEngine.pegCmpMpr.get(entityB);

        if((ballCmpA != null && PCB != null) || (ballCmpB != null && PCA != null)) {
            if(PCA != null) {
                if(PCA.type == Pegs.METAL) {
                    inventoryComponent.inventory.getAndIncrement(METAL_HUNK, 0, 1);
                    inventoryComponent.updateInventoryUI = true;
                }
            } else {

            }
        }

        if((ballCmpA != null && MCB != null) || (ballCmpB != null && MCA != null)) {
            if(MCA != null) {
                MCA.health -= ballCmpB.damage;
            } else {
                MCB.health -= ballCmpA.damage;
            }
        }

        if((ballCmpA != null && exitCmpB != null) || (ballCmpB != null && exitCmpA != null)) {
            if(ballCmpA != null) {
                BCA.needsDelete = true;
                gameEventSystem.moveMonsters();
            } else {
                BCB.needsDelete = true;
                gameEventSystem.moveMonsters();
            }
        }
    }

    @Override
    public void endCollision(Entity entityA, Entity entityB) {
    }
}
