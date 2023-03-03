package com.mygdx.snake;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Player extends Actor {
    Rectangle bounds;
    AssetManager manager;
    float speedy, gravity;

    private boolean hasShield;
    Player()
    {
        setX(200);
        setY(280 / 2 - 64 / 2);
        setSize(64,45);
        bounds = new Rectangle();

        speedy = 0;
        gravity = 850f;
    }
    @Override
    public void act(float delta)
    {
        //Actualitza la posició del jugador amb la velocitat vertical
        moveBy(0, speedy * delta);
//Actualitza la velocitat vertical amb la gravetat
        speedy -= gravity * delta;
        bounds.set(getX(), getY(), getWidth(), getHeight());
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if(hasShield){
            batch.draw(manager.get("bird_blue.png", Texture.class), getX(), getY());
        }else{
            batch.draw(manager.get("bird.png", Texture.class), getX(), getY());
        }

    }
    public Rectangle getBounds() {
        return bounds;
    }
    public void setManager(AssetManager manager) {
        this.manager = manager;
    }

    void impulso()
    {
        speedy = 400f;
    }

    public boolean hasShield() {
        return hasShield;
    }
    public void setHasShield(boolean hasShield) {
        this.hasShield = hasShield;
    }

    public boolean collidesWithShield(Shield shield) {
        return bounds.overlaps(shield.getBounds());
    }
    public boolean collidesWithEnemy(Enemy enemy) {
        return bounds.overlaps(enemy.getBounds());
    }
}
