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
package fr.plafogaj.game.engine;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import fr.plafogaj.game.character.player.Player;

/**
 * Class which is used for the camera scrolling based on the player.
 * When the player is not in the boxFixed, we scroll the camera.
 * Representation:
 * --------------------------------
 * --------------------------------
 * ---+++++++++++------------------
 * ---+++++++++++------------------
 * ---+++++++++++------------------
 * ---+++++++++++------------------
 * --------------------------------
 * --------------------------------
 */

public class TiledMapOrthographicCamera extends OrthographicCamera {
    /**
     * Coordinates of the box used to check if the player is inside, in the world space.
     */
    private Vector2 m_boxFixedWord;
    private Vector2 m_boxFixedSizeWord;

    /**
     * Coordinates of the box in viewport space.
     * Variable as member: avoid to compute at each frame. It is just computed when we change viewport.
     */
    private Vector2 m_boxFixedViewport;
    private OrthographicCamera m_cameraParallax;

    public TiledMapOrthographicCamera(float viewportWidth, float viewportHeight) {
        super(viewportWidth, viewportHeight);
        m_boxFixedWord = new Vector2();
        m_boxFixedViewport = new Vector2();
        this.updateBoxViewport(viewportWidth, viewportHeight);
        m_cameraParallax = new OrthographicCamera(viewportWidth,viewportHeight);
    }

    public TiledMapOrthographicCamera() {
        this(0, 0);
    }

    public void updateBoxViewport(float viewportWidth, float viewportHeight){
        m_boxFixedViewport.x = viewportWidth/3f;
        m_boxFixedViewport.y = viewportHeight/5f;
    }

    @Override
    public void setToOrtho(boolean yDown, float viewportWidth, float viewportHeight){
        super.setToOrtho(yDown, viewportWidth, viewportHeight);
        this.updateBoxViewport(viewportWidth, viewportHeight);
        m_cameraParallax.setToOrtho(yDown, viewportWidth, viewportHeight);
    }

    public void updateScrolling(Player playerToTrack){
        //position.x==camera position==(0,0) of word space
        m_boxFixedWord.x = position.x - m_boxFixedViewport.x;
        m_boxFixedWord.y = position.y - m_boxFixedViewport.y;

        // x axis
        if(playerToTrack.getPosition().x >= position.x)
            position.x = playerToTrack.getPosition().x;
        else if(playerToTrack.getPosition().x <= m_boxFixedWord.x)
            position.x = playerToTrack.getPosition().x + m_boxFixedViewport.x;

        // y axis
        if(playerToTrack.getPosition().y <= position.y - m_boxFixedViewport.y)
            position.y = playerToTrack.getPosition().y + m_boxFixedViewport.y;
        else if(playerToTrack.getPosition().y >= position.y + m_boxFixedViewport.y)
            position.y = playerToTrack.getPosition().y - m_boxFixedViewport.y;

        //clamp
        position.x = position.x <= viewportWidth/2 ? viewportWidth/2 : position.x;
        position.y = position.y <= viewportHeight/2 ? viewportHeight/2 : position.y;

        super.update();
    }

    public OrthographicCamera getParallaxCamera(float xParallax, float yParallax){
        m_cameraParallax.position.x = (position.x*xParallax);
        m_cameraParallax.position.y = (position.y*yParallax);
        m_cameraParallax.update();
        return m_cameraParallax;
    }
}
