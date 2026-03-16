package com.xxtreasurekingxx.plinko.Input;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Array;

public class InputManager implements InputProcessor {
    private final Array<InputProcessor> listeners;

    public InputManager() {
        listeners = new Array<>();
    }

    public void addListener(final InputProcessor listener) {
        listeners.add(listener);
    }

    public void removeListener(final InputProcessor listener) {
        listeners.removeValue(listener, true);
    }

    @Override
    public boolean keyDown(int keycode) {
        for(InputProcessor listener : listeners) {
            listener.keyDown(keycode);
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        for(InputProcessor listener : listeners) {
            listener.keyUp(keycode);
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        for(final InputProcessor listener : listeners) {
            listener.touchDown(screenX, screenY, pointer, button);
        }
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
