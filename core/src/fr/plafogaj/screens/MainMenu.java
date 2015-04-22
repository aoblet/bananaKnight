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
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MainMenu implements Screen{
    private Texture m_wallpaper;
    private Image m_imageWallpaper;
    private Stage m_stage;
    private Music m_ambianceMusic;

    private Skin m_skin;
    private TextButton m_buttonPlay;
    private TextButton m_buttonLoad;
    private TextButton m_buttonEditor;
    private TextButton m_buttonRandom;
    private TextButton m_buttonExit;

    private Table m_table;

    public MainMenu(){
        m_stage = new Stage();
        m_table = new Table();
        m_wallpaper = new Texture(Gdx.files.internal("img/splash/wallpaper.jpg"));
        m_imageWallpaper = new Image(m_wallpaper);

        m_skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        m_buttonPlay = new TextButton("Play", m_skin);
        m_buttonLoad = new TextButton("Load game", m_skin);
        m_buttonEditor = new TextButton("Tile map editor", m_skin);
        m_buttonRandom = new TextButton("Generate random map", m_skin);
        m_buttonExit = new TextButton("Exit", m_skin);

        m_ambianceMusic = Gdx.audio.newMusic(Gdx.files.internal("music/mainMenu.mp3"));
    }

    public void addButtonsToTable(TextButton[] buttons){
        for(TextButton b: buttons){
            m_table.add(b).size(300,60).padBottom(10).padLeft(10).row();
        }
        m_table.setFillParent(true);
        m_table.align(Align.left);
    }
    @Override
    public void show() {
        m_ambianceMusic.setLooping(true);
        m_ambianceMusic.play();
        this.addButtonsToTable(new TextButton[]{
            m_buttonPlay,m_buttonLoad, m_buttonEditor, m_buttonRandom, m_buttonExit
        });
        m_stage.addActor(m_imageWallpaper);
        m_stage.addActor(m_table);
        Gdx.input.setInputProcessor(m_stage);


        m_buttonExit.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0,0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        m_stage.act();
        m_stage.draw();
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
    }

    @Override
    public void dispose() {
        m_wallpaper.dispose();
        m_stage.dispose();
        m_ambianceMusic.dispose();
    }
}