package com.me.mariobros.items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.me.mariobros.MarioBros;
import com.me.mariobros.screens.PlayScreen;
import com.me.mariobros.sprite.Mario;

public abstract class Item extends Sprite {
    protected PlayScreen screen;
    protected World world;
    protected Vector2 velocity;
    protected boolean toDestroy;
    protected boolean destroyed;
    protected Body body;

    public Item(PlayScreen screen, float x, float y) {
        this.screen = screen;
        this.world = screen.getWorld();
        setPosition(x, y);
        setBounds(getX(), getY(), 16 / MarioBros.PPM, 16 / MarioBros.PPM);

        defineItem();

        toDestroy = false;
        destroyed = false;
    }

    public abstract void defineItem();

    public abstract void use(Mario mario);

    public void update(float dt) {
        if (toDestroy && !destroyed) {
            world.destroyBody(body);
            destroyed = true;
        }
    }

    public void destroy() {
        toDestroy = true;
    }

    @Override
    public void draw(Batch batch) {
        if (!destroyed) {
            super.draw(batch);
        }
    }

    public void reverseVelocity(boolean x, boolean y) {
        if (x) {
            velocity.x = -velocity.x;
        }
        if (y) {
            velocity.y = -velocity.y;
        }
    }
}
