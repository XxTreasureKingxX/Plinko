package com.xxtreasurekingxx.plinko.ECS.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.utils.Pool;

public class B2DComponent implements Pool.Poolable, Component {
    public float width;
    public float height;
    public Body body;
    public boolean isSensor;
    public boolean needsBody;
    public boolean needsDelete;
    public boolean shouldCollide;
    public boolean updateCollideStatus;
    public boolean bodyTypeChanged;
    public boolean pauseBody;
    public BodyDef.BodyType bodyType;
    public Shape bodyShape;

    @Override
    public void reset() {
        if(body != null) {
            body.getWorld().destroyBody(body);
            body = null;
        }
        width = 0;
        height = 0;
        isSensor = false;
        needsBody = false;
        needsDelete = false;
        shouldCollide = false;
        updateCollideStatus = false;
        pauseBody = false;
        bodyType = null;
        bodyShape = null;
    }
}
