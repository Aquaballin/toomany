package com.david.casey.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.david.casey.GameClass;

/**
 * Created by David on 1/4/2017.
 */

public class ShittyPetesWharfScreen implements Screen {
    //reference the game
    private GameClass gameClass;
    private TextureAtlas yourTextureAtlas;

    //variables for this screen
    private OrthographicCamera orthographicCamera;
    private Viewport viewport;
    // add this -> private Hud hud;

    //tiled map variables
    private TmxMapLoader tmxMapLoader;
    private TiledMap tiledMap;
    OrthoCachedTiledMapRenderer orthoCachedTiledMapRenderer;

    //box2d physics variables
    private World world;
    private Box2DDebugRenderer b2dr;
    // add this -> private B2

    public ShittyPetesWharfScreen(GameClass gameClass) {
        //yourTextureAtlas = new TextureAtlas()
        this.gameClass = gameClass;
        orthographicCamera = new OrthographicCamera();

        //



    }




    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

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

    }
}

/*
 private final float UPDATE_TIME = 1/60f;
    float timer;
    SpriteBatch batch;
    Texture otherPlayerTexture;
    Texture yourPlayerTexture;
    private Socket socket;
    yourHomelessGuy yourHomelessGuy;
    HashMap<String, theirHomelessGuy> otherPlayers;

    @Override
    public void create() {
        batch = new SpriteBatch();
        yourPlayerTexture = new Texture("starterHomelessGuy1.png");
        otherPlayerTexture = new Texture("starterHomelessGuy2.png");
        otherPlayers = new HashMap<String, theirHomelessGuy>();
        connectSocket();
        configSocketEvents();
    }

    @Override
    public void render() {

        handleInput(Gdx.graphics.getDeltaTime());
        updateServer(Gdx.graphics.getDeltaTime());

        Gdx.gl.glClearColor(255,255,255,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        batch.begin();
        if (yourHomelessGuy != null) {
            yourHomelessGuy.draw(batch);
        }
        for (HashMap.Entry<String, theirHomelessGuy> entry : otherPlayers.entrySet()) {
            entry.getValue().draw(batch);
        }
        batch.end();
    }

    @Override
    public void dispose() {
        yourPlayerTexture.dispose();
        otherPlayerTexture.dispose();
        super.dispose();
    }

    public void connectSocket() {
        try {
            socket = IO.socket("http://localhost:8080");
            socket.connect();
        } catch (Exception e) {
            System.out.println("failed");
        }
    }

    public void handleInput(float deltaTime) {
        if (yourHomelessGuy != null) {
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                yourHomelessGuy.setPosition(yourHomelessGuy.getX() + (-200 * deltaTime), yourHomelessGuy.getY());
            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                yourHomelessGuy.setPosition(yourHomelessGuy.getX() + (+200 * deltaTime), yourHomelessGuy.getY());
            }
        }
    }

    public void updateServer(float deltaTime) {
        timer += deltaTime;
        if (timer >= UPDATE_TIME && yourHomelessGuy != null && yourHomelessGuy.hasMoved()) {
            JSONObject data = new JSONObject();
            try {
                data.put("x", yourHomelessGuy.getX());
                data.put("y", yourHomelessGuy.getY());
                socket.emit("playerMoved",data);
            } catch (JSONException e) {
                Gdx.app.log("SOCKET.IO", "Error sending update data");
            }
        }
    }

    public void configSocketEvents() {
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Gdx.app.log("SocketIO", "Connected");
                yourHomelessGuy = new yourHomelessGuy(yourPlayerTexture);
            }
        }).on("socketID", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String id = data.getString("id");
                    Gdx.app.log("SocketIO", "My ID: " + id);
                } catch (JSONException e) {
                    Gdx.app.log("SocketIO", "Error getting ID");
                }
            }
        }).on("newPlayer", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String playerId = data.getString("id");
                    Gdx.app.log("SocketIO", "New Player Connect: " + playerId);
                    otherPlayers.put(playerId,new theirHomelessGuy(otherPlayerTexture));
                } catch (JSONException e) {
                    Gdx.app.log("SocketIO", "Error getting new player id");
                }
            }


        }).on("playerDisconnected", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String id = data.getString("id");
                    otherPlayers.remove(id);
                } catch (JSONException e) {
                    Gdx.app.log("SocketIO", "Error getting new player id");
                }
            }
        }).on("getPlayers", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONArray objects = (JSONArray) args[0];
                try {
                    for (int i = 0; i < objects.length(); i++) {
                        theirHomelessGuy otherPlayer = new theirHomelessGuy(otherPlayerTexture);
                        Vector2 position = new Vector2();
                        position.x = ((Double) objects.getJSONObject(i).getDouble("x")).floatValue();
                        position.y = ((Double) objects.getJSONObject(i).getDouble("y")).floatValue();
                        otherPlayer.setPosition(position.x,position.y);
                        otherPlayers.put(objects.getJSONObject(i).getString("id"),otherPlayer);
                    }
                } catch (Exception e) {
                    Gdx.app.log("SocketIO", "something is wrong");
                    e.printStackTrace();

                }
            }
        }).on("playerMoved", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String playerId = data.getString("id");
                    Double x = data.getDouble("x");
                    Double y = data.getDouble("y");
                    if (otherPlayers.get(playerId) != null) {
                        otherPlayers.get(playerId).setPosition(x.floatValue(),y.floatValue());
                    }
                } catch (JSONException e) {
                    Gdx.app.log("SocketIO", "Error getting player movements");
                }
            }
        });
    }
 */