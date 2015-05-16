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
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

public class BananaAssetManager extends AssetManager{
    public BananaAssetManager() {
        super();
        this.loadSync();
    }

    public void loadData(String rootPath, java.lang.Class type, String[] filters){
        FileHandle[] fileArray = Gdx.files.internal(rootPath).list();
        for(FileHandle f : fileArray){
            if(f.isDirectory())
                this.loadData(f.path(), type, filters);
            else{
                for(String filter : filters){
                    if(f.extension().equals(filter)){
                        Gdx.app.log("LOAD", f.path());
                        this.load(f.path(), type);
                        break;
                    }
                }
            }
        }
    }

    public void loadImg(){
        this.loadData("img", Texture.class, new String[]{"png","jpg","jpeg"});
    }

    public void loadSound(){
        this.loadData("sound", Sound.class, new String[]{"mp3","wma"});
    }

    public void loadMusic(){
        this.loadData("music", Music.class, new String[]{"mp3","wma"});
    }

    public void loadSync(){
        this.loadImg();
        this.loadSound();
        this.loadMusic();
        this.finishLoading();
    }
}
