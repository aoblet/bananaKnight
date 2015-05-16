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

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * Class which facilites use and access of TiledMap
 * Contains the different tiledMapsLayer common to every level
 */
public class TiledMapConfig{
    private TiledMap m_map;

    private TiledMapTileLayer m_fgLayer;
    private TiledMapTileLayer m_middleLayer;
    private TiledMapTileLayer m_bg_1Layer;
    private TiledMapTileLayer m_bg_2Layer;
    private TiledMapTileLayer m_bg_3Layer;
    private TiledMapTileLayer m_bg_4Layer;
    private TiledMapTileLayer m_collisionLayer;

    private MapObjects m_enemiesObjects;
    private TiledMapTileLayer m_enemiesLayerFake;
    private EllipseMapObject m_endObject;
    private Vector2 m_endObjectCoord;

    private OrthogonalTiledMapRenderer m_mapRenderer;

    private TiledMapOrthographicCamera m_camera;

    public static float TILE_UNIT_SCALE = 1/32f;

    public static int[] ENEMIES_LAYER = {9};
    public static int[] FG_LAYER = {8};
    public static int[] MIDDLE_LAYER = {7};
    public static int[] COLLISION_LAYER ={6};
    public static int[] BG1_LAYER = {5};
    public static int[] BG2_LAYER = {4};
    public static int[] BG3_LAYER = {3};
    public static int[] BG4_LAYER = {2};
    public static int[] BG5_LAYER = {1};
    public static int[] SKY_LAYER = {0};

    public TiledMapConfig(String tmxFile){
        m_map = new TmxMapLoader().load(tmxFile);

        m_fgLayer = (TiledMapTileLayer)m_map.getLayers().get("fg");
        m_middleLayer = (TiledMapTileLayer)m_map.getLayers().get("middle");
        m_collisionLayer = (TiledMapTileLayer)m_map.getLayers().get("collisionLayer");
        m_bg_1Layer = (TiledMapTileLayer)m_map.getLayers().get("bg_1");
        m_bg_2Layer = (TiledMapTileLayer)m_map.getLayers().get("bg_2");
        m_bg_3Layer = (TiledMapTileLayer)m_map.getLayers().get("bg_3");
        m_bg_4Layer = (TiledMapTileLayer)m_map.getLayers().get("bg_4");
        m_enemiesObjects = m_map.getLayers().get("enemies").getObjects();
        m_enemiesLayerFake = new TiledMapTileLayer(m_fgLayer.getWidth(), m_fgLayer.getHeight(),32,32);
        m_endObject = (EllipseMapObject)m_map.getLayers().get("end").getObjects().get(0);
        m_endObjectCoord = new Vector2(m_endObject.getEllipse().x*TILE_UNIT_SCALE, m_endObject.getEllipse().y*TILE_UNIT_SCALE);

        m_mapRenderer= new OrthogonalTiledMapRenderer(m_map, TILE_UNIT_SCALE);

        m_camera = new TiledMapOrthographicCamera();
        m_camera.setToOrtho(false, 40, 20);
        m_camera.update();
    }

    public MapObjects getEnemiesObjects() {
        return m_enemiesObjects;
    }

    public TiledMap getMap() {
        return m_map;
    }

    public TiledMapTileLayer getLayer(int index){
        return (TiledMapTileLayer)m_map.getLayers().get(index);
    }
    public TiledMapTileLayer getFgLayer() {
        return m_fgLayer;
    }

    public TiledMapTileLayer getMiddleLayer() {
        return m_middleLayer;
    }

    public TiledMapTileLayer getBg_1Layer() {
        return m_bg_1Layer;
    }

    public TiledMapTileLayer getBg_2Layer() {
        return m_bg_2Layer;
    }

    public TiledMapTileLayer getBg_3Layer() {
        return m_bg_3Layer;
    }

    public TiledMapTileLayer getBg_4Layer() {
        return m_bg_4Layer;
    }

    public TiledMapTileLayer  getCollisionLayer() {
        return m_collisionLayer;
    }

    public TiledMapTileLayer getEnemiesLayerFake() {
        return m_enemiesLayerFake;
    }

    public OrthogonalTiledMapRenderer getMapRenderer() {
        return m_mapRenderer;
    }

    public TiledMapOrthographicCamera getCamera() {
        return m_camera;
    }

    public Vector2 getEndObjectCoord() {
        return m_endObjectCoord;
    }
}
