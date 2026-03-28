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
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapImageLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.xxtreasurekingxx.plinko.ECS.Components.MapComponent;
import com.xxtreasurekingxx.plinko.ECS.Components.PegComponent;
import com.xxtreasurekingxx.plinko.ECS.Components.TransformComponent;
import com.xxtreasurekingxx.plinko.ECS.ECSEngine;
import com.xxtreasurekingxx.plinko.ECS.EntityCreationFactory;import com.xxtreasurekingxx.plinko.Map.Balls;
import com.xxtreasurekingxx.plinko.Map.Monsters;
import com.xxtreasurekingxx.plinko.Map.Pegs;

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
        Entity mapEntity = engine.createEntity();
        mapCmp = engine.createComponent(MapComponent.class);

        mapCmp.map = assetManager.get(currentLevel.getMapPath(), TiledMap.class);
        mapLayers = mapCmp.map.getLayers();

        mapCmp.dropArea = new Rectangle();

        getBackgroundImage();

        genPegs();
        addDropArea();
        addExitZones();
        addCollisionAreas();

        //add monster spawning and moving after each ball

        mapEntity.add(mapCmp);
        engine.addEntity(mapEntity);
    }

    public void getBackgroundImage() {
        TiledMapImageLayer imageLayer = (TiledMapImageLayer) mapLayers.get("backgroundImage");
        mapCmp.backgroundImage = imageLayer.getTextureRegion();
    }

    public void addDropArea() {
        MapLayer dropPointLayer = mapLayers.get("drops");
        MapObject dropArea = dropPointLayer.getObjects().get(0);
        if(dropArea instanceof RectangleMapObject) {
            mapCmp.dropArea = ((RectangleMapObject) dropArea).getRectangle();
            entityCreationFactory.createDropZone(new Vector2(((RectangleMapObject) dropArea).getRectangle().x, ((RectangleMapObject) dropArea).getRectangle().y), ((RectangleMapObject) dropArea).getRectangle());
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

    private void addCollisionAreas() {
        MapLayer collisionLayer = mapLayers.get("collisions");
        MapObjects collisions = collisionLayer.getObjects();

        for(MapObject object : collisions) {
            if(object instanceof PolygonMapObject) {
                PolygonMapObject collision = (PolygonMapObject) object;
                entityCreationFactory.createCollisionBorder(new Vector2(collision.getPolygon().getX(), collision.getPolygon().getY()), collision.getPolygon().getVertices());
            }
        }
    }

//    public void genBall() {
//        Vector2 ballPos = new Vector2();
//        mapCmp.dropArea.getCenter(ballPos);
//        entityCreationFactory.createBall(ballPos, 12, 12, Balls.BASE);
//    }

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
            pegPos.y = Math.round(pegPos.y);
            for(int j = 0; j < pegEntities.size(); j++) {
                Entity candidate = pegEntities.get(j);
                Vector2 candidatePos = new Vector2();
                candidatePos.set(ECSEngine.trnCmpMpr.get(candidate).renderPosition);

                float dy = candidatePos.y - pegPos.y;
                dy = Math.round(dy);
                if(dy <= 0) {
                    continue;
                }
                candidates.add(candidate);
            }
            if(candidates.size <= 0) {
                continue;
            }

//            Array<Vector2> posArray = new Array<>();
//            for(Entity can : candidates) {
//                posArray.add(ECSEngine.trnCmpMpr.get(can).renderPosition);
//            }
//            System.out.println(pegPos + " before: " + posArray);

            //can possible reverse candidate init order instead of "flipping" y values in array?
            candidates.sort((a, b) -> Float.compare(
                (ECSEngine.trnCmpMpr.get(a).renderPosition.y),
                (ECSEngine.trnCmpMpr.get(b).renderPosition.y)
            ));

            float nextPegYLevel = ECSEngine.trnCmpMpr.get(candidates.first()).renderPosition.y;
            Array<Entity> yLevelCandidates = new Array<>();

            for(Entity entity : candidates) {
                if(ECSEngine.trnCmpMpr.get(entity).renderPosition.y == nextPegYLevel) {
                    yLevelCandidates.add(entity);
                }
            }

            yLevelCandidates.sort((a, b) -> Float.compare(
                Math.abs(ECSEngine.trnCmpMpr.get(a).renderPosition.x - pegPos.x),
                Math.abs(ECSEngine.trnCmpMpr.get(b).renderPosition.x - pegPos.x)
            ));

//            Array<Vector2> posArray2 = new Array<>();
//            for(Entity can : yLevelCandidates) {
//                posArray2.add(ECSEngine.trnCmpMpr.get(can).renderPosition);
//            }
//            System.out.println(pegPos + " after: " + posArray2);

            for(int k = 0; k < Math.min(2, yLevelCandidates.size); k++) {
                if(ECSEngine.trnCmpMpr.get(yLevelCandidates.get(k)).renderPosition.y == nextPegYLevel) {
                    pegCmp.nextPegs.add(yLevelCandidates.get(k));
                }
            }
        }
    }
}
