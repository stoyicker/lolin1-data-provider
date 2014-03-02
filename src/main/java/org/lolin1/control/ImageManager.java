package org.lolin1.control;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.lolin1.data.DataAccessObject;
import org.lolin1.data.DataUpdater;
import org.lolin1.models.champion.Champion;
import org.lolin1.utils.Utils;

public class ImageManager {

	private static ImageManager instance;

	private static final String IMAGES_DIR_NAME = "img",
			BUSTS_DIR_NAME = "champion", SPELLS_DIR_NAME = "spell",
			PASSIVES_DIR_NAME = "passive", IMAGE_FILE_EXTENSION = ".png";

	public static final int IMAGE_TYPE_SPELL = 0, IMAGE_TYPE_PASSIVE = 1,
			IMAGE_TYPE_BUST = 2;

	public static String getBustsDirName() {
		return ImageManager.BUSTS_DIR_NAME;
	}

	public static String getImageFileExtension() {
		return ImageManager.IMAGE_FILE_EXTENSION;
	}

	public static ImageManager getInstance() {
		if (ImageManager.instance == null) {
			ImageManager.instance = new ImageManager();
		}
		return ImageManager.instance;
	}

	public static String getPassivesDirName() {
		return ImageManager.PASSIVES_DIR_NAME;
	}

	public static String getSpellsDirName() {
		return ImageManager.SPELLS_DIR_NAME;
	}

	private ImageManager() {
	}

	public final void createChampionImagesDirectories(String realm) {
		Utils.delete(Paths.get(DataAccessObject.getChampionsDirName(),
				ImageManager.IMAGES_DIR_NAME, realm).toString());
		try {
			Files.createDirectories(Paths.get(DataAccessObject.getChampionsDirName(),
					ImageManager.IMAGES_DIR_NAME, realm,
					ImageManager.getBustsDirName()));
			Files.createDirectories(Paths.get(DataAccessObject.getChampionsDirName(),
					ImageManager.IMAGES_DIR_NAME, realm,
					ImageManager.getSpellsDirName()));
			Files.createDirectories(Paths.get(DataAccessObject.getChampionsDirName(),
					ImageManager.IMAGES_DIR_NAME, realm,
					ImageManager.getPassivesDirName()));
		} catch (FileAlreadyExistsException ex) {
			// It's fine, just avoid retrying because probably everything is
			// already created
			return;
		} catch (IOException e) {
			e.printStackTrace(System.err);
		}
	}

	public File downloadChampionBustImage(String realm, Champion champion,
			String imagesUrl) {
		String championImageName = champion.getImageName();
		return Utils.downloadFile(imagesUrl + ImageManager.getBustsDirName()
				+ "/" + championImageName, Paths.get(
				DataAccessObject.getChampionsDirName(), ImageManager.IMAGES_DIR_NAME,
				realm, ImageManager.getBustsDirName(), championImageName));
	}

	public File downloadChampionPassiveImage(String realm, Champion champion,
			String imagesUrl) {
		String passiveImageName = champion.getPassiveImageName();
		return Utils.downloadFile(imagesUrl + ImageManager.getPassivesDirName()
				+ "/" + passiveImageName, Paths.get(
				DataAccessObject.getChampionsDirName(), ImageManager.IMAGES_DIR_NAME,
				realm, ImageManager.getPassivesDirName(), passiveImageName));
	}

	public File[] downloadChampionSpellImages(String realm, Champion champion,
			String imagesUrl) {
		String[] spellImageNames = champion.getSpellImageNames();
		File[] ret = new File[spellImageNames.length];
		int i = 0;
		for (String spellImageName : spellImageNames) {
			ret[i] = Utils.downloadFile(
					imagesUrl + ImageManager.getSpellsDirName() + "/"
							+ spellImageName, Paths.get(
							DataAccessObject.getChampionsDirName(),
							ImageManager.IMAGES_DIR_NAME, realm,
							ImageManager.getSpellsDirName(), spellImageName));
			i++;
		}
		return ret;
	}

	public File getChampionImage(String realm, int imageType, String name) {
		if (!DataUpdater.isUpdating()) {
			switch (imageType) {
			case ImageManager.IMAGE_TYPE_BUST:
				return new File(
						Paths.get(
								DataAccessObject.getChampionsDirName(),
								ImageManager.IMAGES_DIR_NAME,
								realm,
								ImageManager.getBustsDirName(),
								name
										+ (name.endsWith(ImageManager
												.getImageFileExtension()) ? ""
												: ImageManager
														.getImageFileExtension()))
								.toString());
			case ImageManager.IMAGE_TYPE_PASSIVE:
				return new File(
						Paths.get(
								DataAccessObject.getChampionsDirName(),
								ImageManager.IMAGES_DIR_NAME,
								realm,
								ImageManager.getPassivesDirName(),
								name
										+ (name.endsWith(ImageManager
												.getImageFileExtension()) ? ""
												: ImageManager
														.getImageFileExtension()))
								.toString());
			case ImageManager.IMAGE_TYPE_SPELL:
				return new File(
						Paths.get(
								DataAccessObject.getChampionsDirName(),
								ImageManager.IMAGES_DIR_NAME,
								realm,
								ImageManager.getSpellsDirName(),
								name
										+ (name.endsWith(ImageManager
												.getImageFileExtension()) ? ""
												: ImageManager
														.getImageFileExtension()))
								.toString());
			}
		} else {
			return null;
		}
		return null;// Should never happen
	}

}
