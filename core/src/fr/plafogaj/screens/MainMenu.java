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
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Timer;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.CollapsibleWidget;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import fr.plafogaj.game.BananaKnight;

import java.util.Arrays;
import java.util.LinkedList;

public class MainMenu implements Screen{
    private Texture m_wallpaperTexture;
    private Image m_wallpaperImage;

    private Sound m_swordSound;
    private Sound m_guillotineSound;
    private LinkedList<Music> m_ambianceMusicList;

    private LinkedList<Disposable> m_soundList;
    private LinkedList<Disposable> m_texturesList;
    private LinkedList<VisTextButton> m_mainButtonsList;

    private VisTextButton m_playButton;
    private VisTextButton m_loadButton;
    private VisTextButton m_editorButton;
    private VisTextButton m_randomButton;
    private VisTextButton m_exitButton;
    private VisTextButton m_level1Button;
    private VisTextButton m_level2Button;
    private VisTextButton m_level3Button;

    private CollapsibleWidget m_levelCollapsible;
    private VisTable m_levelTable;

    private VisTable m_mainTable;
    private Stage m_stage;

    public MainMenu(){
        VisUI.load();
        m_stage = new Stage();
        m_mainTable = new VisTable();
        this.createImages();
        this.createButtons();
        this.createSounds();
    }

    public void createButtons(){
        m_levelCollapsible = new CollapsibleWidget(m_levelTable = new VisTable());

        m_level1Button = new VisTextButton("Level 1");
        m_level2Button = new VisTextButton("Level 2");
        m_level3Button = new VisTextButton("Level 3");

        m_mainButtonsList = new LinkedList<VisTextButton>(Arrays.asList(
            m_playButton = new VisTextButton("Play", "toggle"),
            m_loadButton = new VisTextButton("Load custom game"),
            m_editorButton = new VisTextButton("Tile map editor"),
            m_randomButton = new VisTextButton("Generate random map"),
            m_exitButton = new VisTextButton("Exit")));
    }

    public void createSounds(){
        m_ambianceMusicList = new LinkedList<Music>(Arrays.asList(
                Gdx.audio.newMusic(Gdx.files.internal("music/mainMenu.mp3")),
                Gdx.audio.newMusic(Gdx.files.internal("sound/weapon/war.mp3"))
        ));

        for(Music m: m_ambianceMusicList)
            m.setLooping(true);

        m_soundList = new LinkedList<Disposable>(Arrays.asList(
            m_swordSound = Gdx.audio.newSound(Gdx.files.internal("sound/weapon/sword_swipe.mp3")),
            m_guillotineSound = Gdx.audio.newSound(Gdx.files.internal("sound/weapon/guillotine.mp3"))
        ));
    }

    public void createImages(){
        m_texturesList = new LinkedList<Disposable>(Arrays.asList(
            m_wallpaperTexture = new Texture(Gdx.files.internal("img/splash/wallpaper.jpg"))
        ));
        m_wallpaperImage = new Image(m_wallpaperTexture);
    }

    public void setButtonListeners(){
        m_playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                m_levelCollapsible.setCollapsed(!m_levelCollapsible.isCollapsed());
            }
        });

        m_level1Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((BananaKnight) (Gdx.app.getApplicationListener())).setScreen(new Game(Gdx.files.internal("level/1/configLevel.json")));
            }
        });

        m_exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                m_mainTable.addAction(Actions.sequence(
                    Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            m_swordSound.play();
                        }
                    }),
                    Actions.moveTo(-BananaKnight.WIDTH/2, m_mainTable.getY(), 0.6f, Interpolation.swing),
                    Actions.delay(0.2f),
                    Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            Gdx.app.exit();
                        }
                    })
                ));
            }
        });

        m_editorButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    System.out.println(Gdx.files.getLocalStoragePath() + Gdx.files.internal("tiledApp/bin/tiled").path());
                    Runtime.getRuntime().exec(Gdx.files.getLocalStoragePath() + Gdx.files.internal("tiledApp/bin/tiled").path());
                } catch (Exception e) {
                    System.out.println("Problem occurs");
                    e.printStackTrace();
                }
            }
        });
    }

    public void addButtonsOnTable(){
        m_levelTable.defaults().left();
        m_levelTable.add(m_level1Button).size(80,50).padLeft(5);
        m_levelTable.add(m_level2Button).size(80,50).padLeft(5);
        m_levelTable.add(m_level3Button).size(80,50).padLeft(5);
        m_levelCollapsible.setCollapsed(true);

        int cpt = 0;
        for(TextButton b: m_mainButtonsList){
            // if playButton
            if (cpt == 1)
                m_mainTable.add(m_levelCollapsible).padBottom(10).row();

            m_mainTable.add(b).size(300, 60).padBottom(10).padLeft(10).row();
            cpt++;
        }
        m_mainTable.setFillParent(true);
    }

    public void playAmbianceMusic(){
        for(Music m: m_ambianceMusicList)
            m.play();
    }

    public void pauseAmbianceMusic(){
        for(Music m: m_ambianceMusicList)
            m.pause();
    }

    public void stopAmbianceMusic(){
        for(Music m: m_ambianceMusicList)
            m.stop();
    }

    public void playSounds(){
        m_swordSound.play();
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                m_guillotineSound.play();
            }
        }, 0.5f);
    }

    public void animateTable(){
        m_mainTable.addAction(Actions.sequence(
            Actions.alpha(0), Actions.delay(0.2f), Actions.fadeIn(0.3f,Interpolation.sine),Actions.delay(0.4f),
            Actions.moveTo(-BananaKnight.WIDTH / 2 + 160, m_mainTable.getY(), 0.6f, Interpolation.swing),
            Actions.run(new Runnable() {
                @Override
                public void run() {
                    m_playButton.setChecked(true);
                    m_levelCollapsible.setCollapsed(false);
                }
            })));
    }

    @Override
    public void show() {
        Gdx.input.setCursorCatched(false);

        this.setButtonListeners();
        this.playSounds();
        this.playAmbianceMusic();
        this.addButtonsOnTable();
        this.animateTable();

        m_stage.addActor(m_wallpaperImage);
        m_stage.addActor(m_mainTable);
        Gdx.input.setInputProcessor(m_stage);
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
        this.pauseAmbianceMusic();
    }

    @Override
    public void resume() {
        this.playAmbianceMusic();
    }

    @Override
    public void hide() {
        this.stopAmbianceMusic();
        this.dispose();
    }

    @Override
    public void dispose() {
        for(Disposable t : m_texturesList)
            t.dispose();
        for(Disposable d : m_soundList)
            d.dispose();
        for(Music m: m_ambianceMusicList)
            m.dispose();

        m_stage.dispose();
    }
}