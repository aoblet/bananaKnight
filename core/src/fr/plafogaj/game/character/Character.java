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
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import fr.plafogaj.game.character.animation.SpriteTextureConfig;
import fr.plafogaj.game.character.enemy.Enemy;
import fr.plafogaj.game.engine.TiledMapConfig;
import fr.plafogaj.game.weapon.longRange.Arc;
import fr.plafogaj.game.weapon.shortRange.Sword;
import fr.plafogaj.screens.Game;

import java.util.Collection;
import java.util.LinkedList;

public abstract class Character {
    protected Vector2 m_position;

    /**
     * width, height of character
     */
    protected Vector2 m_size;

    protected SpriteTextureConfig m_spriteTextureConfig;
    protected Texture m_spriteTexture;

    protected Animation m_hitAnimation;
    protected Animation m_throwAnimation;
    protected Animation m_walkAnimation;
    protected Animation m_standAnimation;
    protected Animation m_jumpAnimation;
    protected Animation m_runAnimation;
    protected TextureRegion m_currentFrameTexture;

    protected StateMove m_currentStateMove;
    protected String m_name;
    protected Arc m_arc;
    protected Sword m_sword;

    protected int m_life;

    protected Sound m_walkSound;
    protected Sound m_standSound;
    protected Sound m_jumpSound;
    protected Sound m_runSound;
    protected Sound m_landSound;

    protected float m_stateTime;
    protected float m_stateTimeAnimationWeapon;
    protected boolean m_isFacesRight;
    protected boolean m_isGrounded;
    protected boolean m_isCollidedX;
    protected boolean m_isCollidedY;
    protected boolean m_isDoubleJump;
    protected StateMove m_persistMove;

    protected Vector2 m_moveVector;

    protected TiledMapConfig m_mapConfig;

    protected static float MAX_MOVE_VELOCITY = 0.1f;
    protected LinkedList<Character> m_enemies;
    protected Rectangle m_rectangle;


    public Character(Vector2 pos, TiledMapConfig mapConfig, FileHandle spriteConfigFile, StateMove move, String name,
                     int life, LinkedList<Character> enemies){
        m_mapConfig = mapConfig;
        m_spriteTextureConfig = new SpriteTextureConfig(spriteConfigFile);
        m_spriteTexture = m_spriteTextureConfig.getTexture();
        m_size = m_spriteTextureConfig.getSizeOneCell().scl(TiledMapConfig.TILE_UNIT_SCALE);

        m_position = pos;
        m_currentStateMove = move;
        m_name = name;
        m_life = life;
        m_currentFrameTexture = null;
        m_isFacesRight = true;
        m_enemies = enemies == null? new LinkedList<Character>() : enemies;

        m_hitAnimation = m_spriteTextureConfig.getHitAnimationConfig().getAnimation();
        m_throwAnimation = m_spriteTextureConfig.getThrowAnimationConfig().getAnimation();
        m_walkAnimation = m_spriteTextureConfig.getWalkAnimationConfig().getAnimation();
        m_standAnimation = m_spriteTextureConfig.getStandAnimationConfig().getAnimation();
        m_runAnimation = m_spriteTextureConfig.getRunAnimationConfig().getAnimation();
        m_jumpAnimation = m_spriteTextureConfig.getJumpAnimationConfig().getAnimation();
        m_throwAnimation.setFrameDuration(0.03f);
        m_hitAnimation.setFrameDuration(0.08f);
        m_runAnimation.setFrameDuration(0.08f);

        m_walkSound = Game.ASSET_MANAGER.get("sound/player/walk.mp3");
        m_jumpSound = Game.ASSET_MANAGER.get("sound/player/jump.mp3");
        m_landSound = Game.ASSET_MANAGER.get("sound/player/land.mp3");

        m_stateTime = 0;
        m_stateTimeAnimationWeapon = 0;
        m_moveVector = new Vector2(0,0);
        m_isGrounded = false;

        m_arc = new Arc(m_mapConfig);
        m_sword = new Sword(m_mapConfig);

        m_arc.setCharacter(this);
        m_sword.setCharacter(this);
        m_rectangle = new Rectangle(0,0, m_size.x, m_size.y);
    }

    public boolean isInScreen(OrthographicCamera camera){
        float hHalf = camera.viewportHeight/2, wHalf = camera.viewportWidth/2;
        return !(m_position.x > camera.position.x +wHalf || m_position.x+m_size.x < camera.position.x -wHalf
                || m_position.y > camera.position.y + hHalf || m_position.y+ m_size.y < camera.position.y - hHalf);

    }

    public void jump(){
        boolean jump = false;

        if(m_isGrounded) {
            jump = true;
            m_isGrounded = false;
        }
        else if(!m_isDoubleJump)
            m_isDoubleJump = jump = true;

        if(jump){
            m_currentStateMove = StateMove.Jumping;
            m_moveVector.y = Character.MAX_MOVE_VELOCITY* (m_isDoubleJump ? 3f : 4f);
            m_isGrounded = false;
            m_jumpSound.play(0.2f);
        }
    }

    public void run(){
        m_currentStateMove = StateMove.Running;
        m_moveVector.x *= 1.8f;
    }

    public void setEnemies(LinkedList<Character> enemies) {
        this.m_enemies = enemies;
    }

