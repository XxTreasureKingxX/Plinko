package com.xxtreasurekingxx.plinko.ECS;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.xxtreasurekingxx.plinko.ECS.Components.*;
import com.xxtreasurekingxx.plinko.Map.Balls;
import com.xxtreasurekingxx.plinko.Map.Monsters;
import com.xxtreasurekingxx.plinko.Map.Pegs;

import static com.xxtreasurekingxx.plinko.Core.gridSize;
import static com.xxtreasurekingxx.plinko.Core.toMeters;
import static com.xxtreasurekingxx.plinko.Screens.GameScreen.currentLevel;
import static com.xxtreasurekingxx.plinko.World.AnimationType.*;

public class EntityCreationFactory {
    private final ECSEngine engine;

    public EntityCreationFactory(final ECSEngine engine) {
        this.engine = engine;
    }

    public void createBall(final Vector2 pos, final int width, final int height, final Balls type) {
        Entity ball = engine.createEntity();

        B2DComponent b2DCmp = engine.createComponent(B2DComponent.class);
        b2DCmp.needsBody = true;
        b2DCmp.width = toMeters(width);
        b2DCmp.height = toMeters(height);
        b2DCmp.bodyType = BodyDef.BodyType.DynamicBody;
        CircleShape shape = new CircleShape();
        shape.setRadius(b2DCmp.width/2);
        b2DCmp.bodyShape = shape;
        ball.add(b2DCmp);

        TransformComponent transCmp = engine.createComponent(TransformComponent.class);
        transCmp.renderPosition = pos;
        transCmp.transformPosition = pos;
        transCmp.angle = 0;
        ball.add(transCmp);

        AnimationComponent aniCmp = engine.createComponent(AnimationComponent.class);
        aniCmp.type = type.getAnimation();
        ball.add(aniCmp);

        BallComponent ballComponent = engine.createComponent(BallComponent.class);
        ballComponent.type = type;
        ballComponent.damage = type.getDamage();
        ball.add(ballComponent);

        engine.addEntity(ball);
    }

    public Entity createPeg(final Vector2 pos, final int width, final int height, final Pegs type) {
        Entity peg = engine.createEntity();

        B2DComponent b2DCmp = engine.createComponent(B2DComponent.class);
        b2DCmp.needsBody = true;
        b2DCmp.width = toMeters(width);
        b2DCmp.height = toMeters(height);
        b2DCmp.bodyType = BodyDef.BodyType.StaticBody;
        CircleShape shape = new CircleShape();
        shape.setRadius(b2DCmp.width/2);
        b2DCmp.bodyShape = shape;
        peg.add(b2DCmp);

        TransformComponent transCmp = engine.createComponent(TransformComponent.class);
        transCmp.renderPosition = pos;
        transCmp.transformPosition = pos;
        transCmp.angle = 0;
        peg.add(transCmp);

        AnimationComponent aniCmp = engine.createComponent(AnimationComponent.class);
        aniCmp.type = type.getAniType();
        peg.add(aniCmp);

        PegComponent pegCmp = engine.createComponent(PegComponent.class);
        pegCmp.type = type;
        pegCmp.heldMonster = null;
        pegCmp.nextPegs = new Array<>();
        peg.add(pegCmp);

        engine.addEntity(peg);
        return peg;
    }

    public void createMonster(final Vector2 pos, final int width, final int height, final Monsters type, final Entity attachedPeg) {
        Entity monster = engine.createEntity();

        B2DComponent b2DCmp = engine.createComponent(B2DComponent.class);
        b2DCmp.needsBody = true;
        b2DCmp.width = toMeters(width);
        b2DCmp.height = toMeters(height);
        b2DCmp.bodyType = BodyDef.BodyType.StaticBody;
        CircleShape shape = new CircleShape();
        shape.setRadius(b2DCmp.width/2);
        b2DCmp.bodyShape = shape;
        monster.add(b2DCmp);

        TransformComponent transCmp = engine.createComponent(TransformComponent.class);
        transCmp.renderPosition = pos;
        transCmp.transformPosition = pos;
        transCmp.angle = 0;
        monster.add(transCmp);

        AnimationComponent aniCmp = engine.createComponent(AnimationComponent.class);
        aniCmp.type = MONSTER1;
        monster.add(aniCmp);

        MonsterComponent monsterCmp = engine.createComponent(MonsterComponent.class);
        monsterCmp.type = type;
        monsterCmp.peg = attachedPeg;
        monsterCmp.health = type.getHealth();
        monster.add(monsterCmp);

        engine.addEntity(monster);
    }

    public Entity createDropZone(final Vector2 pos, final Rectangle rect) {
        Entity drop = engine.createEntity();

//        Vector2 pos = new Vector2();
//        pos.set(rect.getCenter(pos));
        pos.x += rect.width/2;
        pos.y += rect.height/2;

        B2DComponent b2DCmp = engine.createComponent(B2DComponent.class);
        b2DCmp.needsBody = true;
        b2DCmp.isSensor = true;
        b2DCmp.width = toMeters(rect.getWidth());
        b2DCmp.height = toMeters(rect.getHeight());
        b2DCmp.bodyType = BodyDef.BodyType.StaticBody;
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(b2DCmp.width/2, b2DCmp.height/2);
        b2DCmp.bodyShape = shape;
        drop.add(b2DCmp);

        TransformComponent trnCmp = engine.createComponent(TransformComponent.class);
        trnCmp.renderPosition = pos;
        trnCmp.transformPosition = pos;
        drop.add(trnCmp);

        engine.addEntity(drop);

        return drop;
    }

    public void createExitZone(final Vector2 pos, final float width, final float height) {
        Entity exit = engine.createEntity();

        pos.y -= 50;

        B2DComponent b2DCmp = engine.createComponent(B2DComponent.class);
        b2DCmp.needsBody = true;
        b2DCmp.width = toMeters(width);
        b2DCmp.height = toMeters(height);
        b2DCmp.bodyType = BodyDef.BodyType.StaticBody;
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(b2DCmp.width/2, b2DCmp.height/2);
        b2DCmp.bodyShape = shape;
        exit.add(b2DCmp);

        TransformComponent trnCmp = engine.createComponent(TransformComponent.class);
        trnCmp.renderPosition = pos;
        trnCmp.transformPosition = pos;
        exit.add(trnCmp);

        ExitZoneComponent exitCmp = engine.createComponent(ExitZoneComponent.class);
        exit.add(exitCmp);

        engine.addEntity(exit);
    }

    public void createCollisionBorder(final Vector2 pos, final float[] vertices) {
        Entity collision = engine.createEntity();

        for(int i = 0; i < vertices.length; i++) {
            vertices[i] = toMeters(vertices[i]);
        }

        B2DComponent b2DCmp = engine.createComponent(B2DComponent.class);
        b2DCmp.needsBody = true;
        b2DCmp.bodyType = BodyDef.BodyType.StaticBody;
        PolygonShape shape = new PolygonShape();
        shape.set(vertices);
        b2DCmp.bodyShape = shape;
        collision.add(b2DCmp);

        TransformComponent trnCmp = engine.createComponent(TransformComponent.class);
        trnCmp.renderPosition = pos;
        trnCmp.transformPosition = pos;
        collision.add(trnCmp);

        engine.addEntity(collision);
    }
}
