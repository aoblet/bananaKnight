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

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import fr.plafogaj.game.character.Character;
import fr.plafogaj.game.character.enemy.Enemy;
import fr.plafogaj.game.engine.TiledMapConfig;
import fr.plafogaj.game.engine.TiledMapOrthographicCamera;
import fr.plafogaj.screens.Game;

import java.util.LinkedList;

/** Base weapon class */
public abstract class Weapon {
    protected Texture m_texture;
    protected TextureRegion m_textureRegion;
    /** Sound played when hit*/
    protected Sound m_soundHit;
    protected Sound m_soundHitTarget;
    /** Position of weapon in word space. Points to character vector position object (reference) */
    protected Vector2 m_position;
    /** Character to whom the weapon belongs */
    protected Character m_character;
    /** The weapon gets a cadence of usability. Update in hit and render methods */
    protected float m_minBreakTime_cadenceHit;
    protected float m_elapsedTime_cadenceHit;
    protected boolean m_isUsable;

    protected TiledMapConfig m_mapConfig;

    /** Force of the weapon */
    protected int m_force;

    /** The size of the weapon */
    protected Vector2 m_size;

    public Weapon(Character c, Sound soundToPlay, FileHandle fileTexture, TiledMapConfig mapConfig){
        m_position = c != null ? c.getPosition() : new Vector2(0,0);
        m_character = c;
        m_minBreakTime_cadenceHit = 0.5f;
        m_isUsable = true;
        m_soundHit = soundToPlay;
        m_soundHitTarget = Game.ASSET_MANAGER.get("sound/weapon/punch.mp3");
        m_texture = Game.ASSET_MANAGER.get(fileTexture.path());
        m_size = new Vector2(m_texture.getWidth()* TiledMapConfig.TILE_UNIT_SCALE, m_texture.getHeight()*TiledMapConfig.TILE_UNIT_SCALE);
        m_textureRegion = new TextureRegion(m_texture);
        m_mapConfig = mapConfig;
        m_force = 1;
    }

    /**
     * Render method in game loop, job delegates to subclass
     * @param deltaTime time between two frames
     * @param toRender batch to render the weapon
     * @param camera for viewport data
     * @param collisionLayer collision layer detection
     */
    public void render(float deltaTime, Batch toRender, TiledMapOrthographicCamera camera, TiledMapTileLayer collisionLayer,
                       LinkedList charactersEnemy){
        m_elapsedTime_cadenceHit += deltaTime;
        m_isUsable = m_elapsedTime_cadenceHit > m_minBreakTime_cadenceHit;
    }


    /** Method triggered when the character hit */
    public void hit(TiledMapTileLayer collisionLayer){
        if(!(m_isUsable = m_elapsedTime_cadenceHit > m_minBreakTime_cadenceHit))
            return;
        m_soundHit.play(0.2f);
        m_elapsedTime_cadenceHit = 0;
    }

    public Vector2 getPosition() {
        return m_position;
    }

    public void setPosition(Vector2 position) {
        this.m_position = position;
    }

    public float getMinBreakTime_cadenceHit() {
        return m_minBreakTime_cadenceHit;
    }

    public void setMinBreakTime_cadenceHit(float minBreakTime_cadenceHit) {
        this.m_minBreakTime_cadenceHit = minBreakTime_cadenceHit;
    }

    public float getElapsedTime_cadenceHit() {
        return m_elapsedTime_cadenceHit;
    }

    public void setElapsedTime_cadenceHit(float elapsedTime_cadenceHit) {
        this.m_elapsedTime_cadenceHit = elapsedTime_cadenceHit;
    }

    public Character getCharacter() {
        return m_character;
    }

    public void setCharacter(Character character) {
        this.m_character = character;
        m_position = character.getPosition(); //pointer so no need to update position in game loop
    }

    public Character getCharacterHit(LinkedList<Character> enemies, Vector2 pos, Vector2 size){
        Rectangle self = new Rectangle(pos.x, pos.y, size.x, size.y);
        Rectangle enemy = new Rectangle();
        for(Character e: enemies){
            enemy.set(e.getPosition().x, e.getPosition().y, e.getSize().x, e.getSize().y);
            if(self.overlaps(enemy))
                return e;
        }
        return null;
    }

    /** Free resources */
    public void dispose(){
//        m_soundHit.dispose();
//        m_texture.dispose();
    }
}
