package com.mygdx.snake;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Shield extends Actor {
    Rectangle bounds;
    AssetManager manager;

    Shield(){
        setSize(50,50);
        bounds = new Rectangle();
        setVisible(false);
    }

    @Override
    public void act(float delta) {
        moveBy(-200 * delta, 0);
        bounds.set(getX(), getY(), getWidth(), getHeight());
        if(!isVisible())
            setVisible(true);
        if (getX() < -64)
            remove();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(manager.get("shield.png", Texture.class), getX(), getY());
    }
    public Rectangle getBounds() {
        return bounds;
    }

    public void setManager(AssetManager assetManager) {
        this.manager = assetManager;
    }
}

