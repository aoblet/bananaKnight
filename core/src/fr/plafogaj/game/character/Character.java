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

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import fr.plafogaj.game.character.animation.SpriteTextureConfig;
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
    protected boolean m_isFacesRight;

    protected StateMove m_currentStateMove;
    protected String m_name;
    protected Weapon m_weapon;
    protected int m_life;

    protected Sound m_walkSound;
    protected Sound m_standSound;
    protected Sound m_jumpSound;
    protected Sound m_runSound;

    protected float m_stateTime;

    public Character(Vector2 pos, FileHandle spriteConfigFile, StateMove move, String name, Weapon weapon, int life){
        m_spriteTextureConfig = new SpriteTextureConfig(spriteConfigFile);
        m_spriteTexture = m_spriteTextureConfig.getTexture();
        m_size = m_spriteTextureConfig.getSizeOneCell();

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

        m_stateTime = 0;
    }

    public Rectangle getRectangle(){
        return new Rectangle(m_position.x, m_position.y, m_size.x, m_size.y);
    }

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

    public void dispose(){
        m_spriteTexture.dispose();
    }
}
