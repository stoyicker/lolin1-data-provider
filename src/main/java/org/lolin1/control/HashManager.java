package org.lolin1.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.lolin1.data.DataAccessObject;
import org.lolin1.data.DataUpdater;
import org.lolin1.utils.Utils;

public class HashManager {

	private static HashManager instance;
	private static final String HASH_TYPE = "MD5", HASH_TYPE_EXTENSION = "."
			+ HashManager.HASH_TYPE, HASH_FOLDER_NAME = "hashes";

	public static String getHashFolderName() {
		return HashManager.HASH_FOLDER_NAME;
	}

	public static HashManager getInstance() {
		if (HashManager.instance == null) {
			HashManager.instance = new HashManager();
		}
		return HashManager.instance;
	}

	private HashManager() {
	}

	public void createChampionHashesDirectories(String realm) {
		Utils.delete(Paths.get(Controller.getChampionsDirName(),
				HashManager.getHashFolderName(), realm).toString());
		try {
			Files.createDirectories(Paths.get(Controller.getChampionsDirName(),
					HashManager.getHashFolderName(), realm,
					ImageManager.getBustsDirName()));
			Files.createDirectories(Paths.get(Controller.getChampionsDirName(),
					HashManager.getHashFolderName(), realm,
					ImageManager.getPassivesDirName()));
			Files.createDirectories(Paths.get(Controller.getChampionsDirName(),
					HashManager.getHashFolderName(), realm,
					ImageManager.getSpellsDirName()));
		} catch (IOException e) {
			e.printStackTrace(System.err);
		}
	}

	public String getFileHash(File file) {
		String ret = null;
		MessageDigest messageDigest = null;
		byte[] auxArray = new byte[1024], digestedBytes = null;
		try {
			messageDigest = MessageDigest.getInstance(HashManager.HASH_TYPE);
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

	public String getImageHash(String root, String realm, int imageType,
			String name) {
		if (!DataUpdater.isUpdating()) {
			String namePlusExtension = (name.toLowerCase()
					+ ((name.toLowerCase().endsWith(
							ImageManager.getImageFileExtension().toLowerCase()) ? ""
							: ImageManager.getImageFileExtension()
									.toLowerCase())) + HashManager.HASH_TYPE_EXTENSION), ret;
			switch (imageType) {
			case ImageManager.IMAGE_TYPE_BUST:
				ret = Utils.readFile(Paths.get(root, realm,
						ImageManager.getBustsDirName(), namePlusExtension));
				break;
			case ImageManager.IMAGE_TYPE_PASSIVE:
				ret = Utils.readFile(Paths.get(root, realm,
						ImageManager.getPassivesDirName(), namePlusExtension));
				break;
			case ImageManager.IMAGE_TYPE_SPELL:
				ret = Utils.readFile(Paths.get(root, realm,
						ImageManager.getSpellsDirName(), namePlusExtension));
				break;
			default:
				ret = DataAccessObject.getResponseError(); // Should never
															// happen
			}
			return ret;
		} else {
			return DataAccessObject.getResponseError();
		}
	}

	public void setImageHash(String root, String realm, int imageType,
			String imageName, String hash) {
		switch (imageType) {
		case ImageManager.IMAGE_TYPE_BUST:
			Utils.setMapFile(Paths.get(root, HashManager.getHashFolderName(),
					realm, ImageManager.getBustsDirName(),
					imageName.toLowerCase() + HashManager.HASH_TYPE_EXTENSION),
					hash);
		case ImageManager.IMAGE_TYPE_PASSIVE:
			Utils.setMapFile(Paths.get(root, HashManager.getHashFolderName(),
					realm, ImageManager.getPassivesDirName(),
					imageName.toLowerCase() + HashManager.HASH_TYPE_EXTENSION),
					hash);
		case ImageManager.IMAGE_TYPE_SPELL:
			Utils.setMapFile(Paths.get(root, HashManager.getHashFolderName(),
					realm, ImageManager.getSpellsDirName(),
					imageName.toLowerCase() + HashManager.HASH_TYPE_EXTENSION),
					hash);
		default:
			throw new IllegalArgumentException(
					"Controller.setImageHash: imageType must be one of "
							+ ImageManager.IMAGE_TYPE_BUST + ", "
							+ ImageManager.IMAGE_TYPE_PASSIVE + " and "
							+ ImageManager.IMAGE_TYPE_SPELL);
		}
	}
}
