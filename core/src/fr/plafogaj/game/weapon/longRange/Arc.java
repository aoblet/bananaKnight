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

import com.badlogic.gdx.math.Vector2;
import fr.plafogaj.game.character.player.Player;

public class Arc extends LongRange {

    public Arc(Vector2 pos, Player p){
        super(pos, p);
    }

    public Arc(){
        this(new Vector2(0,0), null);
    }

    @Override
    public void hit(){
        super.hit();

        if(!m_isHitable)
            return;
        if(!m_character.isFacesRight())
            m_angle.x = -Math.abs(m_angle.x);
        else
            m_angle.x = Math.abs(m_angle.x);
        m_bullets.add(new Bullet(m_position, m_angle, LongRange.GRAVITY));
    }

    @Override
    public void dispose() {}
}
