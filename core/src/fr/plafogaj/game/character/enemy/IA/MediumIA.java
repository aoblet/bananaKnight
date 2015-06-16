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
import com.badlogic.gdx.utils.Timer;
import fr.plafogaj.game.character.Character;
import fr.plafogaj.game.character.enemy.Enemy;
import fr.plafogaj.game.character.player.Player;
import fr.plafogaj.game.weapon.Weapon;

import java.util.LinkedList;

public class MediumIA extends IA{
    public MediumIA(LinkedList<Character> players, Enemy owner) {
        super(players, owner);
        m_owner.getArc().setMinBreakTime_cadenceHit(m_owner.getArc().getMinBreakTime_cadenceHit()*1.5f);
        m_owner.getSword().setMinBreakTime_cadenceHit(m_owner.getSword().getMinBreakTime_cadenceHit()*1.5f);
    }

    @Override
    public void process() {
        m_distancePlayerPoint = m_owner.getPosition().cpy().mulAdd(m_player.getPosition(),-1);
        m_isPlayerRight = m_distancePlayerPoint.x <= 0;

        m_distancePlayerPoint.x *= m_distancePlayerPoint.x < 0 ? -1 : 1;
        m_distancePlayerPoint.y *= m_distancePlayerPoint.y < 0 ? -1 : 1;

        if(m_owner.isGrounded())
            m_owner.setIsDoubleJump(false);

        if(m_owner.isCollidedX()){
            if(m_owner.isDoubleJump())
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        m_owner.jump();
                    }
                },0.3f);
            else
                m_owner.jump();
        }

        if(m_owner.isTerminatorMode())
            this.attack();
        else if(m_distancePlayerPoint.x > 40 || m_distancePlayerPoint.y > 30)
            return;
        else if(m_distancePlayerPoint.x > 20 || m_distancePlayerPoint.y > 20)
            this.sentinel();
        else
            this.attack();
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

        if(m_owner.getPosition().y < m_player.getPosition().y - 5)
            m_owner.jump();
    }

    @Override
    public void defend() {

    }
}
