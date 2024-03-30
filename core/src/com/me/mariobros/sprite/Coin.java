package com.me.mariobros.sprite;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.me.mariobros.MarioBros;
import com.me.mariobros.scenes.Hud;
import com.me.mariobros.screens.PlayScreen;

public class Coin extends InteractiveTileObject {

    private static TiledMapTileSet tileSet;
    private final int BLANK_COIN = 28;

    public Coin(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        tileSet = map.getTileSets().getTileSet("tileset_gutter");
        fixture.setUserData(this);
        setCategoryFilter(MarioBros.COIN_BIT);
    }

    @Override
    public void onHeadHit() {
        getCell().setTile(tileSet.getTile(BLANK_COIN));
        if (getCell().getTile().getId() == BLANK_COIN) {
            MarioBros.manager.get("audio/sounds/bump.wav", Sound.class).play();
        } else {
            MarioBros.manager.get("audio/sounds/coin.wav", Sound.class).play();
        }
        Hud.addScore(100);
    }
}
