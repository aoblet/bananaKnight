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
package fr.plafogaj.game.weapon.shortRange;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import fr.plafogaj.game.character.Character;
import fr.plafogaj.game.character.enemy.Enemy;
import fr.plafogaj.game.character.player.Player;
import fr.plafogaj.game.engine.TiledMapConfig;
import fr.plafogaj.game.engine.TiledMapOrthographicCamera;
import fr.plafogaj.game.weapon.Weapon;
import fr.plafogaj.screens.Game;

import java.util.LinkedList;

public abstract class ShortRange extends Weapon {
    protected boolean m_isHitting;
    protected float m_stateTimeAnimation;

    public ShortRange(Character c, Sound soundToPlay, FileHandle fileTexture, TiledMapConfig mapConfig) {
        super(c,soundToPlay,fileTexture, mapConfig);
        m_size.x = TiledMapConfig.TILE_UNIT_SCALE;
        m_size.y = TiledMapConfig.TILE_UNIT_SCALE;
        m_isHitting = false;
        m_stateTimeAnimation = 0;
    }

    @Override
    public void render(float deltaTime, Batch toRender, TiledMapOrthographicCamera camera, TiledMapTileLayer collisionLayer,
                       LinkedList enemies) {
        super.render(deltaTime, toRender, camera, collisionLayer, enemies);
        m_stateTimeAnimation += deltaTime;
    }

    @Override
    public void hit(TiledMapTileLayer collisionLayer) {
        super.hit(collisionLayer);

        if(!m_isUsable)
            return;

        m_isHitting = true;
        int xCell = (int)(m_character.getPosition().x + (m_character.getSize().x*1.5));
        int yCell = (int)(m_character.getPosition().y + m_character.getSize().y*(3f/4));
        xCell = !m_character.isFacesRight() ? (int)(xCell-(m_character.getSize().x*1.5)): xCell;

        if(collisionLayer.getCell(xCell, yCell) != null){
            collisionLayer.setCell(xCell,yCell, null);
        }
        Character enemyHit = null;
        Vector2 positionComputed = m_position.cpy().add(m_character.getSize().x,0);
        if(!m_character.isFacesRight())
            positionComputed.add(-m_character.getSize().x,0);

        enemyHit=this.getCharacterHit(m_character.getEnemies(), positionComputed, m_size);
        if(enemyHit != null){
            enemyHit.impactLife(m_force);
            m_soundHitTarget.play();
        }
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
