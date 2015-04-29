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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import fr.plafogaj.game.character.player.Player;

public class GameLevelConfig {
    private FileHandle m_levelFile;
    private FileHandle m_mapTmxFile;
    private FileHandle m_configGameFile;

    private JsonValue m_charactersConfig;
    private JsonValue m_artefactsConfig;

    private TiledMapConfig m_tileMapConfig;

    private Player m_player;

    public GameLevelConfig(FileHandle levelFile) throws NullPointerException{
        this.setUpConfig(levelFile);
    }

    private void setUpConfig(FileHandle levelFile) throws NullPointerException{
        JsonValue root = new JsonReader().parse(m_levelFile = levelFile);
        m_mapTmxFile = Gdx.files.internal(root.get("mapTmxFile").asString());
        m_configGameFile = Gdx.files.internal(root.get("configGameFile").asString());

        JsonValue rootConfig = new JsonReader().parse(m_configGameFile);
        m_charactersConfig = rootConfig.get("charactersConfig");
        m_artefactsConfig = rootConfig.get("artefactsConfig");

        m_tileMapConfig = new TiledMapConfig(m_mapTmxFile.path());
        m_player = new Player(new Vector2(20,20), m_tileMapConfig);
    }

    public Player getPlayer() {
        return m_player;
    }

    public TiledMapConfig getTileMapConfig() {
        return m_tileMapConfig;
    }

    public FileHandle getLevelFile() {
        return m_levelFile;
    }

    public FileHandle getMapTmxFile() {
        return m_mapTmxFile;
    }

    public FileHandle getConfigGameFile() {
        return m_configGameFile;
    }

    public JsonValue getCharactersConfig() {
        return m_charactersConfig;
    }

    public JsonValue getArtefactsConfig() {
        return m_artefactsConfig;
    }
}
