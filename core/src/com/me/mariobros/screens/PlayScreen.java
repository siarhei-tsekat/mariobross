package com.me.mariobros.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.me.mariobros.MarioBros;
import com.me.mariobros.items.Item;
import com.me.mariobros.items.ItemDef;
import com.me.mariobros.items.Mushroom;
import com.me.mariobros.scenes.GameOverScreen;
import com.me.mariobros.scenes.Hud;
import com.me.mariobros.sprite.Coin;
import com.me.mariobros.sprite.Enemy;
import com.me.mariobros.sprite.Mario;
import com.me.mariobros.tools.B2WorldCreator_Level_1;
import com.me.mariobros.tools.B2WorldCreator_Level_2;
import com.me.mariobros.tools.WorldContactListener;

import java.util.concurrent.LinkedBlockingQueue;

import sun.jvm.hotspot.opto.Block;

public class PlayScreen implements Screen {

    private MarioBros game;
    private TextureAtlas atlas;

    private OrthographicCamera gamecam;
    public Viewport gamePort;

    private Hud hud;
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private World world;
    private Box2DDebugRenderer b2dr;
    private Mario player;

    private Music music;
    private B2WorldCreator_Level_1 creator;
    private Array<Item> items;
    private LinkedBlockingQueue<ItemDef> itemsToSpawn;

    public PlayScreen(MarioBros game, String levelMap) {

        this.atlas = new TextureAtlas("mario_and_enemies.pack");
        this.game = game;

        gamecam = new OrthographicCamera();
        gamePort = new FillViewport(MarioBros.V_WIDTH / MarioBros.PPM, MarioBros.V_HEIGHT / MarioBros.PPM, gamecam);
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        hud = new Hud(game.batch);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load(levelMap);
        renderer = new OrthogonalTiledMapRenderer(map, 1 / MarioBros.PPM);

        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();

        creator = new B2WorldCreator_Level_1(this);

        player = new Mario(this);
        hud.setPlayer(player);
        world.setContactListener(new WorldContactListener());

        music = MarioBros.manager.get("audio/music/mario_music.ogg", Music.class);
        music.setLooping(true);
        music.play();

        items = new Array<>();
        itemsToSpawn = new LinkedBlockingQueue<>();

    }

    public void spawnItem(ItemDef itemDef) {
        itemsToSpawn.add(itemDef);
    }

    public void handleSpawningItems() {
        if (!itemsToSpawn.isEmpty()) {
            ItemDef itemDef = itemsToSpawn.poll();
            if (itemDef.type == Mushroom.class) {
                items.add(new Mushroom(this, itemDef.position.x, itemDef.position.y));
            }

            if (itemDef.type == Coin.class) {
                items.add(new Coin(this, itemDef.position.x, itemDef.position.y));
            }
        }
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    public TiledMap getMap() {
        return map;
    }

    public World getWorld() {
        return world;
    }

    public void update(float dt) {
        handleInput(dt);
        handleSpawningItems();

        world.step(1 / 60f, 6, 2);

        player.update(dt);
        for (Enemy enemy : creator.getEnemies()) {
            enemy.update(dt);
            if (enemy.getX() < player.getX() + 224 / MarioBros.PPM) {
                enemy.b2body.setActive(true);
            }
        }

        for (Item item : items) {
            item.update(dt);
        }

        hud.update(dt);

        if (player.currentState != Mario.State.DEAD && player.b2body.getPosition().x > 2 && (player.b2body.getPosition().x < 33)) {
            // attach our gamecam to our players.x coordinate
            gamecam.position.x = player.b2body.getPosition().x;
        }

        gamecam.update();

        renderer.setView(gamecam);
    }

    private long lastJumpTime = 0;

    private void handleInput(float dt) {
        if (player.currentState != Mario.State.DEAD) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {

                boolean not_higher_then_80_percentages = player.b2body.getPosition().y < MarioBros.V_HEIGHT / MarioBros.PPM - (MarioBros.V_HEIGHT / 4f / MarioBros.PPM);

                boolean f = (System.currentTimeMillis() - lastJumpTime) > 150 || lastJumpTime == 0;

                if (not_higher_then_80_percentages && f) {
                    player.b2body.applyLinearImpulse(new Vector2(0, 3f), player.b2body.getWorldCenter(), true);
                    lastJumpTime = System.currentTimeMillis();
                }
            }

            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2) {
                player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
            }

            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2) {
                player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
            }
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // render our game map
        renderer.render();

        // render our Box2DebugLines
        b2dr.render(world, gamecam.combined);

        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        player.draw(game.batch);

        for (Enemy enemy : creator.getEnemies()) {
            enemy.draw(game.batch);
        }

        for (Item item : items) {
            item.draw(game.batch);
        }

        game.batch.end();

//        game.sr.setProjectionMatrix(gamecam.combined);
//        game.sr.begin(ShapeRenderer.ShapeType.Line);
//        game.sr.setColor(new Color(1,0,1,0));
//
//        for (Item item : items) {
//            game.sr.rect((item.getX()), (item.getY()), item.getWidth(), item.getHeight());
//        }
//
//        game.sr.rect((player.getX()), (player.getY()), player.getWidth(), player.getHeight());
//        game.sr.end();

        // set our batch to now draw what the hud camera sees
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        if (gameOver()) {
            game.setScreen(new GameOverScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }

    public boolean gameOver() {
        if (player.currentState == Mario.State.DEAD && player.getStateTimer() > 3) {
            return true;
        } else {
            return false;
        }
    }
}
