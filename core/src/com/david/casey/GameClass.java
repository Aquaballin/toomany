package com.david.casey;

import com.badlogic.gdx.*;
import com.badlogic.gdx.Game;
import com.david.casey.screens.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import com.badlogic.gdx.math.Vector2;
import com.david.casey.sprites.*;

import java.util.HashMap;
//import com.david.casey.scenes.*;

/*

Fast placed multiplayer
http://www.gabrielgambetta.com/fpm1.html
 */

public class GameClass extends Game {
    public GoogleApiClient googleApiClient;
    public SpriteBatch batch;
    public static final int MENU_STATE_WIDTH = 512;
    public static final int MENU_STATE_HEIGHT = 288;
    public static final int SHITTY_PETES_WHARF_WIDTH = 512;
    public static final int SHITTY_PETES_WHARF_HEIGHT = 288;



    public static final float PPM = 100;

    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new MenuScreen(this));

        /*
        batch = new SpriteBatch();
        yourPlayerTexture = new Texture("starterHomelessGuy1.png");
        otherPlayerTexture = new Texture("starterHomelessGuy2.png");
        otherPlayers = new HashMap<String, theirHomelessGuy>();
        connectSocket();
        configSocketEvents();
        */
    }

    public GameClass() {
        super();
    }

    @Override
    public void dispose() {
        batch.dispose();
        super.dispose();
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void setScreen(Screen screen) {
        super.setScreen(screen);
    }

    @Override
    public Screen getScreen() {
        return super.getScreen();
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
    */



    /*

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
}
