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
        setSize(64, 64);
        bounds = new Rectangle();
        bounds.set(getX(), getY(), getWidth(), getHeight());
        setVisible(false);
    }

    @Override
    public void act(float delta) {

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(manager.get("shield.png", Texture.class), getX(), getY());
    }
    public Rectangle getBounds() {
        return bounds;
    }

}
