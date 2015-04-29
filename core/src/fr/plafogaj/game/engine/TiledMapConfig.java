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

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

/**
 * Class which facilites use and access of TiledMap
 * Contains the different tiledMapsLayer common to every level
 */
public class TiledMapConfig{
    private TiledMap m_map;
    private TiledMapTileLayer m_backgroundFarLayer;
    private TiledMapTileLayer m_backgroundNearLayer;
    private TiledMapTileLayer m_foregroundLayer;
    /**
     * could be related to decor
     */
    private TiledMapTileLayer m_collisionLayer;

    private OrthogonalTiledMapRenderer m_backgroundFarLayerRenderer;
    private OrthogonalTiledMapRenderer m_backgroundNearLayerRenderer;
    private OrthogonalTiledMapRenderer m_foregroundLayerRenderer;
    private OrthogonalTiledMapRenderer m_collisionLayerRenderer;

    private TiledMapOrthographicCamera m_camera;

    public static float TILE_UNIT_SCALE = 1/32f;

    public TiledMapConfig(String tmxFile){
        m_map = new TmxMapLoader().load(tmxFile);

        m_backgroundFarLayer = (TiledMapTileLayer)m_map.getLayers().get("backgroundFarLayer");
        m_backgroundNearLayer = (TiledMapTileLayer)m_map.getLayers().get("backgroundNearLayer");
        m_collisionLayer = (TiledMapTileLayer)m_map.getLayers().get("collisionLayer");
        m_foregroundLayer = (TiledMapTileLayer)m_map.getLayers().get("foregroundLayer");

        m_collisionLayerRenderer = new OrthogonalTiledMapRenderer(m_map, TILE_UNIT_SCALE);
        m_backgroundFarLayerRenderer = new OrthogonalTiledMapRenderer(m_map, TILE_UNIT_SCALE);
        m_backgroundNearLayerRenderer = new OrthogonalTiledMapRenderer(m_map, TILE_UNIT_SCALE);
        m_foregroundLayerRenderer = new OrthogonalTiledMapRenderer(m_map, TILE_UNIT_SCALE   );

        m_camera = new TiledMapOrthographicCamera();
        m_camera.setToOrtho(false, 40, 20);
        m_camera.update();
    }

    public TiledMap getMap() {
        return m_map;
    }

    public TiledMapTileLayer getBackgroundFarLayer() {
        return m_backgroundFarLayer;
    }

    public TiledMapTileLayer getBackgroundNearLayer() {
        return m_backgroundNearLayer;
    }

    public TiledMapTileLayer getCollisionLayer() {
        return m_collisionLayer;
    }

    public TiledMapTileLayer getForegroundLayer() {
        return m_foregroundLayer;
    }

    public OrthogonalTiledMapRenderer getBackgroundFarLayerRenderer() {
        return m_backgroundFarLayerRenderer;
    }

    public OrthogonalTiledMapRenderer getBackgroundNearLayerRenderer() {
        return m_backgroundNearLayerRenderer;
    }

    public OrthogonalTiledMapRenderer getForegroundLayerRenderer() {
        return m_foregroundLayerRenderer;
    }

    public OrthogonalTiledMapRenderer getCollisionLayerRenderer() {
        return m_collisionLayerRenderer;
    }

    public TiledMapOrthographicCamera getCamera() {
        return m_camera;
    }
}
