package org.lolin1.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;

import org.lolin1.control.Controller;
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

	public static final void createImagesDirectory(String realm) {
		try {
			Utils.delete(Paths.get(Utils.IMAGES_DIR_NAME, realm).toString());
			Files.createDirectories(Paths.get(Utils.IMAGES_DIR_NAME, realm,
					Utils.BUSTS_DIR_NAME));
			Files.createDirectories(Paths.get(Utils.IMAGES_DIR_NAME, realm,
					Utils.SPELLS_DIR_NAME));
			Files.createDirectories(Paths.get(Utils.IMAGES_DIR_NAME, realm,
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

	public static File downloadChampionBustImage(String realm,
			Champion champion, String imagesUrl) {
		String championImageName = champion.getImageName();
		return Utils.downloadFile(imagesUrl + Utils.BUSTS_DIR_NAME + "/"
				+ championImageName, Paths.get(Utils.IMAGES_DIR_NAME, realm,
				Utils.BUSTS_DIR_NAME, championImageName));
	}

	public static File downloadChampionPassiveImage(String realm,
			Champion champion, String imagesUrl) {
		String passiveImageName = champion.getPassiveImageName();
		return Utils.downloadFile(imagesUrl + Utils.PASSIVES_DIR_NAME + "/"
				+ passiveImageName, Paths.get(Utils.IMAGES_DIR_NAME, realm,
				Utils.PASSIVES_DIR_NAME, passiveImageName));
	}

	public static File[] downloadChampionSpellImages(String realm,
			Champion champion, String imagesUrl) {
		String[] spellImageNames = champion.getSpellImageNames();
		File[] ret = new File[spellImageNames.length];
		int i = 0;
		for (String spellImageName : spellImageNames) {
			ret[i] = Utils.downloadFile(imagesUrl + Utils.SPELLS_DIR_NAME + "/"
					+ spellImageName, Paths.get(Utils.IMAGES_DIR_NAME, realm,
					Utils.SPELLS_DIR_NAME, spellImageName));
			i++;
		}
		return ret;
	}

	// TODO What happens when the update is trying to be performed but there's
	// no connection?

	/**
	 * Note that, while this method is quite efficient, it's limited to files
	 * <16 MB.
	 * 
	 * @param url
	 * @param pathToFileToSaveTo
	 */
	private static File downloadFile(String url, Path pathToFileToSaveTo) {
		URL website = null;
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

		return new File(pathToFileToSaveTo.toString());
	}

	public static File getFile(String realm, int imageType, String name) {
		switch (imageType) {
		case Controller.IMAGE_TYPE_SPELL:
			return new File(Paths.get(Utils.IMAGES_DIR_NAME, realm,
					Utils.BUSTS_DIR_NAME, name).toString());
		case Controller.IMAGE_TYPE_PASSIVE:
			return new File(Paths.get(Utils.IMAGES_DIR_NAME, realm,
					Utils.PASSIVES_DIR_NAME, name).toString());
		case Controller.IMAGE_TYPE_BUST:
			return new File(Paths.get(Utils.IMAGES_DIR_NAME, realm,
					Utils.SPELLS_DIR_NAME, name).toString());
		}
		return null;// Should never happen
	}

	public static String getFileMD5(File file) {
		String ret = null;
		MessageDigest messageDigest = null;
		byte[] auxArray = new byte[1024], digestedBytes = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace(System.err);
		}
		try (FileInputStream fileInputStream = new FileInputStream(file)) {
			int readBytes;
			while ((readBytes = fileInputStream.read(auxArray)) != -1) {
				messageDigest.update(auxArray, 0, readBytes);
			}
			digestedBytes = messageDigest.digest();
		} catch (IOException ex) {
			// Should never happen
			ex.printStackTrace(System.err);
		}

		StringBuffer stringBuffer = new StringBuffer();
		for (byte digestedByte : digestedBytes) {
			stringBuffer.append(Integer.toString((digestedByte & 0xff) + 0x100,
					16).substring(1));
		}

		ret = stringBuffer.toString();

		return ret;
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
