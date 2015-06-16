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
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import fr.plafogaj.game.character.Character;
import fr.plafogaj.game.character.StateMove;
import fr.plafogaj.game.character.enemy.IA.IA;
import fr.plafogaj.game.character.enemy.IA.MediumIA;
import fr.plafogaj.game.character.player.Player;
import fr.plafogaj.game.engine.TiledMapConfig;
import fr.plafogaj.screens.Game;

import java.util.LinkedList;

public abstract class Enemy extends Character{
    protected IA m_IA_module;

    public static float MAX_MOVE_VELOCITY = 0.1f;
    public static float GRAVITY = -0.008f;
    public float max_move_velocity;
    protected float m_positionToGo;
    protected Sound m_soundAttack;
    protected boolean m_isDiscoverPlayer;
    protected boolean m_isTerminatorMode;

    public Enemy(Vector2 position, TiledMapConfig mapConfig, FileHandle spriteConfigFile, String name, int life,
                 LinkedList<Character> players){
        super(position, mapConfig, spriteConfigFile, StateMove.Standing, name, life, players);

        max_move_velocity = MathUtils.random(0.05f, 0.1f);
        m_soundAttack = Game.ASSET_MANAGER.get("sound/player/attack.mp3");
        m_IA_module = new MediumIA(players, this);
        m_isDiscoverPlayer = false;
        m_isTerminatorMode = false;
    }

    @Override
    public void render(float deltaTime, Batch toRender){
        this.m_IA_module.process();
        this.update(deltaTime);
        super.render(deltaTime, toRender);
        m_arc.render(deltaTime, toRender, m_mapConfig.getCamera(), m_mapConfig.getCollisionLayer(), m_enemies);
        m_sword.render(deltaTime, toRender, m_mapConfig.getCamera(), m_mapConfig.getCollisionLayer(), m_enemies);
    }

    @Override
    public void impactLife(int impact){
        super.impactLife(impact);
        m_isTerminatorMode = true;
        if(m_isGrounded){
            m_moveVector.y = Enemy.MAX_MOVE_VELOCITY*4f;
        }
    }

    @Override
    public void update(float deltaTime){
        super.update(deltaTime);
        m_moveVector.y += Enemy.GRAVITY;
        this.processCollision();
        this.applyPersistMove();
    }

    public boolean isDiscoverPlayer() {
        return m_isDiscoverPlayer;
    }

    public void setIsDiscoverPlayer(boolean isDiscoverPlayer) {
        this.m_isDiscoverPlayer = isDiscoverPlayer;
        if(isDiscoverPlayer)
            m_soundAttack.play();
    }

    public boolean isTerminatorMode() {
        return m_isTerminatorMode;
    }

    public float getPositionToGo() {
        return m_positionToGo;
    }

    public void setPositionToGo(float positionToGo) {
        this.m_positionToGo = positionToGo;
    }
}
