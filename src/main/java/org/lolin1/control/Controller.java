package org.lolin1.control;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lolin1.models.champion.Champion;

public final class Controller {

	private class Pair {
		@SuppressWarnings("unused")
		// They're used to index the Controller.CHAMPIONS map
		private final String partOne, partTwo;

		private Pair(String _one, String _two) {
			this.partOne = _one;
			this.partTwo = _two;
		}
	}

	private final Map<String, String> SPELL_HASHES = new HashMap<>(),
			BUST_HASHES = new HashMap<>(), PASSIVE_HASHES = new HashMap<>();

	public static final int IMAGE_TYPE_SPELL = 0, IMAGE_TYPE_PASSIVE = 1,
			IMAGE_TYPE_BUST = 2;

	private static Controller singleton = null;

	private final static Map<Pair, List<Champion>> CHAMPIONS = new HashMap<>();

	public static Controller getChampionManager() {
		if (Controller.singleton == null) {
			Controller.singleton = new Controller();
		}
		return Controller.singleton;
	}

	private Controller() {
	}

	public List<Champion> getChampions(String locale, String realm) {
		return Controller.CHAMPIONS.get(new Pair(locale, realm));
	}

	public boolean isPairSupported(String locale, String realm) {
		return Controller.CHAMPIONS.containsKey(new Pair(locale, realm));
	}

	public void setChampions(String locale, String realm,
			List<Champion> champions) {
		Controller.CHAMPIONS.put(new Pair(locale, realm), champions);
	}

	public void setImageHash(int imageType, String imageName, String hash) {
		// TODO setImageHash
		switch (imageType) {
		case IMAGE_TYPE_BUST:
			this.BUST_HASHES.put(imageName, hash);
		case IMAGE_TYPE_PASSIVE:
			this.PASSIVE_HASHES.put(imageName, hash);
		case IMAGE_TYPE_SPELL:
			this.SPELL_HASHES.put(imageName, hash);
		default:
			throw new IllegalArgumentException(
					"Controller.setImageHash: imageType must be one of Controller.IMAGE_TYPE_BUST, Controller.IMAGE_TYPE_PASSIVE and Controller.IMAGE_TYPE_SPELL");
		}
	}
}
