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

package fr.plafogaj.game.character.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import fr.plafogaj.game.character.Character;
import fr.plafogaj.game.character.StateMove;
import fr.plafogaj.game.engine.TiledMapConfig;
import fr.plafogaj.game.engine.TiledMapOrthographicCamera;
import fr.plafogaj.game.weapon.Weapon;
import fr.plafogaj.game.weapon.longRange.Arc;
import fr.plafogaj.game.weapon.longRange.LongRange;
import fr.plafogaj.game.weapon.shortRange.Sword;

public class Player extends Character{
    private static float MAX_VELOCITY = 0.1f;
    private static float GRAVITY= -0.004f;
    private Armor m_armor;

    public Player(Vector2 pos, TiledMapConfig mapConfig, FileHandle spriteConfigFile, StateMove move, String name, Weapon weapon, int life){
        super(pos, mapConfig, spriteConfigFile, move, name, weapon, life);
    }

    public Player(Vector2 pos, TiledMapConfig mapConfig){
        this(pos, mapConfig, Gdx.files.internal("img/sprite/player/config.json"), StateMove.Walking, "Player", new Arc(), 10);
    }

    @Override
    public void render(float deltaTime, Batch toRender, TiledMapOrthographicCamera camera, TiledMapTileLayer collision){
        this.update(collision);
        super.render(deltaTime, toRender);
        m_weapon.update(m_position);
        m_weapon.render(deltaTime, toRender, camera, collision);
    }

    @Override
    public void update(TiledMapTileLayer collisionLayer){

        // weapon
        if(m_weapon instanceof LongRange){
            if(Gdx.input.isKeyPressed(Input.Keys.U)){
                ((LongRange) m_weapon).upAngle();
            }

            if(Gdx.input.isKeyPressed(Input.Keys.I)){
                ((LongRange) m_weapon).downAngle();
            }
        }


        if(Gdx.input.isKeyPressed(Input.Keys.H))
            m_weapon.hit();

        //movement
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.RIGHT) ||
           Gdx.input.isKeyPressed(Input.Keys.Q) || Gdx.input.isKeyPressed(Input.Keys.D)){
            if(m_isGrounded)
                m_currentStateMove = StateMove.Walking;
            m_isFacesRight = (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D));
            m_moveVector.x = Player.MAX_VELOCITY *(m_isFacesRight ? 1 : -1);
        }
        else{
            m_moveVector.x = 0;
            m_currentStateMove = StateMove.Standing;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.SPACE) && m_isGrounded){
            m_currentStateMove = StateMove.Jumping;
            m_moveVector.y = Player.MAX_VELOCITY;
            m_isGrounded = false;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)){
            if(m_isGrounded){
                m_currentStateMove = StateMove.Running;
                m_moveVector.scl(1.8f);
            }
        }


        m_moveVector.y += Player.GRAVITY;

        //collision
        Vector2 start = new Vector2(), end = new Vector2();
        Rectangle tmpCollide;

        end.x = start.x = m_position.x + m_moveVector.x;
        if(m_moveVector.x >0)
            end.x = (start.x += m_size.x);
        start.y = m_position.y+0.2f;
        end.y = start.y + m_size.y;

        if(this.getTiledCollided(start, end, collisionLayer) == null)
            m_position.x += m_moveVector.x;

        start.x = m_position.x;
        end.x = m_position.x + m_size.x;
        start.y = end.y = m_position.y + m_moveVector.y;
        if(m_moveVector.y > 0)
            start.y = (end.y += m_size.y);

        tmpCollide = this.getTiledCollided(start, end, collisionLayer);

        if(tmpCollide != null){
            if(m_moveVector.y > 0 ){
                m_position.y = tmpCollide.y - m_size.y;
            }
            else{
                m_position.y = tmpCollide.y + 1;
                m_isGrounded = true;
            }
            m_moveVector.y = 0;
        }

        m_position.y += m_moveVector.y;
        if(m_position.y<0) {
            m_isGrounded = true;
            m_position.y = 0;
            m_life = 0;
        }
        if(m_position.x <0)
            m_position.x = 0;

//        if(m_isGrounded){
//            if((int)m_moveVector.x == 0)
//                m_currentStateMove = StateMove.Standing;
//            else
//                m_currentStateMove = StateMove.Walking;
//        }
//        else
//            m_currentStateMove = StateMove.Jumping;
    }
}
