package com.xxtreasurekingxx.plinko.ECS.Systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;import com.badlogic.gdx.physics.box2d.Fixture;import com.badlogic.gdx.utils.Array;
import com.xxtreasurekingxx.plinko.Core;
import com.xxtreasurekingxx.plinko.ECS.Components.*;
import com.xxtreasurekingxx.plinko.ECS.ECSEngine;

public class GameEventSystem extends EntitySystem implements InputProcessor {
    private final Core game;
    private final ECSEngine engine;
    private final MapSystem mapSystem;
    private Entity mapEntity;
    private Entity ballEntity;
    private ImmutableArray<Entity> monsterEntities;
    public Array<Vector2> dropPositions;
    public int dropPosIncrement;
    public Vector2 currentDropPos;

    public GameEventSystem(final Core game, final ECSEngine engine, final MapSystem mapSystem) {
        this.game = game;
        this.engine = engine;
        this.mapSystem = mapSystem;

        dropPositions = new Array<>();
        dropPosIncrement = 0;
        currentDropPos = new Vector2();

        game.getInputManager().addListener(this);
    }

    @Override
    public void addedToEngine(Engine engine) {
        monsterEntities = engine.getEntitiesFor(Family.all(MonsterComponent.class).get());
        mapEntity = engine.getEntitiesFor(Family.one(MapComponent.class).get()).first();
        ballEntity = engine.getEntitiesFor(Family.one(BallComponent.class).get()).first();

        MapComponent mapComponent = mapEntity.getComponent(MapComponent.class);
        for(Vector2 pos : mapComponent.dropPoints.keySet()) {
            dropPositions.add(pos);
        }
        dropPositions.sort((a, b) -> Float.compare(a.x, b.x));
    }

    public void dropBall() {
        hideDropPositions();
    }

    public void initDropPhase() {
        //set up drop zones again
        showDropPositions();

        //move next ball into position from the ball queue (idk what this will be yet) / make new ball (temporary) "temporary" I will need to transform pre created ball positions to the drop zones
        teleportBallToDropPos();

        //move monster position //TODO givwe transform component the needstranfm boolean
        for(Entity monster : monsterEntities) {
            B2DComponent b2DComponent = monster.getComponent(B2DComponent.class);
            TransformComponent transformComponent = monster.getComponent(TransformComponent.class);
            MonsterComponent monsterComponent = monster.getComponent(MonsterComponent.class);
            Entity monstersPegEntity = monsterComponent.peg;
            PegComponent monstersPegCmp = monstersPegEntity.getComponent(PegComponent.class);
            monstersPegCmp.heldMonster = null;
            Entity nextPegEntity = monstersPegCmp.nextPegs.random();
            monsterComponent.peg = nextPegEntity;
            TransformComponent nextPegTrnCmp = nextPegEntity.getComponent(TransformComponent.class);
            transformComponent.transformPosition.set(nextPegTrnCmp.renderPosition);
            b2DComponent.needsTransform = true;
        }
    }

    public void moveBallLeft() {
        if(dropPositions.size <= 0) {
            return;
        }
        B2DComponent b2DComponent = ballEntity.getComponent(B2DComponent.class);
        TransformComponent transformComponent = ballEntity.getComponent(TransformComponent.class);

        dropPosIncrement--;
        if(dropPosIncrement < 0) {
            dropPosIncrement += dropPositions.size;
        }

        transformComponent.transformPosition.set(dropPositions.get(dropPosIncrement));
        b2DComponent.needsTransform = true;
    }

    public void moveBallRight() {
        if(dropPositions.size <= 0) {
            return;
        }

        dropPosIncrement++;
        if(dropPosIncrement >= dropPositions.size) {
            dropPosIncrement -= dropPositions.size;
        }

        teleportBallToDropPos();
    }

    public void teleportBallToDropPos() {
        B2DComponent b2DComponent = ballEntity.getComponent(B2DComponent.class);
        TransformComponent transformComponent = ballEntity.getComponent(TransformComponent.class);
        transformComponent.transformPosition.set(dropPositions.get(dropPosIncrement));
        b2DComponent.needsTransform = true;
    }

    public void showDropPositions() {
        MapComponent mapComponent = mapEntity.getComponent(MapComponent.class);
        for(Entity entity : mapComponent.dropPoints.values()) {
            B2DComponent b2DComponent = entity.getComponent(B2DComponent.class);
            b2DComponent.shouldCollide = true;
            b2DComponent.updateCollideStatus = true;
        }
    }

    public void hideDropPositions() {
        MapComponent mapComponent = mapEntity.getComponent(MapComponent.class);
        for(Entity entity : mapComponent.dropPoints.values()) {
            B2DComponent b2DComponent = entity.getComponent(B2DComponent.class);
            b2DComponent.shouldCollide = false;
            b2DComponent.updateCollideStatus = true;
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.ENTER) {
            dropBall();
        }
        if(keycode == Input.Keys.LEFT) {
            moveBallLeft();
        }
        if(keycode == Input.Keys.RIGHT) {
            moveBallRight();
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
