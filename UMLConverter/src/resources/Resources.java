package resources;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.scene.image.Image;

/**
 * Resources class which takes care of files.
 * 
 * @author Lhomme Lucien
 */
public abstract class Resources {
	
	/**
	 * Get the right bundle in the folder "resources/bundles".
	 * @param name String
	 * @return ResourceBundle
	 */
	public static ResourceBundle getBundle(String name) {
		return ResourceBundle.getBundle(String.format("Resources/bundles.%s", name));
	}

	/**
	 * Get the right bundle in the folder "resources/bundles" for a specific language.
	 * @param name String
	 * @param locale Locale
	 * @return ResourceBundle
	 */
	public static ResourceBundle getBundle(String name, Locale locale) {
		return ResourceBundle.getBundle(String.format("Resources/bundles.%s", name), locale);
	}
	
	/**
	 * Get the right image in the folder "resources/images".
	 * @param name String
	 * @return Image
	 */
	public static Image getImage(String name) {
		return new Image(Resources.class.getResourceAsStream("images/" + name));
	}
	
	/**
	 * Get the url of the path name.
	 * @param name String
	 * @return URL
	 */
	public static URL getURL(String name) {
		return Resources.class.getResource(name);
	}

	/**
	 * Check if the file has the good extension.
	 * @param file File
	 * @param ext String
	 * @return boolean
	 */
	private static boolean hasGoodExtension(File file) {
		return hasGoodExtension(file.getName());
	}

	/**
	 * Check if the file name has the good extension.
	 * @param fileName String
	 * @param ext String
	 * @return boolean
	 */
	private static boolean hasGoodExtension(String fileName) {
		return ".txt".equalsIgnoreCase(fileName.substring(fileName.lastIndexOf('.')));
	}
	
	public static String chargeStringFile(File file) {
		StringBuilder lines = new StringBuilder();
		if (file.isFile()) {
			if (hasGoodExtension(file)) {
				if (file.canRead()) {
					try (BufferedReader br = new BufferedReader(
							new InputStreamReader(new FileInputStream(file), "UTF8"))) {
						String line;
						while ((line = br.readLine()) != null) {
							lines.append(line + "\n");
						}
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return lines.toString();
	}
}
