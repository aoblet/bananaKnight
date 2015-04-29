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

package fr.plafogaj.game.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import fr.plafogaj.game.character.animation.SpriteTextureConfig;
import fr.plafogaj.game.engine.TiledMapConfig;
import fr.plafogaj.game.engine.TiledMapOrthographicCamera;
import fr.plafogaj.game.weapon.Weapon;

public abstract class Character {
    protected Vector2 m_position;

    /**
     * width, height of character
     */
    protected Vector2 m_size;

    protected SpriteTextureConfig m_spriteTextureConfig;
    protected Texture m_spriteTexture;

    protected Animation m_walkAnimation;
    protected Animation m_standAnimation;
    protected Animation m_jumpAnimation;
    protected Animation m_runAnimation;
    protected TextureRegion m_currentFrameTexture;

    protected StateMove m_currentStateMove;
    protected String m_name;
    protected Weapon m_weapon;
    protected int m_life;

    protected Music m_walkSound;
    protected Sound m_standSound;
    protected Sound m_jumpSound;
    protected Sound m_runSound;
    protected Sound m_groundSound;

    protected float m_stateTime;
    protected boolean m_isFacesRight;
    protected boolean m_isGrounded;
    protected Vector2 m_moveVector;

    protected TiledMapConfig m_mapConfig;

    public Character(Vector2 pos, TiledMapConfig mapConfig, FileHandle spriteConfigFile, StateMove move, String name,
                     Weapon weapon, int life){
        m_mapConfig = mapConfig;
        m_spriteTextureConfig = new SpriteTextureConfig(spriteConfigFile);
        m_spriteTexture = m_spriteTextureConfig.getTexture();
        m_size = m_spriteTextureConfig.getSizeOneCell().scl(TiledMapConfig.TILE_UNIT_SCALE);

        m_position = pos;
        m_currentStateMove = move;
        m_name = name;
        m_weapon = weapon;
        m_life = life;
        m_currentFrameTexture = null;
        m_isFacesRight = true;

        m_walkAnimation = m_spriteTextureConfig.getWalkAnimationConfig().getAnimation();
        m_standAnimation = m_spriteTextureConfig.getStandAnimationConfig().getAnimation();
        m_runAnimation = m_spriteTextureConfig.getRunAnimationConfig().getAnimation();
        m_jumpAnimation = m_spriteTextureConfig.getJumpAnimationConfig().getAnimation();

        m_walkSound = Gdx.audio.newMusic(Gdx.files.internal("sound/player/walk.mp3"));
        m_stateTime = 0;
        m_moveVector = new Vector2(0,0);
        m_isGrounded = false;

        m_weapon.setCharacter(this);
    }

    public Rectangle getTiledCollided(Vector2 start, Vector2 end, TiledMapTileLayer collisionLayer){
        for(int x = (int)start.x; x<=end.x; ++x ){
            for(int y = (int)start.y ; y<=end.y ; ++y){
                if(collisionLayer.getCell(x,y) != null)
                    return new Rectangle(x,y,1,1); // 1 == 1unit
            }
        }
        return null;
    }

    public Rectangle getRectangle(){
        return new Rectangle(m_position.x, m_position.y, m_size.x, m_size.y);
    }

    public abstract void render(float deltaTime, Batch toRender, TiledMapOrthographicCamera camera, TiledMapTileLayer collision);

    public void render(float deltaTime, Batch batchToRender){
        m_stateTime += deltaTime;
        switch (m_currentStateMove){
            case Standing:
                m_currentFrameTexture = m_standAnimation.getKeyFrame(m_stateTime, true);
                break;
            case Walking:
                m_currentFrameTexture = m_walkAnimation.getKeyFrame(m_stateTime, true);
                break;
            case Running:
                m_currentFrameTexture = m_runAnimation.getKeyFrame(m_stateTime, true);
                break;
            case Jumping:
                m_currentFrameTexture = m_jumpAnimation.getKeyFrame(m_stateTime, true);
                break;
        }

        if(m_isFacesRight)
            batchToRender.draw(m_currentFrameTexture, m_position.x, m_position.y, m_size.x, m_size.y);
        else
            batchToRender.draw(m_currentFrameTexture, m_position.x + m_size.x, m_position.y, -m_size.x, m_size.y);
    }

    public abstract void update(TiledMapTileLayer collisionLayer);

    public Vector2 getPosition() {
        return m_position;
    }

    public Vector2 getSize() {
        return m_size;
    }

    public SpriteTextureConfig getSpriteTextureConfig() {
        return m_spriteTextureConfig;
    }

    public Texture getSpriteTexture() {
        return m_spriteTexture;
    }

    public Animation getWalkAnimation() {
        return m_walkAnimation;
    }

    public Animation getStandAnimation() {
        return m_standAnimation;
    }

    public Animation getJumpAnimation() {
        return m_jumpAnimation;
    }

    public Animation getRunAnimation() {
        return m_runAnimation;
    }

    public TextureRegion getCurrentFrameTexture() {
        return m_currentFrameTexture;
    }

    public boolean isFacesRight() {
        return m_isFacesRight;
    }

    public StateMove getCurrentStateMove() {
        return m_currentStateMove;
    }

    public String getName() {
        return m_name;
    }

    public Weapon getWeapon() {
        return m_weapon;
    }

    public int getLife() {
        return m_life;
    }

    public Music getWalkSound() {
        return m_walkSound;
    }

    public Sound getStandSound() {
        return m_standSound;
    }

    public Sound getJumpSound() {
        return m_jumpSound;
    }

    public Sound getRunSound() {
        return m_runSound;
    }

    public float getStateTime() {
        return m_stateTime;
    }

    public void dispose(){
        m_spriteTexture.dispose();
    }
}
