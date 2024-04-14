package com.me.mariobros.sprite;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.me.mariobros.MarioBros;
import com.me.mariobros.items.Item;
import com.me.mariobros.screens.PlayScreen;

public class Coin extends Item {

    private Animation<TextureRegion> coins;
    private float stateTimer;
    private int lifeTime = 0;

    public Coin(PlayScreen screen, float x, float y) {
        super(screen, x, y);

        Array<TextureRegion> frames = new Array<TextureRegion>();

        frames.add(new TextureRegion(screen.getAtlas().findRegion("coins_white"), 4, 6, 24, 24));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("coins_white"), 34, 6, 24, 24));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("coins_white"), 64, 6, 24, 24));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("coins_white"), 94, 6, 24, 24));

        coins = new Animation<TextureRegion>(0.1f, frames);

    }

    @Override
    public void defineItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(3 / MarioBros.PPM);

        fdef.filter.categoryBits = MarioBros.ITEM_BIT;
        fdef.filter.maskBits = MarioBros.MARIO_BIT | MarioBros.OBJECT_BIT | MarioBros.GROUND_BIT | MarioBros.COIN_BIT | MarioBros.BRICK_BIT;
        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);

        body.applyLinearImpulse(new Vector2(0, 2f), body.getWorldCenter(), true);
    }

    @Override
    public void use(Mario mario) {

    }

    @Override
    public void update(float dt) {
        super.update(dt);
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRegion(coins.getKeyFrame(stateTimer, true));
        stateTimer = stateTimer + dt;

        if (lifeTime++ > 25) {
            destroy();
        }
    }
}
