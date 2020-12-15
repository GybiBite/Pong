
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class ExtLoader {
	public static void addToClassPath(File jarFile)
			throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		addUrlMethod.invoke(addUrlThis, jarFile.toURI().toURL());
	}

	private static ClassLoader addUrlThis;
	private static Method addUrlMethod;

	static {
		addUrlThis = ClassLoader.getSystemClassLoader();
		try {
			addUrlMethod = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		addUrlMethod.setAccessible(true);
	}
}
