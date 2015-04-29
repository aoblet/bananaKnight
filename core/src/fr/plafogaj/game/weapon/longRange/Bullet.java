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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import fr.plafogaj.game.engine.TiledMapConfig;
import fr.plafogaj.game.engine.TiledMapOrthographicCamera;

public class Bullet {
    protected Vector2 m_position;
    protected Vector2 m_moveVector;
    protected float m_gravity;
    protected Texture m_texture;
    protected Vector2 m_size;

    public Bullet(Vector2 position, Vector2 move, float gravity){
        m_position = new Vector2(position);
        m_moveVector = new Vector2(move);
        m_gravity = gravity;
        m_texture = new Texture(Gdx.files.internal("img/weapon/bullet.png"));
        m_size = new Vector2(m_texture.getWidth(), m_texture.getHeight()).scl(TiledMapConfig.TILE_UNIT_SCALE);
    }

    public Bullet(){
        this(new Vector2(0,0), new Vector2(0,0), 0);
    }

    public void render(Batch toRender){
        if(m_moveVector.x > 0)
            toRender.draw(m_texture, m_position.x, m_position.y, m_size.x, m_size.y);
        else
            toRender.draw(m_texture, m_position.x + m_size.x, m_position.y, -m_size.x, m_size.y);
    }

    public void update(){
        m_moveVector.y += m_gravity;
        m_position.add(m_moveVector);
    }

    public Vector2 getCellCollision(TiledMapTileLayer collisionLayer){
        if(m_moveVector.x > 0){
            if(collisionLayer.getCell((int)(m_position.x + m_size.x), (int)(m_position.y)) != null)
                return new Vector2(m_position.x + m_size.x, m_position.y);
            if(collisionLayer.getCell((int)(m_position.x + m_size.x), (int)(m_position.y + m_size.y)) != null)
                return new Vector2(m_position.x + m_size.x, m_position.y+m_size.y);
        }
        else{
            if(collisionLayer.getCell((int)(m_position.x), (int)(m_position.y)) != null)
                return new Vector2(m_position.x, m_position.y);
            if(collisionLayer.getCell((int)(m_position.x), (int)(m_position.y + m_size.y)) != null)
                return new Vector2(m_position.x, m_position.y+m_size.y);
        }
        return null;
    }

    public boolean isInScreen(TiledMapOrthographicCamera camera){
        float viewportWidthHalf = camera.viewportWidth/2f;
        float viewportHeightHalf = camera.viewportHeight/2f;

        if(m_position.x + m_size.x < camera.position.x - viewportWidthHalf || m_position.x > camera.position.x + viewportWidthHalf ||
           m_position.y > camera.position.y + viewportHeightHalf || m_position.y + m_size.y < camera.position.y - viewportHeightHalf)
            return false;
        return true;
    }

    public Vector2 getPosition() {
        return m_position;
    }

    public Vector2 getMoveVector() {
        return m_moveVector;
    }

    public float getGravity() {
        return m_gravity;
    }

    public Texture getTexture() {
        return m_texture;
    }

    public Vector2 getSize() {
        return m_size;
    }

    public void dispose(){
        m_texture.dispose();
    }
}
