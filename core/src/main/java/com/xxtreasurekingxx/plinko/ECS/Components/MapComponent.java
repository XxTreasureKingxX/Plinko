package com.xxtreasurekingxx.plinko.ECS.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import java.util.HashMap;

public class MapComponent implements Component, Pool.Poolable {
    public TiledMap map;
    public int width;
    public int height;
    public HashMap<Vector2, Entity> dropPoints;

    @Override
    public void reset() {
        map = null;
        width = 0;
        height = 0;
        dropPoints = null;
    }
}
