package com.me.mariobros.sprite;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.me.mariobros.MarioBros;
import com.me.mariobros.scenes.Hud;
import com.me.mariobros.screens.PlayScreen;

public class Brick extends InteractiveTileObject {

    public Brick(PlayScreen screen, MapObject object) {
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(MarioBros.BRICK_BIT);
    }

    @Override
    public void onHeadHit(Mario mario) {
        if (mario.isBig()) {

            setCategoryFilter(MarioBros.DESTROYED_BIT);
            getCell().setTile(null);
            Hud.addScore(200);
            MarioBros.manager.get("audio/sounds/breakblock.wav", Sound.class).play();
        } else {
            MarioBros.manager.get("audio/sounds/bump.wav", Sound.class).play();
        }
    }
}
