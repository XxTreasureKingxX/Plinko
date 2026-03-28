package com.xxtreasurekingxx.plinko.ECS.Systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PointMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.xxtreasurekingxx.plinko.Core;
import com.xxtreasurekingxx.plinko.ECS.Components.*;
import com.xxtreasurekingxx.plinko.ECS.ECSEngine;
import com.xxtreasurekingxx.plinko.Map.Pegs;
import com.xxtreasurekingxx.plinko.World.AnimationType;

import java.util.EnumMap;

import static com.xxtreasurekingxx.plinko.Core.*;

public class RenderSystem extends EntitySystem {
    private final Core core;
    private final SpriteBatch batch;
    private final AssetManager assetManager;
    private final FitViewport viewport;
    private final World world;
    private final ECSEngine engine;
    private Matrix4 debugMatrix;

    private final Box2DDebugRenderer debugRenderer;
    private ImmutableArray<Entity> animatedEntities;
    private EnumMap<AnimationType, Animation<Sprite>> animations;
    private Entity mapEntity;
    private MapComponent mapComponent;

    public RenderSystem(final Core core, final SpriteBatch batch, final World world, final FitViewport viewport, final ECSEngine engine) {
        this.core = core;
        this.batch = batch;
        this.world = world;
        this.viewport = viewport;
        this.engine = engine;

        assetManager = core.getAssetManager();
        debugRenderer = new Box2DDebugRenderer();
        animations = new EnumMap<>(AnimationType.class);
    }

    @Override
    public void addedToEngine(Engine engine) {
        animatedEntities = getEngine().getEntitiesFor(Family.all(AnimationComponent.class, B2DComponent.class).get());
        mapEntity = engine.getEntitiesFor(Family.one(MapComponent.class).get()).first();
        mapComponent = mapEntity.getComponent(MapComponent.class);
    }

    public void render(float delta) {

        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        batch.draw(mapComponent.backgroundImage, 0, 0);

        for(Entity entity : animatedEntities) {
            final AnimationComponent animationComponent = ECSEngine.aniCmpMpr.get(entity);
            final TransformComponent transformComponent = ECSEngine.trnCmpMpr.get(entity);

            if(animationComponent.type != null) {
                final Animation<Sprite> animation = getAnimation(animationComponent.type);
                animation.setPlayMode(Animation.PlayMode.LOOP);
                final Sprite frame = animation.getKeyFrame(animationComponent.animationTimer);

                float width = gridSize;
                float height = gridSize;
                frame.setBounds(transformComponent.renderPosition.x - width/2, transformComponent.renderPosition.y - height/2, width, height);
                frame.setOriginCenter();
                frame.setRotation((float)Math.toDegrees(transformComponent.angle));
                frame.draw(batch);
            }
        }
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.end();

        debugMatrix = viewport.getCamera().combined.scl(PPM);
        //debugRenderer.render(world, debugMatrix);
    }

    public Animation<Sprite> getAnimation(AnimationType type) {
        Animation<Sprite> animation = animations.get(type);
        if(animation == null) {
            System.out.println("Creating animation for " + type);
            final TextureAtlas.AtlasRegion region = assetManager.get(type.getAtlas(), TextureAtlas.class).findRegion(type.getRegion());
            int gridSize = Core.gridSize;
            final TextureRegion[][] textureRegions = region.split(gridSize, gridSize);
            animation = new Animation<>(type.getFrameRate(), getAnimationSequence(textureRegions[type.getRow()]));
            animations.put(type, animation);
        }
        return animation;
    }

    public Sprite[] getAnimationSequence(final TextureRegion[] regions) {
        final Sprite[] sequence = new Sprite[regions.length];
        int i = 0;
        for(final TextureRegion region : regions) {
            final Sprite sprite = new Sprite(region);
            sequence[i++] = sprite;
        }
        return sequence;
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        animatedEntities = null;
        animations.clear();
        debugRenderer.dispose();
    }
}