    public void walk(boolean rightDirection){
        this.walk(rightDirection, Character.MAX_MOVE_VELOCITY);
    }
    public void walk(boolean rightDirection, float speed){
        m_isFacesRight = rightDirection;
        if(m_isGrounded)
            m_currentStateMove = StateMove.Walking;
        m_moveVector.x = speed *(m_isFacesRight ? 1 : -1);
    }

    public void hitSword(){
        m_sword.hit(m_mapConfig.getCollisionLayer());
        m_currentStateMove = StateMove.Hitting;
    }

    public void throwBanana(){
        m_arc.hit(m_mapConfig.getCollisionLayer());
        m_currentStateMove = StateMove.Throwing;
    }

    public Arc getArc() {
        return m_arc;
    }

    public Sword getSword() {
        return m_sword;
    }

    public void processCollision(){

        //collision
        Vector2 start = new Vector2(), end = new Vector2();
        Rectangle tmpCollide;

        end.x = start.x = m_position.x + m_moveVector.x;
        if(m_moveVector.x >0)
            end.x = (start.x += m_size.x);
        start.y = m_position.y+0.2f;
        end.y = start.y + m_size.y;

        if(this.getTiledCollided(start, end, m_mapConfig.getCollisionLayer()) == null){
            m_isCollidedX = false;
            m_position.x += m_moveVector.x;
        }
        else
            m_isCollidedX = true;

        start.x = m_position.x;
        end.x = m_position.x + m_size.x;
        start.y = end.y = m_position.y + m_moveVector.y;
        if(m_moveVector.y > 0)
            start.y = (end.y += m_size.y);

        tmpCollide = this.getTiledCollided(start, end, m_mapConfig.getCollisionLayer());

        if(tmpCollide != null){
            if(m_moveVector.y > 0 ){
                m_position.y = tmpCollide.y - m_size.y;
            }
            else{
                m_position.y = tmpCollide.y + 1;
                if(!m_isGrounded)
                    m_landSound.play(0.2f);
                m_isGrounded = true;
            }
            m_moveVector.y = 0;
        }

        m_position.y += m_moveVector.y;
        if(m_position.y<0) {
            if(!m_isGrounded)
                m_landSound.play(0.2f);

            m_isGrounded = true;
            m_position.y = 0;
        }
        else{
            if(tmpCollide == null )
                m_isGrounded = false;
        }
        if(m_position.x <0)
            m_position.x = 0;
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
        return m_rectangle;
    }

    public void render(float deltaTime, Batch batchToRender){
        m_stateTime += deltaTime;
        m_rectangle.setPosition(m_position.x, m_position.y);
        switch (m_currentStateMove){
            case Hitting:
                m_currentFrameTexture = m_hitAnimation.getKeyFrame(m_stateTimeAnimationWeapon, true);
                break;
            case Throwing:
                m_currentFrameTexture = m_throwAnimation.getKeyFrame(m_stateTimeAnimationWeapon, true);
                break;
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

    public void update(float deltaTime){
        m_stateTimeAnimationWeapon += deltaTime;
        this.updatePersistMove();
    }

    protected void updatePersistMove(){
        m_persistMove = null;
        if(m_currentStateMove == StateMove.Hitting && !m_hitAnimation.isAnimationFinished(m_stateTimeAnimationWeapon))
            m_persistMove = StateMove.Hitting;
        else if(m_currentStateMove == StateMove.Throwing && !m_throwAnimation.isAnimationFinished(m_stateTimeAnimationWeapon))
            m_persistMove = StateMove.Throwing;
        else
            m_stateTimeAnimationWeapon = 0;
    }

    protected void applyPersistMove(){
        if(m_persistMove != null)
            m_currentStateMove = m_persistMove;
    }

    public void impactLife(int impact){
        m_life -= impact;
    }

    public Vector2 getPosition() {
        return m_position;
    }

    public Vector2 getSize() {
        return m_size;
    }

    public boolean isFacesRight() {
        return m_isFacesRight;
    }

    public String getName() {
        return m_name;
    }

    public int getLife() {
        return m_life;
    }

    public Vector2 getMoveVector() {
        return m_moveVector;
    }

    public void setPosition(Vector2 position) {
        this.m_position = position;
    }

    public void setIsGrounded(boolean isGrounded) {
        this.m_isGrounded = isGrounded;
    }

    public void setMoveVector(Vector2 moveVector) {
        this.m_moveVector = moveVector;
    }

    public void setCurrentStateMove(StateMove currentStateMove) {
        this.m_currentStateMove = currentStateMove;
    }

    public TiledMapConfig getMapConfig() {
        return m_mapConfig;
    }

    public boolean isCollidedX() {
        return m_isCollidedX;
    }

    public boolean isCollidedY() {
        return m_isCollidedY;
    }

    public void setIsFacesRight(boolean isFacesRight) {
        this.m_isFacesRight = isFacesRight;
    }

    public void setLife(int life) {
        this.m_life = life;
    }

    public boolean isAlive(){
        return m_life > 1 ;
    }

    public boolean isGrounded() {
        return m_isGrounded;
    }

    public boolean isDoubleJump() {
        return m_isDoubleJump;
    }

    public void setIsDoubleJump(boolean isDoubleJump) {
        this.m_isDoubleJump = isDoubleJump;
    }

    public LinkedList<Character> getEnemies() {
        return m_enemies;
    }

    public void dispose(){
        //  m_spriteTexture.dispose();
    }
}
