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
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Timer;
import fr.plafogaj.game.BananaKnight;

public class Splash implements Screen {

    private Texture m_textureSplash;
    private Texture m_textureLogo;
    private Image m_splashImage;
    private Image m_LogoImage;
    private Stage m_stage;
    private Music m_music;

    public Splash(){
        m_textureSplash = new Texture(Gdx.files.internal("img/splash/wallpaper.jpg"));
        m_splashImage = new Image(m_textureSplash);
        m_splashImage.setWidth(BananaKnight.WIDTH);
        m_splashImage.setHeight(BananaKnight.HEIGHT);

        m_textureLogo = new Texture(Gdx.files.internal("img/splash/logo.png"));
        m_LogoImage = new Image(m_textureLogo);
        m_LogoImage.setX(BananaKnight.WIDTH /2 - m_LogoImage.getWidth()/2);
        m_LogoImage.setY(BananaKnight.HEIGHT / 2 - m_LogoImage.getHeight() / 2);
        m_stage = new Stage();
        m_music = Gdx.audio.newMusic(Gdx.files.internal("music/splash.mp3"));
    }

    @Override
    public void show() {
        m_music.setLooping(true);
        m_music.play();
        m_stage.addActor(m_splashImage);
        m_stage.addActor(m_LogoImage);
        m_splashImage.addAction(Actions.sequence(Actions.alpha(0),
                Actions.fadeIn(1),
                Actions.delay(3),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        Timer.schedule(new Timer.Task() {
                            @Override
                            public void run() {
                                m_music.setVolume(m_music.getVolume() - 0.05f);
                            }
                        },0,0.3f, 21);
                    }
                }),
                Actions.fadeOut(3),
                Actions.delay(2.5f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        ((BananaKnight) (Gdx.app.getApplicationListener())).setScreen(new MainMenu());
                    }
                })));

        m_LogoImage.addAction((Actions.sequence(
                Actions.alpha(0),
                Actions.delay(1),
                Actions.fadeIn(1),
                Actions.delay(1.3f),
                Actions.fadeOut(1)
        )));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        m_stage.act(delta);  // we update all actors
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
        m_textureLogo.dispose();
        m_textureSplash.dispose();
        m_stage.dispose();
        m_music.dispose();
    }
}
