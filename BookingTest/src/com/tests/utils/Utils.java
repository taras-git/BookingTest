package com.tests.utils;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * The Class Utils. Holds the static methods to work with Property files, and
 * retrieves the value based on appropriate key.
 *
 * @author Taras Tymchyshyn
 */
public class Utils {

	/**
	 * Gets the absolute path of the working directory.
	 *
	 * @return the absolute path
	 */
	private static Path getAbsolutePath() {
		return Paths.get("").toAbsolutePath();
	}

	public static Path getAbsolutePath(String path) {
		return Paths.get(path).toAbsolutePath();
	}

	/**
	 * Gets the value from *.ini file, based on appropriate key.
	 *
	 * @param key
	 *            the key
	 * @param path
	 *            the path to the ini file
	 * @return the value
	 */
	public static String getIniFileValue(String key, String path) {
		Properties prop = new Properties();
		InputStream input = null;
		String value = null;

		try {

			input = new FileInputStream(getAbsolutePath() + path);
			// load a properties file
			prop.load(input);
			// get the property value
			value = prop.getProperty(key);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		// return the value in case of success
		return value;
	}

	public static void pasteFromClipboard() throws AWTException, InterruptedException {
		Thread.sleep(500);
		Robot r = new Robot();
		r.keyPress(KeyEvent.VK_CONTROL); // Pasting clipboard content using CTRL-V
		r.keyPress(KeyEvent.VK_V);
		r.keyRelease(KeyEvent.VK_V);
		r.keyRelease(KeyEvent.VK_CONTROL);
		Thread.sleep(500);
		r.keyPress(KeyEvent.VK_ENTER); // confirm by pressing Enter in the end
		r.keyRelease(KeyEvent.VK_ENTER);
	}

	public static void copyToClipboard(String text) {
		StringSelection data = new StringSelection(text);
		Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
		cb.setContents(data, data);
	}

	public static void print(String text) {
		System.out.println(">>>>>>>>>> " + text);
	}
}
