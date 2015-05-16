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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import fr.plafogaj.game.character.*;
import fr.plafogaj.game.engine.TiledMapConfig;
import fr.plafogaj.game.engine.TiledMapOrthographicCamera;

import java.util.LinkedList;

public class Sword extends ShortRange {
    public Sword(TiledMapConfig mapConfig) {
        super( null, Gdx.audio.newSound(Gdx.files.internal("sound/weapon/sword_swipe.mp3")),
               Gdx.files.internal("img/sprite/weapon/sword.png"), mapConfig);
        m_minBreakTime_cadenceHit = 0.65f;
    }

    @Override
    public void render(float deltaTime, Batch toRender, TiledMapOrthographicCamera camera, TiledMapTileLayer collisionLayer,
                       LinkedList charactersEnemy) {
        super.render(deltaTime, toRender, camera, collisionLayer, charactersEnemy);
    }

    @Override
    public void hit( TiledMapTileLayer collisionLayer) {
        super.hit(collisionLayer);
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
