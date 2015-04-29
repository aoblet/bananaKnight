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

package fr.plafogaj.game.weapon;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import fr.plafogaj.game.character.Character;
import fr.plafogaj.game.engine.TiledMapOrthographicCamera;

public abstract class Weapon {
    protected Vector2 m_position;
    protected fr.plafogaj.game.character.Character m_character;
    protected float m_cadencyHitLimit;
    protected float m_stateTimeCadencyHit;
    protected boolean m_isHitable;

    public Weapon(Vector2 pos, Character c){
        m_position = new Vector2(pos);
        m_character = c;
        m_cadencyHitLimit = 0.3f;
        m_isHitable = true;
    }

    public Vector2 getPosition() {
        return m_position;
    }

    public void setPosition(Vector2 position) {
        this.m_position = position;
    }

    public float getCadencyHit() {
        return m_cadencyHitLimit;
    }

    public void setCadencyHit(float cadencyHit) {
        this.m_cadencyHitLimit = cadencyHit;
    }

    public Character getCharacter() {
        return m_character;
    }

    public void setCharacter(Character character) {
        this.m_character = character;
    }

    public void update(Vector2 position){
        m_position.x = position.x;
        m_position.y = position.y;
    }

    public void render(float deltaTime, Batch toRender, TiledMapOrthographicCamera camera, TiledMapTileLayer collisionLayer){
        m_stateTimeCadencyHit += deltaTime;
    }

    public void hit(){
        if(m_stateTimeCadencyHit < m_cadencyHitLimit){
            m_isHitable = false;
            return;
        }
        m_stateTimeCadencyHit = 0;
        m_isHitable = true;
    }

    public abstract void dispose();
}
