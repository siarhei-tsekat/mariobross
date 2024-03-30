package com.me.mariobros;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.me.mariobros.screens.PlayScreen;

public class MarioBros extends Game {
    public static final int V_WIDTH = 400;
    public static final int V_HEIGHT = 208;
    // pixels per meter
    public static final float PPM = 100;
    public SpriteBatch batch;

    public static final short DEFAULT_BIT = 1;
    public static final short MARIO_BIT = 2;
    public static final short BRICK_BIT = 4;
    public static final short COIN_BIT = 8;
    public static final short DESTROYED_BIT = 16;

    /*
        Warning
        Using AssetManager in a static way can cause issues, especially on Android. Instead you may want to pass around AssetManager to those classes that need it.
    */

    public static AssetManager manager;

    @Override
    public void create() {
        batch = new SpriteBatch();

        manager = new AssetManager();
        manager.load("audio/music/mario_music.ogg", Music.class);
        manager.load("audio/sounds/coin.wav", Sound.class);
        manager.load("audio/sounds/bump.wav", Sound.class);
        manager.load("audio/sounds/breakblock.wav", Sound.class);
        manager.finishLoading();

        setScreen(new PlayScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        manager.dispose();
        batch.dispose();
    }
}
