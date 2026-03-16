package com.xxtreasurekingxx.plinko.ECS.Systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PointMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.xxtreasurekingxx.plinko.ECS.Components.MapComponent;
import com.xxtreasurekingxx.plinko.ECS.Components.PegComponent;
import com.xxtreasurekingxx.plinko.ECS.Components.TransformComponent;
import com.xxtreasurekingxx.plinko.ECS.ECSEngine;
import com.xxtreasurekingxx.plinko.ECS.EntityCreationFactory;import com.xxtreasurekingxx.plinko.Map.Balls;
import com.xxtreasurekingxx.plinko.Map.Monsters;
import com.xxtreasurekingxx.plinko.Map.Pegs;

import java.util.HashMap;

import static com.xxtreasurekingxx.plinko.Screens.GameScreen.currentLevel;

public class MapSystem extends EntitySystem {
    private final ECSEngine engine;
    private final AssetManager assetManager;
    private EntityCreationFactory entityCreationFactory;
    private ImmutableArray<Entity> pegEntities;
    private MapComponent mapCmp;
    private MapLayers mapLayers;
    private MapState currentMapState;

    private enum MapState {
        GENERATE_ENTITIES,
        CHECK_FOR_ENTITIES,
        INITIALIZE_ENTITIES,
        READY
    }

    public MapSystem(final ECSEngine engine, final AssetManager assetManager) {
        this.engine = engine;
        this.assetManager = assetManager;

        currentMapState = MapState.GENERATE_ENTITIES;
    }

    @Override
    public void addedToEngine(Engine baseEngine) {
        entityCreationFactory = engine.getFactory();
        pegEntities = engine.getEntitiesFor(Family.all(PegComponent.class, TransformComponent.class).get());
        genMap();
    }

    @Override
    public void update(float deltaTime) {
        if(currentMapState != MapState.READY) {
            switch (currentMapState) {
                case GENERATE_ENTITIES:
                    currentMapState = MapState.CHECK_FOR_ENTITIES;
                    break;
                case CHECK_FOR_ENTITIES:
                    if (pegEntities.size() > 0) {
                        currentMapState = MapState.INITIALIZE_ENTITIES;
                    }
                    break;
                case INITIALIZE_ENTITIES:
                    initNextPegs();
                    currentMapState = MapState.READY;
                    break;
            }
        }
    }

    private void genMap() {
        System.out.println("Gen map");
        Entity mapEntity = engine.createEntity();
        mapCmp = engine.createComponent(MapComponent.class);

        mapCmp.map = assetManager.get(currentLevel.getMapPath(), TiledMap.class);
        mapCmp.dropPoints = new HashMap<>();
        mapLayers = mapCmp.map.getLayers();

        genPegs();
        addDropZones();
        addExitZones();
        genBall();

        //add monster spawning and moving after each ball

        mapEntity.add(mapCmp);
        engine.addEntity(mapEntity);
    }

    public void addDropZones() {
        //expensive?
        MapLayer dropPointLayer = mapLayers.get("drops");
        MapObjects dropPointObjects = dropPointLayer.getObjects();
        for(MapObject dropPoint : dropPointObjects) {
            if(dropPoint instanceof PointMapObject) {
                mapCmp.dropPoints.put(
                    new Vector2(((PointMapObject) dropPoint).getPoint().x + 1, ((PointMapObject) dropPoint).getPoint().y),
                    entityCreationFactory.createDropZone(new Vector2(((PointMapObject) dropPoint).getPoint().x, ((PointMapObject) dropPoint).getPoint().y - 8))
                );
            }
        }
    }

    private void addExitZones() {
        MapLayer exitZoneLayer = mapLayers.get("exits");
        MapObjects exitZoneObjects = exitZoneLayer.getObjects();
        for(MapObject exitZone : exitZoneObjects) {
            if(exitZone instanceof RectangleMapObject) {
                RectangleMapObject exitObject = (RectangleMapObject) exitZone;
                Vector2 pos = new Vector2();
                entityCreationFactory.createExitZone(exitObject.getRectangle().getCenter(pos), exitObject.getRectangle().width, exitObject.getRectangle().height);
            }
        }
    }

