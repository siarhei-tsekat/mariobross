package com.me.mariobros.sprite;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.me.mariobros.screens.PlayScreen;

public abstract class Enemy extends Sprite {

    protected final World world;
    protected final PlayScreen screen;
    public Vector2 velocity;

    public Body b2body;

    public Enemy(PlayScreen screen, float x, float y) {
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineEnemy();

        velocity = new Vector2(-1, -2);
        b2body.setActive(false);
    }

    protected abstract void defineEnemy();

    public abstract void hitOnHead();

    public void reverseVelocity(boolean x, boolean y) {
        if (x) {
            velocity.x = -velocity.x;
        }
        if (y) {
            velocity.y = -velocity.y;
        }
    }

    public abstract void update(float dt);
}