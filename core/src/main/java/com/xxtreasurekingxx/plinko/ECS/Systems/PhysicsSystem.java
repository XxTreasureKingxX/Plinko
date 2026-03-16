package com.xxtreasurekingxx.plinko.ECS.Systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.xxtreasurekingxx.plinko.ECS.Components.B2DComponent;
import com.xxtreasurekingxx.plinko.ECS.Components.TransformComponent;
import com.xxtreasurekingxx.plinko.World.WorldContactListener;

import static com.xxtreasurekingxx.plinko.Core.FPS;
import static com.xxtreasurekingxx.plinko.Core.toPixels;
import static com.xxtreasurekingxx.plinko.ECS.ECSEngine.b2dCmpMpr;
import static com.xxtreasurekingxx.plinko.ECS.ECSEngine.trnCmpMpr;

public class PhysicsSystem extends EntitySystem {

    private final World world;
    private final WorldContactListener contactListener;
    public final Array<Runnable> postPhysicsCalls;

    public PhysicsSystem() {
        world = new World(new Vector2(0, -9.8f), true);
        world.setContactListener(contactListener = new WorldContactListener());
        postPhysicsCalls = new Array<>();
    }

    @Override
    public void addedToEngine(Engine engine) {
        //wait
    }

    @Override
    public void update(float delta) {
        world.step(delta, 6, 2);

        for(Runnable runnable : postPhysicsCalls) {
            runnable.run();
        }
        postPhysicsCalls.clear();
    }

    public World getWorld() {
        return world;
    }

    public WorldContactListener getContactListener() {
        return contactListener;
    }

    @Override
    public void removedFromEngine(Engine engine) {
        world.dispose();
    }
}
