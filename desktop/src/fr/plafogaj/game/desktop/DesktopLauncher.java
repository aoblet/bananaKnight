package fr.plafogaj.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import fr.plafogaj.game.BananaKnight;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = BananaKnight.WIDTH;
		config.height = BananaKnight.HEIGHT;
		config.title = BananaKnight.TITLE;
		config.resizable = false;
		new LwjglApplication(new BananaKnight(), config);
	}
}
