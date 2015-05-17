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
package fr.plafogaj.game.artefact;

import com.badlogic.gdx.math.Vector2;
import fr.plafogaj.game.character.Character;

public class ArtifactLife extends Artifact {
    private int m_lifeToAdd;

    public ArtifactLife(int m_lifeToAdd, Vector2 pos) {
        super("img/game/life.png", pos, "sound/player/life.mp3");
        this.m_lifeToAdd = m_lifeToAdd;
    }

    @Override
    public void processOnCharacter(Character c){
        super.processOnCharacter(c);
        c.impactLife(-m_lifeToAdd);
    }
}
