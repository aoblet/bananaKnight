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

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import fr.plafogaj.game.character.player.Player;
import fr.plafogaj.game.engine.TiledMapOrthographicCamera;
import fr.plafogaj.game.weapon.Weapon;

import java.util.LinkedList;

public abstract class LongRange extends Weapon {
    protected LinkedList<Bullet> m_bullets;
    protected Vector2 m_angle;
    protected float m_velocityAngleMove;

    public static float GRAVITY = -0.003f;

    public LongRange(Vector2 pos, Player p) {
        super(pos,p);
        m_bullets = new LinkedList<Bullet>();
        m_angle = new Vector2(0.2f,0.1f);
        m_velocityAngleMove = 0.01f;
    }

    public void upAngle(){
        Vector2 tmp = new Vector2(m_angle);

        // we clamp if angle >= 90
        if(Math.acos(tmp.add(0, m_velocityAngleMove).dot(new Vector2(0, 1)) / m_angle.len()) >20)
            return;
        m_angle.add(0, m_velocityAngleMove);
        m_angle.scl((1 / m_angle.len()));

    }

    public void downAngle(){
//        Vector2 tmp = new Vector2(m_angle);
//
//        // we clamp if angle <= -90
//        if(Math.acos(tmp.add(0, m_velocityAngleMove).dot(new Vector2(0,1))/m_angle.len()) < -Math.toRadians(75))
//            return;
//        m_angle.add(0, -m_velocityAngleMove);
//        m_angle.scl((1/m_angle.len()));
    }

    public void render(float deltaTime, Batch toRender, TiledMapOrthographicCamera camera, TiledMapTileLayer collisionLayer) {
        super.render(deltaTime, toRender, camera, collisionLayer);

        Bullet bTmp;
        Vector2 cellCollideCoord;

        for (int i = 0; i<m_bullets.size(); ++i) {
            cellCollideCoord = null;
            bTmp = m_bullets.get(i);

            bTmp.update();
            if((cellCollideCoord = bTmp.getCellCollision(collisionLayer)) != null)
                collisionLayer.setCell((int)cellCollideCoord.x, (int)cellCollideCoord.y, null);
            if((cellCollideCoord != null) || !bTmp.isInScreen(camera))
                m_bullets.remove(bTmp);
            else
                bTmp.render(toRender);
        }
    }

    public LinkedList<Bullet> getBullets() {
        return m_bullets;
    }

    public Vector2 getAngle() {
        return m_angle;
    }

    public void setAngle(Vector2 angle) {
        this.m_angle = angle;
    }

    public float getVelocityAngleMove() {
        return m_velocityAngleMove;
    }

    public void setVelocityAngleMove(float velocityAngleMove) {
        this.m_velocityAngleMove = velocityAngleMove;
    }
}