/**
 * Copyright 2015 see AUTHORS file
 * This file is part of BananaKnight.
 * BananaKnight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * BananaKnight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with BananaKnight.  If not, see <http://www.gnu.org/licenses/>
 */
package fr.plafogaj.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import fr.plafogaj.game.BananaKnight;
import fr.plafogaj.game.artefact.Artifact;
import fr.plafogaj.game.artefact.ArtifactLife;
import fr.plafogaj.game.character.Character;
import fr.plafogaj.game.character.enemy.Knight;
import fr.plafogaj.game.character.player.Player;
import fr.plafogaj.game.engine.BananaAssetManager;
import fr.plafogaj.game.engine.GameLevelConfig;
import fr.plafogaj.game.engine.TiledMapConfig;
import fr.plafogaj.game.engine.TiledMapOrthographicCamera;
import fr.plafogaj.game.weapon.longRange.Arc;

import java.util.ArrayList;
import java.util.LinkedList;


public class Game implements Screen, InputProcessor {
    protected BananaAssetManager m_assetManager;
    public static BananaAssetManager ASSET_MANAGER;

    protected LinkedList<Character> m_enemiesList;
    protected LinkedList<Character> m_playersList;

    private Music m_ambianceMusic;
    private Player m_player;
    private GameLevelConfig m_gameLevelConfig;
    private TiledMapConfig m_tiledMapConfig;
    private TiledMapOrthographicCamera m_camera;
    private OrthogonalTiledMapRenderer m_mapRenderer;

    public Batch gameBatch;
    public BitmapFont font;
    public Batch screenBatch;
    public float timeLevel;
    protected Texture m_endTexture;
    protected Vector2 m_endTextureSize;
    protected LinkedList<Arc> m_arcsTmp;
    protected ArrayList<ArtifactLife> m_artifactsLife;
    protected String m_timeFormatted;

    public Game(FileHandle levelConfig){
        //singleton
        if(Game.ASSET_MANAGER == null){
            m_assetManager = new BananaAssetManager();
            Game.ASSET_MANAGER = m_assetManager;
        }
        else
            m_assetManager = ASSET_MANAGER;

        m_endTexture = m_assetManager.get("img/game/end.png");
        m_endTextureSize = new Vector2(m_endTexture.getWidth()*TiledMapConfig.TILE_UNIT_SCALE, m_endTexture.getHeight()*TiledMapConfig.TILE_UNIT_SCALE);
        m_gameLevelConfig = new GameLevelConfig(levelConfig);
        m_tiledMapConfig = m_gameLevelConfig.getTileMapConfig();

        m_playersList = new LinkedList<Character>();
        m_enemiesList = new LinkedList<Character>();
        m_artifactsLife = new ArrayList<ArtifactLife>();
        m_arcsTmp = new LinkedList<Arc>(); //when an enemy dies and have shot bananas

        m_player = new Player(new Vector2(2,10), m_tiledMapConfig);
        m_playersList.add(m_player);

        this.initEnemies();
        this.initLifeArtifacts();

        m_player.setEnemies(m_enemiesList);

        m_mapRenderer = m_tiledMapConfig.getMapRenderer();
        gameBatch = m_mapRenderer.getBatch();
        m_camera = m_tiledMapConfig.getCamera();
        m_ambianceMusic = Game.ASSET_MANAGER.get("music/ambiance.mp3");
        m_ambianceMusic.setLooping(true);

        font = new BitmapFont();
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        screenBatch = new SpriteBatch();

        timeLevel = 0;
        m_timeFormatted = "";
    }

    private void initLifeArtifacts(){
        int nbArtifacts = 30;
        Vector2 pos = new Vector2();
        int widthMap = m_tiledMapConfig.getMap().getProperties().get("width", Integer.class);

        for(int i=0; i<nbArtifacts; ++i){
            pos.set(MathUtils.random(0,widthMap), MathUtils.random(0,30));
            m_artifactsLife.add(new ArtifactLife(MathUtils.random(1,5), pos));
        }
    }

    private void initEnemies(){
        for(MapObject e : m_tiledMapConfig.getEnemiesObjects()){
            EllipseMapObject poly = (EllipseMapObject)e;
            m_enemiesList.add(new Knight(new Vector2(poly.getEllipse().x * TiledMapConfig.TILE_UNIT_SCALE,
                    poly.getEllipse().y * TiledMapConfig.TILE_UNIT_SCALE),
                    m_tiledMapConfig, m_playersList));
        }

    }

    private void renderArcPersistent(float deltaTime){
        for(Arc a: m_arcsTmp){
            a.render(deltaTime, gameBatch, m_camera, m_tiledMapConfig.getCollisionLayer(), null);
            if(a.getBullets().size() == 0)
                m_arcsTmp.remove(a);
        }
    }

    private void renderEnemies(float deltaTime){
        //TODO: opti m_tiledMapConfig.getEnemiesLayerFake().setCell((int)m_enemiesList.get(i).getPosition().x, (int)m_enemiesList.get(i).getPosition().y, new TiledMapTileLayer.Cell());
        for(int i=0; i<m_enemiesList.size();++i){
            //if(m_enemiesList.get(i).isInScreen(m_camera)) {
                m_enemiesList.get(i).render(deltaTime, gameBatch);
                if(m_enemiesList.get(i).getLife() <= 0){
                    if(m_enemiesList.get(i).getArc().getBullets().size() != 0)
                        m_arcsTmp.add(m_enemiesList.get(i).getArc());
                    m_enemiesList.remove(i);
                    --i;
                }
            //}
        }
    }