    public void genBall() {
        final Array<Vector2> dropPositions = new Array<>();
        for(Vector2 pos : mapCmp.dropPoints.keySet()) {
            dropPositions.add(pos);
        }
        dropPositions.sort((a, b) -> Float.compare(a.x, b.x));
        Vector2 ballPos = new Vector2();
        ballPos.set(dropPositions.first());
        //ballPos.x += 96; offset ball spawn pos

        entityCreationFactory.createBall(ballPos, 12, 12, Balls.BASE);
    }

    private void genPegs() {
        MapLayer pegObjectLayer = mapLayers.get("pegs");
        MapObjects pegObjects = pegObjectLayer.getObjects();
        System.out.println(pegObjects.getCount() + " amount of pegs");
        for(int i = 0; i < pegObjects.getCount(); i++) {
            for(Pegs peg : Pegs.values()) {
                if(pegObjects.get(i).getName().equals(peg.getName())) {
                    //peg type exists/creation
                    PointMapObject pegObject = (PointMapObject) pegObjects.get(i); //pegObject.getPoint().y = GAMEH - pegObject.getPoint().y + gridSize/2f; //360 - 72 =
                    Entity pegEntity = entityCreationFactory.createPeg(pegObject.getPoint(), peg.getSize(), peg.getSize(), peg);
                    //gen monsters
                    if(pegObject.getProperties().containsKey("monster")) {
                        Monsters monster = null;
                        for(Monsters type : Monsters.values()) {
                            if(type.getName().equals(pegObject.getProperties().get("monster"))) {
                                monster = type;
                            }
                        }

                        if(monster != null) {
                            System.out.println("creating monster");
                            Vector2 monsterPos = new Vector2();
                            monsterPos.set(pegObject.getPoint());
                            entityCreationFactory.createMonster(monsterPos, monster.getWidth(), monster.getHeight(), monster, pegEntity);
                        }
                    }
                }
            }
        }
    }

    public void initNextPegs() {
        System.out.println(pegEntities.size());
        for(int i = 0; i < pegEntities.size(); i++) {
            Entity peg = pegEntities.get(i);
            PegComponent pegCmp = ECSEngine.pegCmpMpr.get(peg);
            Array<Entity> candidates = new Array<>();
            Vector2 pegPos = ECSEngine.trnCmpMpr.get(peg).renderPosition;

            for(int j = 0; j < pegEntities.size(); j++) {
                Entity candidate = pegEntities.get(j);
                Vector2 candidatePos = new Vector2();
                candidatePos.set(ECSEngine.trnCmpMpr.get(candidate).renderPosition);

                float dy = candidatePos.y - pegPos.y;

                if(dy <= 0 || dy >= 32) {
                    continue;
                }
                System.out.println(dy + "dy");

                candidates.add(candidate);
            }

            //resort to for loop

            //next pos are inverted?

            candidates.sort((a, b) -> Float.compare(
                (ECSEngine.trnCmpMpr.get(a).renderPosition.y),
                (ECSEngine.trnCmpMpr.get(b).renderPosition.y)
            ));

            candidates.sort((a, b) -> Float.compare(
                Math.abs(ECSEngine.trnCmpMpr.get(a).renderPosition.x - pegPos.x),
                Math.abs(ECSEngine.trnCmpMpr.get(b).renderPosition.x - pegPos.x)
            ));

            for(int q = 0; q <= candidates.size-1; q++) {
                if((ECSEngine.trnCmpMpr.get(candidates.get(q)).renderPosition.x == pegPos.x)) {
                    System.out.println(ECSEngine.trnCmpMpr.get(candidates.get(q)).renderPosition.x + " test " + pegPos.x);
                    candidates.removeIndex(q);
                    System.out.println(candidates.size + " size");
                }
            }

            System.out.println(candidates.size + " can size");
            if(candidates.size > 0) {
                System.out.println(ECSEngine.trnCmpMpr.get(candidates.first()).renderPosition + " next pos1");
                System.out.println(ECSEngine.trnCmpMpr.get(candidates.get(1)).renderPosition + " next pos2");
                System.out.println(pegPos);
            }

            for(int k = 0; k < Math.min(2, candidates.size); k++) {
                System.out.println();
                pegCmp.nextPegs.add(candidates.get(k));
            }
        }
    }
}
