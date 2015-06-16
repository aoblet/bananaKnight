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

package fr.plafogaj.game.character.enemy.IA;

import com.badlogic.gdx.math.Vector2;
import fr.plafogaj.game.character.Character;
import fr.plafogaj.game.character.enemy.Enemy;
import fr.plafogaj.game.character.player.Player;

import java.util.LinkedList;

public abstract class IA {
    protected LinkedList<Character> m_playersList;
    protected Player m_player; // more readable
    protected Enemy m_owner;
    protected boolean m_keepMove;
    protected Vector2 m_distancePlayerPoint;
    protected boolean m_isPlayerRight;

    public IA(LinkedList<Character> players, Enemy owner){
        m_distancePlayerPoint = new Vector2();
        m_playersList = players;
        m_player = (Player)m_playersList.get(0); //for the moment
        m_owner = owner;
        m_keepMove = false;
    }

    public abstract void process();
    public abstract void sentinel();
    public abstract void attack();
    public abstract void defend();
}
