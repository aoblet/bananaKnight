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

import com.badlogic.gdx.math.MathUtils;
import fr.plafogaj.game.character.enemy.Enemy;
import fr.plafogaj.game.character.player.Player;

public class MediumIA extends IA{
    public MediumIA(Player player, Enemy owner) {
        super(player, owner);
    }

    @Override
    public void process() {
        m_distancePlayerPoint = m_owner.getPosition().cpy().mulAdd(m_player.getPosition(),-1);
        m_isPlayerRight = m_distancePlayerPoint.x <= 0;

        m_distancePlayerPoint.x *= m_distancePlayerPoint.x < 0 ? -1 : 1;
        m_distancePlayerPoint.y *= m_distancePlayerPoint.y < 0 ? -1 : 1;

        if(m_owner.isTerminatorMode())
            this.attack();
        else if(m_distancePlayerPoint.x > 30 || m_distancePlayerPoint.y > 20)
            return;
        else if(m_distancePlayerPoint.x > 13 || m_distancePlayerPoint.y > 10)
            this.sentinel();
        else
            this.attack();

        if(m_owner.isCollidedX()){
            m_owner.jump();
        }
    }

    @Override
    public void sentinel() {
        m_owner.setIsDiscoverPlayer(false);
        if(!m_keepMove){
            int rightDirection = MathUtils.randomSign();
            m_owner.setPositionToGo(MathUtils.random(m_owner.getPosition().x, m_owner.getPosition().x + (2 * rightDirection)));
            m_owner.getMoveVector().x = 0;
            m_keepMove = true;
        }
        else
            m_owner.walk(m_owner.getPositionToGo() >= m_owner.getPosition().x, 0.03f);

        if(MathUtils.isEqual(m_owner.getPosition().x, m_owner.getPositionToGo(), 0.3f))
            m_keepMove = false;
    }

    @Override
    public void attack() {
        m_owner.setIsFacesRight(m_isPlayerRight);

        if(!m_owner.isDiscoverPlayer())
            m_owner.setIsDiscoverPlayer(true);
        if(m_distancePlayerPoint.x > 1)
            m_owner.walk(m_isPlayerRight, m_owner.max_move_velocity);

        if(m_distancePlayerPoint.x < 2 && m_distancePlayerPoint.y < 3)
            m_owner.hitSword();
        else
            m_owner.throwBanana();

        if(m_owner.getPosition().y < m_player.getPosition().y - 2)
            m_owner.jump();
    }

    @Override
    public void defend() {

    }
}
