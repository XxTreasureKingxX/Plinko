package com.xxtreasurekingxx.plinko.ECS;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.xxtreasurekingxx.plinko.Core;
import com.xxtreasurekingxx.plinko.ECS.Components.*;
import com.xxtreasurekingxx.plinko.ECS.Systems.*;

public class ECSEngine extends PooledEngine {
    private final Core core;
    private final SpriteBatch batch;
    private final AssetManager assetManager;
    private final FitViewport viewport;
    private final EntityCreationFactory entityCreationFactory;

    private final MapSystem mapSystem;
    private final GameEventSystem gameEventSystem;
    private final PhysicsSystem physicsSystem;
    private final BodyHandleSystem bodyHandleSystem;
    private final CollisionSystem collisionSystem;
    private final BodySyncSystem bodySyncSystem;
    private final RenderSystem renderSystem;

    public static final ComponentMapper<B2DComponent> b2dCmpMpr = ComponentMapper.getFor(B2DComponent.class);
    public static final ComponentMapper<AnimationComponent> aniCmpMpr = ComponentMapper.getFor(AnimationComponent.class);
    public static final ComponentMapper<TransformComponent> trnCmpMpr = ComponentMapper.getFor(TransformComponent.class);
    public static final ComponentMapper<PegComponent> pegCmpMpr = ComponentMapper.getFor(PegComponent.class);
    public static final ComponentMapper<BallComponent> ballCmpMpr = ComponentMapper.getFor(BallComponent.class);
    public static final ComponentMapper<ExitZoneComponent> exitCmpMpr = ComponentMapper.getFor(ExitZoneComponent.class);

    //TODO on game hide clear any systems that are level based for the next level selection/creation (this is rather than creating a whole new ecs each time a level is started)

    public ECSEngine(final Core core, final FitViewport viewport) {
        super();
        this.core = core;
        this.viewport = viewport;
        entityCreationFactory = new EntityCreationFactory(this);
        batch = core.getBatch();
        assetManager = core.getAssetManager();

        //Systems
        mapSystem = new MapSystem(this, assetManager);
        gameEventSystem = new GameEventSystem(core, this, mapSystem);
        physicsSystem = new PhysicsSystem();
        collisionSystem = new CollisionSystem(core, this, physicsSystem.getContactListener(), gameEventSystem); // after physics system just for access
//        gameUISystem = new GameUISystem(core, viewport, this);
        bodyHandleSystem = new BodyHandleSystem(this, physicsSystem.getWorld());
        bodySyncSystem = new BodySyncSystem();
        renderSystem = new RenderSystem(core, batch, physicsSystem.getWorld(), viewport, this);

        this.addSystem(mapSystem);
        this.addSystem(gameEventSystem);
        //pegs created
        this.addSystem(new AnimationSystem());
        this.addSystem(collisionSystem); //collisions happen DURING physics
        this.addSystem(physicsSystem);
        this.addSystem(bodyHandleSystem);
        this.addSystem(bodySyncSystem);
        this.addSystem(renderSystem);
    }

    public void render(float delta) {
        renderSystem.render(delta);

        //batch.setProjectionMatrix(gameUI.getStage().getCamera().combined);
        //gameUI.getStage().draw();
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    public void hide() {
        //core.getInputMultiplexer().removeProcessor(gameUISystem.getStage());
    }

    public EntityCreationFactory getFactory() {
        return entityCreationFactory;
    }
}
