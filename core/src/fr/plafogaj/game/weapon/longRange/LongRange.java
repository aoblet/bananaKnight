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
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import fr.plafogaj.game.character.Character;
import fr.plafogaj.game.character.enemy.Enemy;
import fr.plafogaj.game.character.player.Player;
import fr.plafogaj.game.engine.TiledMapConfig;
import fr.plafogaj.game.engine.TiledMapOrthographicCamera;
import fr.plafogaj.game.weapon.Weapon;
import fr.plafogaj.screens.Game;

import java.util.LinkedList;

/** Abstract class which represents a distant weapon such as Catapult, Arc, etc */
public abstract class LongRange extends Weapon {
    /** Bullets list shot by the weapon */
    protected LinkedList<Bullet> m_bullets;
    /** Force direction of the weapon */
    protected Vector2 m_forceDirection;
    /** Velocity for angle movement of weapon */
    protected float m_velocityAngleMove;
    /** Gravity force operating on long range bullets */
    public static float GRAVITY = -0.003f;

    public LongRange(Character c, Sound soundToPlay, FileHandle fileTexture, TiledMapConfig mapConfig) {
        super(c, soundToPlay, fileTexture, mapConfig);
        m_bullets = new LinkedList<Bullet>();
        m_forceDirection = new Vector2(0.25f,0.1f);
        m_velocityAngleMove = (float)Math.toRadians(0.5);
    }

    public void moveAngle(boolean down){
        Vector2 force = m_forceDirection.cpy();
        float angle = (m_forceDirection.x > 0 && down) || (m_forceDirection.x < 0 && !down) ? m_velocityAngleMove*-1 : m_velocityAngleMove;
        float cosAngle = (float)Math.cos(angle), sinAngle = (float)Math.sin(angle);
        float x = force.x, y = force.y;
        force.x = x*cosAngle - sinAngle*y;

        if((m_forceDirection.x > 0 && force.x < 0) ||
           (m_forceDirection.x < 0 && force.x > 0))
            return;
        force.y = x*sinAngle + y*cosAngle;
        m_forceDirection = force.cpy();
    }

    public void render(float deltaTime, Batch toRender, TiledMapOrthographicCamera camera, TiledMapTileLayer collisionLayer,
                       LinkedList charactersEnemy) {
        super.render(deltaTime, toRender, camera, collisionLayer, charactersEnemy);

        // bullets
        Bullet bTmp;
        Vector2 cellCollideCoord;
        Character enemyHit;

        for (int i = 0; i<m_bullets.size(); ++i) {
            cellCollideCoord = null;
            enemyHit = null;
            bTmp = m_bullets.get(i);

            bTmp.update();
            if((cellCollideCoord = bTmp.getCellCollision(collisionLayer)) != null)
                collisionLayer.setCell((int)cellCollideCoord.x, (int)cellCollideCoord.y, null);
            if((cellCollideCoord != null) || !bTmp.isInScreen(camera)){
                m_bullets.remove(bTmp);
                bTmp.dispose();
            }
            else{
                enemyHit = this.getCharacterHit(m_character.getEnemies(), bTmp.getPosition(), bTmp.getSize());

                if(enemyHit != null){
                    enemyHit.impactLife(m_force);
                    m_bullets.remove(bTmp);
                    bTmp.dispose();
                    m_soundHitTarget.play();
                }
                else
                    bTmp.render(toRender);
            }
        }
    }

    public LinkedList<Bullet> getBullets() {
        return m_bullets;
    }

    public Vector2 getAngle() {
        return m_forceDirection;
    }

    public void setAngle(Vector2 angle) {
        this.m_forceDirection = angle;
    }

    public float getVelocityAngleMove() {
        return m_velocityAngleMove;
    }

    public void setVelocityAngleMove(float velocityAngleMove) {
        this.m_velocityAngleMove = velocityAngleMove;
    }

    @Override
    public void dispose(){
        super.dispose();
    }
}