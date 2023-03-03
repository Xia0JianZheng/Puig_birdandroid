package com.mygdx.snake;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Enemy extends Actor {
    Rectangle bounds;
    AssetManager manager;

    Enemy()
    {
        setSize(32, 32);
        bounds = new Rectangle();
        setVisible(false);
    }
    public void act(float delta)
    {
        moveBy(-400 * delta, 0);
        bounds.set(getX(), getY(), getWidth(), getHeight());
        if(!isVisible())
            setVisible(true);
        if (getX() < -64)
            remove();

    }
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw( manager.get("fireball.png", Texture.class), getX(), getY() );
    }
    public Rectangle getBounds() {
        return bounds;
    }
      public void setManager(AssetManager manager) {
        this.manager = manager;
    }
}
