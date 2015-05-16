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
import fr.plafogaj.game.character.enemy.Enemy;
import fr.plafogaj.game.engine.TiledMapConfig;
import fr.plafogaj.game.engine.TiledMapOrthographicCamera;
import fr.plafogaj.game.weapon.longRange.Arc;
import fr.plafogaj.screens.Game;

import java.util.LinkedList;

public class Player extends Character{
    protected static float MAX_MOVE_VELOCITY = 0.1f;
    protected static float GRAVITY = -0.008f;
    protected Armor m_armor;

    public Player(Vector2 pos, TiledMapConfig mapConfig, FileHandle spriteConfigFile, StateMove move, String name, int life){
        super(pos, mapConfig, spriteConfigFile, move, name, life);
        m_enemies = Game.m_enemiesList;
    }

    public Player(Vector2 pos, TiledMapConfig mapConfig){
        this(pos, mapConfig, Gdx.files.internal("img/sprite/player/configNinja.json"), StateMove.Walking, "Player", 10);
    }

    @Override
    public void render(float deltaTime, Batch toRender){
        this.update(deltaTime);
        super.render(deltaTime, toRender);
        m_arc.render(deltaTime, toRender, m_mapConfig.getCamera(), m_mapConfig.getCollisionLayer(), m_enemies);
        m_sword.render(deltaTime, toRender, m_mapConfig.getCamera(), m_mapConfig.getCollisionLayer(), m_enemies);
    }

    @Override
    public void update(float deltaTime){
        super.update(deltaTime);

        //movement input
        if(Gdx.input.isKeyPressed(Input.Keys.Q) || Gdx.input.isKeyPressed(Input.Keys.D))
            this.walk(Gdx.input.isKeyPressed(Input.Keys.D));
        else{
            m_moveVector.x = 0;
            m_currentStateMove = StateMove.Standing;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
            this.jump();
        if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT))
            this.run();

        if(!m_isGrounded)
            m_currentStateMove = StateMove.Jumping;

        m_moveVector.y += Player.GRAVITY;

        this.processCollision();
        if(m_isGrounded)
            m_isDoubleJump = false;

        // weapon
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
            this.hitSword();
        else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            this.throwBanana();

        //banana angle
        if(Gdx.input.isKeyPressed(Input.Keys.UP))
            m_arc.moveAngle(false);
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN))
            m_arc.moveAngle(true);

        this.applyPersistMove();
    }
}
