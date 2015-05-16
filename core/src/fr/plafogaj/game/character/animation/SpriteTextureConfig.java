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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import fr.plafogaj.screens.Game;

public class SpriteTextureConfig {
    private FileHandle m_fileConfig;
    private FileHandle m_textureFile;
    private Texture m_texture;

    private Vector2 m_colRow;
    private Vector2 m_sizeOneCell;

    private AnimationConfig m_hitAnimationConfig;
    private AnimationConfig m_throwAnimationConfig;
    private AnimationConfig m_walkAnimationConfig;
    private AnimationConfig m_jumpAnimationConfig;
    private AnimationConfig m_runAnimationConfig;
    private AnimationConfig m_standAnimationConfig;

    private TextureRegion[][] m_sheetTextureRegion;

    public SpriteTextureConfig(FileHandle fileConfig){
        // base config
        JsonValue root = new JsonReader().parse(m_fileConfig = fileConfig);
        m_textureFile = Gdx.files.internal(root.get("rootPath").asString() + root.get("textureFile").asString());
        m_texture = Game.ASSET_MANAGER.get(m_textureFile.path());
        m_colRow = new Vector2(root.get("colRow").get("x").asInt(), root.get("colRow").get("y").asInt());
        m_sizeOneCell = new Vector2(m_texture.getWidth() / m_colRow.x, m_texture.getHeight() / m_colRow.y );

        // animation config
        JsonValue texturesRegion = root.get("texturesRegion");
        m_sheetTextureRegion = TextureRegion.split(m_texture, (int)m_sizeOneCell.x, (int)m_sizeOneCell.y);
        m_hitAnimationConfig = new AnimationConfig(m_sheetTextureRegion, texturesRegion.get("hit"));
        m_throwAnimationConfig = new AnimationConfig(m_sheetTextureRegion, texturesRegion.get("throw"));
        m_standAnimationConfig = new AnimationConfig(m_sheetTextureRegion, texturesRegion.get("stand"));
        m_walkAnimationConfig = new AnimationConfig(m_sheetTextureRegion, texturesRegion.get("walk"));
        m_jumpAnimationConfig = new AnimationConfig(m_sheetTextureRegion, texturesRegion.get("jump"));
        m_runAnimationConfig = new AnimationConfig(m_sheetTextureRegion, texturesRegion.get("run"));
    }

    public Texture getTexture() {
        return m_texture;
    }

    public Vector2 getSizeOneCell() {
        return m_sizeOneCell;
    }

    public FileHandle getFileConfig() {
        return m_fileConfig;
    }

    public FileHandle getTextureFile() {
        return m_textureFile;
    }

    public AnimationConfig getWalkAnimationConfig() {
        return m_walkAnimationConfig;
    }

    public AnimationConfig getJumpAnimationConfig() {
        return m_jumpAnimationConfig;
    }

    public AnimationConfig getRunAnimationConfig() {
        return m_runAnimationConfig;
    }

    public AnimationConfig getStandAnimationConfig() {
        return m_standAnimationConfig;
    }

    public AnimationConfig getHitAnimationConfig() {
        return m_hitAnimationConfig;
    }

    public AnimationConfig getThrowAnimationConfig() {
        return m_throwAnimationConfig;
    }

    public TextureRegion[][] getSheetTextureRegion() {
        return m_sheetTextureRegion;
    }

    public Vector2 getColRow() {
        return m_colRow;
    }
}
