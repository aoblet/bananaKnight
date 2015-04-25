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
package fr.plafogaj.game.character.animation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;

/**
 * Class which facilitates creation of an movement animation related to a character
 */

public class AnimationConfig {
    private TextureRegion[][] m_sheetTextureRegion;
    private ArrayList<Vector2> m_coordInTexture;
    private Animation m_animation;

    public AnimationConfig(TextureRegion[][] sheet, JsonValue coordData){
        m_sheetTextureRegion = sheet;
        m_coordInTexture = new ArrayList<Vector2>();
        this.configCoord(coordData);
        this.configAnimation();
    }

    private void configCoord(JsonValue data){
        for(int i=0; i<data.size ; ++i)
            m_coordInTexture.add(new Vector2(data.get(i).get("x").asInt(),data.get(i).get("y").asInt()));
    }

    private void configAnimation(){
        TextureRegion[] tmp = new TextureRegion[m_coordInTexture.size()];
        int i = -1;
        for(Vector2 v: m_coordInTexture)
            tmp[++i] = m_sheetTextureRegion[(int)v.x][(int)v.y];
        m_animation = new Animation(0.25f, tmp);
    }

    public TextureRegion[][] getSheetTextureRegion() {
        return m_sheetTextureRegion;
    }

    public ArrayList<Vector2> getCoordInTexture() {
        return m_coordInTexture;
    }

    public Animation getAnimation() {
        return m_animation;
    }
}
