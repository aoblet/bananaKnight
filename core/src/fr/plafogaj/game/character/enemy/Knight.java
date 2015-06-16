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

package fr.plafogaj.game.character.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import fr.plafogaj.game.character.Character;
import fr.plafogaj.game.engine.TiledMapConfig;

import java.util.LinkedList;

public class Knight extends Enemy{
    public Knight(Vector2 position, TiledMapConfig mapConfig, FileHandle spriteConfigFile, String name, int life, LinkedList players) {
        super(position, mapConfig, Gdx.files.internal("img/sprite/player/configEnemy.json"), "Knight", 2, players);
    }

    public Knight(Vector2 position, TiledMapConfig mapConfig, LinkedList<Character> players){
        this(position, mapConfig, null, "",0,players);
    }
}
