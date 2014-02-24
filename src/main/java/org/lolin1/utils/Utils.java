package org.lolin1.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.net.ssl.HttpsURLConnection;

import org.lolin1.models.champion.Champion;

public abstract class Utils {

	private static final String API_KEY = "c236fa80-b223-487d-8358-41a4e94276b6", // TODO
																					// Replace
																					// this
																					// from
																					// the
																					// public
																					// repo
																					// upon
																					// deployment
																					// for
																					// "YOUR_API_KEY_HERE"
			API_PARAM_NAME = "api_key",
			IMAGES_DIR_NAME = "img",
			BUSTS_DIR_NAME = "champion",
			SPELLS_DIR_NAME = "spell",
			PASSIVES_DIR_NAME = "passive";

	public static final void createImagesDirectory() {
		try {
			Utils.delete(Utils.IMAGES_DIR_NAME);
			Files.createDirectories(Paths.get(Utils.IMAGES_DIR_NAME,
					Utils.BUSTS_DIR_NAME));
			Files.createDirectories(Paths.get(Utils.IMAGES_DIR_NAME,
					Utils.SPELLS_DIR_NAME));
			Files.createDirectories(Paths.get(Utils.IMAGES_DIR_NAME,
					Utils.PASSIVES_DIR_NAME));
		} catch (FileAlreadyExistsException ex) {
			// It's fine, just avoid retrying because probably everything is
			// already created
			return;
		} catch (IOException e) {
			e.printStackTrace(System.err);
		}
	}

	/**
	 * Recursively deletes a file or folder (even if it's not empty). This
	 * method firsts checks for the existence of the specified file, and then
	 * recursively deletes it, and, if any, all of its sub-folders.
	 * 
	 * @param pathToFile
	 *            {@link String} The file to be deleted.
	 * @return A boolean representing the success of the operation.
	 */
	public static boolean delete(String pathToFile) {
		File file, fileAux;

		if ((file = new File(pathToFile)).exists()) {
			try {
				for (String target : file.list()) {
					if ((fileAux = new File(target)).isFile()) {
						if (!fileAux.delete()) {
							return false;
						}
					} else {
						Utils.delete(pathToFile + "/" + target);
					}
				}
			} catch (NullPointerException ex) {
			}
			return file.delete();
		}
		return false;
	}

	public static void downloadChampionBustImage(Champion champion,
			String imagesUrl) {
		String championImageName = champion.getImageName();
		Utils.downloadFile(imagesUrl + Utils.BUSTS_DIR_NAME + "/"
				+ championImageName, Paths.get(Utils.IMAGES_DIR_NAME,
				Utils.BUSTS_DIR_NAME, championImageName));
	}

	public static void downloadChampionPassiveImage(Champion champion,
			String imagesUrl) {
		String passiveImageName = champion.getPassiveImageName();
		Utils.downloadFile(imagesUrl + Utils.PASSIVES_DIR_NAME + "/"
				+ passiveImageName, Paths.get(Utils.IMAGES_DIR_NAME,
				Utils.PASSIVES_DIR_NAME, passiveImageName));
	}

	/**
	 * Note that, while this method is quite efficient, it's limited to files
	 * <16 MB.
	 * 
	 * @param url
	 * @param pathToFileToSaveTo
	 */
	private static void downloadFile(String url, Path pathToFileToSaveTo) {
		URL website = null;
		System.out.println(url);
		try {
			website = new URL(url.replaceAll(" ", "%20"));
		} catch (MalformedURLException e) {
			e.printStackTrace(System.err);
		}
		try (ReadableByteChannel rbc = Channels
				.newChannel(website.openStream());
				FileOutputStream fos = new FileOutputStream(
						pathToFileToSaveTo.toString());) {
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		} catch (IOException e) {
			e.printStackTrace(System.err);
		}
	}

	public static final synchronized String performRiotGet(String url) {
		StringBuilder ret = new StringBuilder();
		URL obj = null;
		try {
			obj = new URL(url + ((url.contains("=")) ? "&" : "?")
					+ Utils.API_PARAM_NAME + "=" + Utils.API_KEY);
		} catch (MalformedURLException e) {
			e.printStackTrace(System.err);
		}

		HttpsURLConnection con = null;
		try {
			con = (HttpsURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
		} catch (IOException e) {
			e.printStackTrace(System.err);
		}

		try {
			if (con.getResponseCode() == 200) {

				BufferedReader in = new BufferedReader(new InputStreamReader(
						con.getInputStream()));
				String inputLine;
				StringBuilder response = new StringBuilder();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();

				ret.append(response.toString());
			}
		} catch (IOException e) {
			e.printStackTrace(System.err);
		}

		return ret.toString();
	}
}
