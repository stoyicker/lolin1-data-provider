package org.lolin1.control;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lolin1.data.DataAccessObject;
import org.lolin1.data.DataUpdater;
import org.lolin1.models.champion.Champion;
import org.lolin1.utils.Utils;

public final class Controller {

	private class Pair {
		private final String partOne, partTwo;

		private Pair(String _one, String _two) {
			this.partOne = _one;
			this.partTwo = _two;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return Boolean.TRUE;
			}
			if (!(o instanceof Pair)) {
				return Boolean.FALSE;
			}
			Pair target = (Pair) o;
			return this.partOne.contentEquals(target.partOne)
					&& this.partTwo.contentEquals(target.partTwo);
		}

		@Override
		public int hashCode() {
			int sum = 0, i = 1;
			StringBuilder everything = new StringBuilder(this.partOne);
			everything.append(this.partTwo);
			for (char x : everything.toString().toCharArray()) {
				sum += Character.getNumericValue(x) * i;
				i++;
			}
			return sum;
		}
	}

	private final Map<String, String> SPELL_HASHES = new HashMap<>(),
			BUST_HASHES = new HashMap<>(), PASSIVE_HASHES = new HashMap<>();

	public static final int IMAGE_TYPE_SPELL = 0, IMAGE_TYPE_PASSIVE = 1,
			IMAGE_TYPE_BUST = 2;

	private static Controller singleton = null;

	private final static Map<Pair, List<Champion>> CHAMPIONS = new HashMap<>();

	private static final String IMAGE_FILE_EXTENSION = ".png";

	public static Controller getController() {
		if (Controller.singleton == null) {
			Controller.singleton = new Controller();
		}
		return Controller.singleton;
	}

	public static String getImageFileExtension() {
		return Controller.IMAGE_FILE_EXTENSION;
	}

	private Controller() {
	}

	public List<Champion> getChampions(String locale, String realm) {
		return Controller.CHAMPIONS.get(new Pair(locale, realm));
	}

	public File getImage(String realm, int imageType, String name) {
		if (!DataUpdater.isUpdating()) {
			return Utils.getFile(realm, imageType, name);
		} else {
			return null;
		}
	}

	public String getImageHash(int imageType, String name) {
		String namePlusExtension = (name + Controller.getImageFileExtension())
				.toLowerCase();
		if (!DataUpdater.isUpdating()) {
			switch (imageType) {
			case IMAGE_TYPE_BUST:
				return this.BUST_HASHES.get(namePlusExtension);
			case IMAGE_TYPE_PASSIVE:
				return this.PASSIVE_HASHES.get(namePlusExtension);
			case IMAGE_TYPE_SPELL:
				return this.SPELL_HASHES.get(namePlusExtension);
			default:
				return DataAccessObject.getResponseError(); // Should never
															// happen
			}
		} else {
			return DataAccessObject.getResponseError();
		}
	}

	public boolean isPairSupported(String locale, String realm) {
		return Controller.CHAMPIONS.containsKey(new Pair(locale, realm));
	}

	public void setChampions(String locale, String realm,
			List<Champion> champions) {
		Controller.CHAMPIONS.put(new Pair(locale, realm), champions);
	}

	// TODO Put the portal for visitors

	public void setImageHash(int imageType, String imageName, String hash) {
		switch (imageType) {
		case IMAGE_TYPE_BUST:
			this.BUST_HASHES.put(imageName.toLowerCase(), hash);
		case IMAGE_TYPE_PASSIVE:
			this.PASSIVE_HASHES.put(imageName.toLowerCase(), hash);
		case IMAGE_TYPE_SPELL:
			this.SPELL_HASHES.put(imageName.toLowerCase(), hash);
		default:
			throw new IllegalArgumentException(
					"Controller.setImageHash: imageType must be one of Controller.IMAGE_TYPE_BUST, Controller.IMAGE_TYPE_PASSIVE and Controller.IMAGE_TYPE_SPELL");
		}
	}
}