    private void renderBGDecorParallax(){
        m_mapRenderer.setView(m_camera);
        m_mapRenderer.render(TiledMapConfig.SKY_LAYER);

        m_mapRenderer.setView(m_camera.getParallaxCamera(.07f, 1f));
        m_mapRenderer.render(TiledMapConfig.BG5_LAYER);

        m_mapRenderer.setView(m_camera.getParallaxCamera(.1f, 1f));
        m_mapRenderer.render(TiledMapConfig.BG4_LAYER);

        m_mapRenderer.setView(m_camera.getParallaxCamera(.2f, 1f));
        m_mapRenderer.render(TiledMapConfig.BG3_LAYER);

        m_mapRenderer.setView(m_camera.getParallaxCamera(.5f,1f));
        m_mapRenderer.render(TiledMapConfig.BG2_LAYER);

        m_mapRenderer.setView(m_camera.getParallaxCamera(.8f,1f));
        m_mapRenderer.render(TiledMapConfig.BG1_LAYER);
    }

    private void renderMiddleDecorParallax(){
        m_mapRenderer.setView(m_camera.getParallaxCamera(.99f,1f));
        m_mapRenderer.render(TiledMapConfig.MIDDLE_LAYER);
    }

    private void renderFGDecorParallax(){
        m_mapRenderer.setView(m_camera.getParallaxCamera(2l, 1f));
        m_mapRenderer.render(TiledMapConfig.FG_LAYER);
    }

    private boolean isEndGame(){
        if(!m_player.isAlive()|| (m_player.getPosition().epsilonEquals(m_tiledMapConfig.getEndObjectCoord(),2)))
            return true;
        return false;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
        Gdx.input.setCursorCatched(true);
        m_ambianceMusic.play();
    }

    private void renderEndObject(){
        gameBatch.draw(m_endTexture, m_tiledMapConfig.getEndObjectCoord().x, m_tiledMapConfig.getEndObjectCoord().y,
                m_endTextureSize.x, m_endTextureSize.y);
    }

    private void renderLifeArtifacts(){
        Artifact a; // need for int i loop, otherwise ConcurrentModificationException...
        for(int i=0; i<m_artifactsLife.size();++i ){
            a = m_artifactsLife.get(i);
            a.render(gameBatch);
            if(a.isOverlapped(m_player.getRectangle())){
                a.processOnCharacter(m_player);
                m_artifactsLife.remove(i);
                --i;
            }
        }
    }

    @Override
    public void render(float delta) {
        timeLevel += delta;

        // clear the screen
        Gdx.gl.glClearColor(0.7f, 0.7f, 1.0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        this.renderBGDecorParallax();
        this.renderMiddleDecorParallax();

        m_mapRenderer.setView(m_camera);
        m_mapRenderer.render(TiledMapConfig.COLLISION_LAYER);

        gameBatch.begin();
        m_player.render(delta, gameBatch);
        this.renderEnemies(delta);
        this.renderLifeArtifacts();
        this.renderEndObject();
        this.renderArcPersistent(delta);
        gameBatch.end();

        this.renderFGDecorParallax();
        this.drawInfoPlayer();
        m_camera.updateScrolling(m_player);

        this.updateTimeFormatted();

        if(this.isEndGame()){
            ((BananaKnight) (Gdx.app.getApplicationListener())).setScreen(new EndMenu(m_gameLevelConfig.getLevelFile(), m_player.isAlive(), m_timeFormatted));
            this.dispose();
        }
    }

    private void drawInfoPlayer(){
        screenBatch.begin();
        font.draw(screenBatch, "Life: " + m_player.getLife(), 0, BananaKnight.HEIGHT);
        font.draw(screenBatch, "Enemies: " + m_enemiesList.size(), 0, BananaKnight.HEIGHT-15);
        font.draw(screenBatch, "Time: " + m_timeFormatted, 0, BananaKnight.HEIGHT-45);
        font.draw(screenBatch, "Banana thrown: " + m_player.getArc().getHitCpt(), 0, BananaKnight.HEIGHT-60);
        font.draw(screenBatch, "Sword hit: " + m_player.getSword().getHitCpt(), 0, BananaKnight.HEIGHT-75);
        screenBatch.end();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
        m_ambianceMusic.pause();
    }

    @Override
    public void resume() {
        m_ambianceMusic.play();
    }

    @Override
    public void hide() {
        m_ambianceMusic.stop();
        this.dispose();
    }

    public void updateTimeFormatted(){
        if(timeLevel < 60 )
            m_timeFormatted = (int) (timeLevel) + " s";
        else if(timeLevel <60*60)
            m_timeFormatted = (int)(timeLevel/60)+":"+(int)(timeLevel)%60;
    }

    @Override
    public void dispose() {
        font.dispose();
        m_tiledMapConfig.getMap().dispose();
        m_player.dispose();
        for(Character e: m_enemiesList)
            e.dispose();
        m_playersList.clear();
        m_enemiesList.clear();
    }

    @Override
    public void finalize(){
        m_assetManager.dispose();
        m_assetManager = null;
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.ESCAPE){
            ((BananaKnight) (Gdx.app.getApplicationListener())).setScreen(new MainMenu());
            this.dispose();
            return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
