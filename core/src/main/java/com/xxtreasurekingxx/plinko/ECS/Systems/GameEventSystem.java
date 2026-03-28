package com.xxtreasurekingxx.plinko.ECS.Systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.xxtreasurekingxx.plinko.Core;
import com.xxtreasurekingxx.plinko.ECS.Components.*;
import com.xxtreasurekingxx.plinko.ECS.ECSEngine;
import com.xxtreasurekingxx.plinko.ECS.EntityCreationFactory;

public class GameEventSystem extends EntitySystem implements InputProcessor {
    private final Core game;
    private final ECSEngine engine;
    private final MapSystem mapSystem;
    private Entity mapEntity;
    private MapComponent mapComponent;
    private Entity dataEntity;
    private DataComponent dataComponent;
    private Entity ballEntity;
    private ImmutableArray<Entity> monsterEntities;
    public boolean ballDropped;

    public GameEventSystem(final Core game, final ECSEngine engine, final MapSystem mapSystem) {
        this.game = game;
        this.engine = engine;
        this.mapSystem = mapSystem;

        game.getInputManager().addListener(this);
    }

    @Override
    public void addedToEngine(Engine engine) {
        monsterEntities = engine.getEntitiesFor(Family.all(MonsterComponent.class).get());
        mapEntity = engine.getEntitiesFor(Family.one(MapComponent.class).get()).first();
        mapComponent = mapEntity.getComponent(MapComponent.class);
        dataEntity = engine.getEntitiesFor(Family.all(DataComponent.class).get()).first();
        dataComponent = dataEntity.getComponent(DataComponent.class);
    }

    public void moveMonsters() {
        for(Entity monster : monsterEntities) {
            TransformComponent transformComponent = monster.getComponent(TransformComponent.class);
            MonsterComponent monsterComponent = monster.getComponent(MonsterComponent.class);
            Entity monstersPegEntity = monsterComponent.peg;
            PegComponent monstersPegCmp = monstersPegEntity.getComponent(PegComponent.class);
            Entity nextPegEntity = monstersPegCmp.nextPegs.random();
            if(nextPegEntity != null) {
                monstersPegCmp.heldMonster = null;
                monsterComponent.peg = nextPegEntity;
                TransformComponent nextPegTrnCmp = nextPegEntity.getComponent(TransformComponent.class);
                transformComponent.transformPosition.set(nextPegTrnCmp.renderPosition);
                transformComponent.needsTransform = true;
            } else {
                //monster is at top (or other niche situations don't have to deal with right now)
                dataComponent.health -= 1;
            }
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.ENTER) {

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
        System.out.println("call drop ball");
        engine.getSystem(BallSystem.class).dropBall();
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
