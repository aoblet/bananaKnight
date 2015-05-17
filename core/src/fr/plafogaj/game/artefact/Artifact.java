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
package fr.plafogaj.game.artefact;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import fr.plafogaj.game.character.Character;
import fr.plafogaj.game.engine.TiledMapConfig;
import fr.plafogaj.screens.Game;

public abstract class Artifact {
    protected Texture m_texture;
    protected Vector2 m_size;
    protected Vector2 m_position;
    protected Rectangle m_rectOverlaps;
    protected Sound m_sound;

    public Artifact(String fileTexture, Vector2 pos, String sound){
        m_texture = Game.ASSET_MANAGER.get(fileTexture);
        m_position = pos.cpy();
        m_size = new Vector2(m_texture.getWidth()* TiledMapConfig.TILE_UNIT_SCALE, m_texture.getHeight()*TiledMapConfig.TILE_UNIT_SCALE);
        m_rectOverlaps = new Rectangle(m_position.x, m_position.y, m_size.x, m_size.y);
        m_sound = Game.ASSET_MANAGER.get(sound);
    }

    public boolean isOverlapped(Rectangle r){
        return r.overlaps(m_rectOverlaps);
    }

    public void render(Batch toRender){
        toRender.draw(m_texture, m_position.x, m_position.y, m_size.x, m_size.y);
    }

    public void processOnCharacter(Character c){
        m_sound.play();
    }
}
