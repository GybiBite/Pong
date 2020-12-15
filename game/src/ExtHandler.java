
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import ext_bot.ExtensionBot;

public class ExtHandler extends ExtLoader {

	static File bot = new File(".\\ExtBot.jar");

	public static boolean loadBot() {
		try {
			addToClassPath(bot);
			ExtensionBot.check();
			return true;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | IOException | NoClassDefFoundError e) {
			System.out.println("ERROR: Encountered error loading extension \"1 player mode\"");
			e.printStackTrace();
			System.out.println("-----");
			return false;
		}
	}
}