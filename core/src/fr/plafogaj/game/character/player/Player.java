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

package fr.plafogaj.game.character.player;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import fr.plafogaj.game.character.Character;
import fr.plafogaj.game.character.StateMove;
import fr.plafogaj.game.weapon.Weapon;

public class Player extends Character implements InputProcessor{

    public Player(Vector2 pos, FileHandle spriteConfigFile, StateMove move, String name, Weapon weapon, int life){
        super(pos,spriteConfigFile,move,name,weapon,life);
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
