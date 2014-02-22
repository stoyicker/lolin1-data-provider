package org.lolin1.control;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lolin1.models.Champion;

public class ChampionManager {

	private class Pair {
		private final String partOne, partTwo;

		private Pair(String _one, String _two) {
			this.partOne = _one;
			this.partTwo = _two;
		}
	}

	private static ChampionManager singleton = null;

	private final static Map<Pair, List<Champion>> CHAMPIONS = new HashMap<>();

	public static ChampionManager getChampionManager() {
		if (ChampionManager.singleton == null) {
			ChampionManager.singleton = new ChampionManager();
		}
		return ChampionManager.singleton;
	}

	private ChampionManager() {
	}

	public List<Champion> getChampions(String locale, String realm) {
		return ChampionManager.CHAMPIONS.get(new Pair(locale, realm));
	}

	public boolean isPairSupported(String locale, String realm) {
		return ChampionManager.CHAMPIONS.containsKey(new Pair(locale, realm));
	}
}
