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
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import fr.plafogaj.game.BananaKnight;

import java.io.File;

public class EndMenu implements Screen {
    private VisTextButton m_menuButton;
    private VisTextButton m_playAgainButton;
    private VisTextButton m_message;
    private VisTable m_mainTable;
    private Stage m_stage;
    private Texture m_wallpaperTexture;
    private Image m_wallpaperImage;
    private Music m_music;
    private FileHandle m_levelFile;
    private boolean m_success;
    private String m_timeLevel;

    public EndMenu(FileHandle levelFile, boolean success, String timeLevel){
        m_wallpaperTexture = new Texture(Gdx.files.internal("map/decor/background.png"));
        m_wallpaperImage = new Image(m_wallpaperTexture);
        m_levelFile = levelFile;
        m_stage = new Stage();
        m_mainTable = new VisTable();
        m_menuButton = new VisTextButton("Back to menu");
        m_playAgainButton = new VisTextButton("Try again!");
        m_timeLevel = timeLevel;

        m_music = Game.ASSET_MANAGER.get("music/end.mp3");

        if((m_success = success))
            m_message = new VisTextButton("Congrats! You've beaten the ninja empire III of Copernic");
        else
            m_message = new VisTextButton("You fail, you need more practice");
        m_message.setText(m_message.getText()+"\nTime level: "+m_timeLevel);

        m_mainTable.add(m_message).size(m_message.getWidth()+100, 60).center().padBottom(30).padLeft(60).padRight(40).row();
        m_mainTable.add(m_playAgainButton).size(300, 60).padBottom(10).padLeft(10).row();
        m_mainTable.add(m_menuButton).size(300, 60).padBottom(10).padLeft(10).row();
        m_mainTable.setFillParent(true);
        this.setListeners();
    }

    public void setListeners(){
        m_playAgainButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((BananaKnight) (Gdx.app.getApplicationListener())).setScreen(new Game(m_levelFile));
            }
        });

        m_menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((BananaKnight) (Gdx.app.getApplicationListener())).setScreen(new MainMenu());
            }
        });
    }

    @Override
    public void show() {
        Gdx.input.setCursorCatched(false);
        m_stage.addActor(m_wallpaperImage);
        m_stage.addActor(m_mainTable);
        Gdx.input.setInputProcessor(m_stage);
        m_music.play();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        m_stage.act();
        m_stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        m_music.pause();
    }

    @Override
    public void resume() {
        m_music.play();
    }

    @Override
    public void hide() {
        m_music.stop();
        this.dispose();
    }

    @Override
    public void dispose() {
        m_wallpaperTexture.dispose();
        m_music.dispose();
    }
}
