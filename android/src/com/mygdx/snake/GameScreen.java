package com.mygdx.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;

import java.util.Iterator;

public class GameScreen implements Screen {
    final Bird game;
    Stage stage;
    Player player;
    OrthographicCamera camera;
    boolean dead;
    Array<Pipe> obstacles;
    Array<Shield> shields;
    Array<Enemy> enemys;
    long lastObstacleTime;
    long lastShieldTime;
    long lastEnemyTime;
    float score;
    float holey;
    boolean shieldTouch;
    public GameScreen(final Bird gam) {
        this.game = gam;

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        player = new Player();
        player.setManager(game.manager);
        stage = new Stage();
        stage.getViewport().setCamera(camera);
        stage.addActor(player);

        // create the obstacles array and spawn the first obstacle
        obstacles = new Array<Pipe>();
        shields = new Array<>();
        enemys = new Array<>();
        spawnObstacle();
        score = 0;
    }
    @Override
    public void render(float delta) {
        boolean dead = false;
        // clear the screen with a color
        ScreenUtils.clear(0.3f, 0.8f, 0.8f, 1);
// tell the camera to update its matrices.
        camera.update();
// tell the SpriteBatch to render in the
// coordinate system specified by the camera.
        game.batch.setProjectionMatrix(camera.combined);
// begin a new batch
        game.batch.begin();
        game.batch.draw(game.manager.get("background.png", Texture.class), 0, 0);
        game.batch.end();

        // Stage batch: Actors
        stage.getBatch().setProjectionMatrix(camera.combined);
        stage.draw();

        // process user input
        if (Gdx.input.justTouched()) {
            player.impulso();
            //game.manager.get("flap.wav", Sound.class).play();
        }

        stage.act();

// Comprova que el jugador no es surt de la pantalla.
// Si surt per la part inferior, game over
        if (player.getBounds().y > 480 - 45)
            player.setY( 480 - 45 );
        if (player.getBounds().y < 0 - 45) {
            dead = true;
        }

        game.batch.begin();
        game.smallFont.draw(game.batch, "Score: " + (int)score, 10,
                470);
        game.batch.end();

//La puntuació augmenta amb el temps de joc
        score += Gdx.graphics.getDeltaTime();

        if (TimeUtils.nanoTime() - lastShieldTime > 5500000000L){
            spawnShield();
            shieldTouch = true;
        }

        if (TimeUtils.nanoTime() - lastEnemyTime > 6500000000L){
            spawnEnemy();
        }

        for (Shield shield : shields) {
            if (player.collidesWithShield(shield)) {
                // Handle collision between player and shield
                player.setHasShield(true);
                shield.remove();
                shields.removeIndex(0);
            }
        }

        for (Enemy enemy : enemys) {
            if (player.collidesWithEnemy(enemy))
                if(player.hasShield()) {
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            // Remove the shield
                            player.setHasShield(false);

                        }
                    }, 0.75f);
                }else {
                    dead = true;
                }
            }


        // Comprova si cal generar un obstacle nou
        if (TimeUtils.nanoTime() - lastObstacleTime > 1500000000)
            spawnObstacle();


// Comprova si les tuberies colisionen amb el jugador
        Iterator<Pipe> iter = obstacles.iterator();
        while (iter.hasNext()) {
            Pipe pipe = iter.next();
            if (pipe.getBounds().overlaps(player.getBounds())) {
                if(player.hasShield()) {

                        pipe.setManager(game.manager);
                        pipe.setBreaked(true);

                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            // Remove the shield
                            player.setHasShield(false);
                        }
                    }, 0.75f);
                }else {
                    dead = true;
                }

            }
        }

        iter = obstacles.iterator();
        while (iter.hasNext()) {
            Pipe pipe = iter.next();
            if (pipe.getX() < -64) {
                obstacles.removeValue(pipe, true);
            }
        }


        if(dead)
        {
            game.lastScore = (int)score;
            if(game.lastScore > game.topScore)
                game.topScore = game.lastScore;
            //game.manager.get("fail.wav",    Sound.class).play();
            game.setScreen(new GameOverScreen(game));
            dispose();
        }
    }

    private void spawnObstacle() {
       holey = MathUtils.random(50, 230);
// Calcula la alçada de l'obstacle aleatòriament
// Crea dos obstacles: Una tubería superior i una inferior
        Pipe pipe1 = new Pipe();
        pipe1.setX(800);
        pipe1.setY(holey - 230);
        pipe1.setUpsideDown(true);
        pipe1.setManager(game.manager);
        obstacles.add(pipe1);
        stage.addActor(pipe1);
        Pipe pipe2 = new Pipe();
        pipe2.setX(800);
        pipe2.setY(holey + 200);
        pipe2.setUpsideDown(false);
        pipe2.setManager(game.manager);
        obstacles.add(pipe2);
        stage.addActor(pipe2);
        lastObstacleTime = TimeUtils.nanoTime();
    }

    private void spawnShield(){
        float holey = MathUtils.random(50, 230);
        Shield shieldItem = new Shield();
        shieldItem.setX(800);
        shieldItem.setY(holey);
        shieldItem.setManager(game.manager);
        shields.add(shieldItem);
        stage.addActor(shieldItem);
        lastShieldTime = TimeUtils.nanoTime();
    }

    private void spawnEnemy(){
        float holey = MathUtils.random(50, 230);
        Enemy enemy = new Enemy();
        enemy.setX(800);
        enemy.setY(holey);
        enemy.setManager(game.manager);
        enemys.add(enemy);
        stage.addActor(enemy);
        lastEnemyTime = TimeUtils.nanoTime();
    }


    @Override
    public void resize(int width, int height) {
    }
    @Override
    public void show() {
    }
    @Override
    public void hide() {
    }
    @Override
    public void pause() {
    }
    @Override
    public void resume() {
    }
    @Override
    public void dispose() {
    }
}

