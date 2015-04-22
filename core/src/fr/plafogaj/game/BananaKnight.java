/**
 *  Copyright 2015 see AUTHORS file
 *  This file is part of BananaKnight.
 *  BananaKnight is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  BananaKnight is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  You should have received a copy of the GNU General Public License
 *  along with BananaKnight.  If not, see <http://www.gnu.org/licenses/>
 */

package fr.plafogaj.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fr.plafogaj.screens.Splash;

public class BananaKnight extends Game {
	public static final String TITLE = "Banana Knight Game";
	public static final int WIDTH = 1280, HEIGHT = 720;

	public SpriteBatch batch;
	public BitmapFont font;

	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		this.setScreen(new Splash());
	}

	@Override
	public void render () {
		super.render();
	}
	@Override
	public void dispose(){
		batch.dispose();
		font.dispose();
	}
}
