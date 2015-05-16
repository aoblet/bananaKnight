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
package fr.plafogaj.game.weapon.longRange;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import fr.plafogaj.game.character.Character;
import fr.plafogaj.game.engine.TiledMapConfig;
import fr.plafogaj.screens.Game;

import java.util.LinkedList;

public class Arc extends LongRange {

    public Arc(Character c, TiledMapConfig mapConfig){
        super(c, (Sound)Game.ASSET_MANAGER.get("sound/weapon/throw.mp3"),
                Gdx.files.internal("img/weapon/arc.png"), mapConfig);
        m_minBreakTime_cadenceHit = 0.3f;
    }

    public Arc(TiledMapConfig mapConfig){
        this(null, mapConfig);
    }

    @Override
    public void hit(TiledMapTileLayer collisionLayer){
        super.hit(collisionLayer);

        if(!m_isUsable)
            return;

        m_forceDirection.x = m_character.isFacesRight() ? Math.abs(m_forceDirection.x) :-Math.abs(m_forceDirection.x);
        m_bullets.add(new Bullet(m_position.cpy().add(0,m_character.getSize().y*0.5f), m_forceDirection, LongRange.GRAVITY));
    }

    @Override
    public void dispose(){
        super.dispose();
    }
}
