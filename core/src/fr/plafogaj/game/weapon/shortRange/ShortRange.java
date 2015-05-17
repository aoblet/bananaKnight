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
import com.badlogic.gdx.utils.Timer;
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
        m_soundHitTarget = Game.ASSET_MANAGER.get("sound/weapon/sword_hit.mp3");
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
        Vector2 sizeComputed = m_character.getSize().cpy().add(m_character.getSize().x/2f, 0);
        Character enemyHit = this.getCharacterHit(m_character.getEnemies(), m_character.getPosition().cpy(), sizeComputed);

        if(enemyHit != null){
            enemyHit.impactLife(m_force);
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    m_soundHitTarget.play();
                }
            }, 0.05f);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
