package org.lolin1.control;


public class MapManager {

	private final String MAPS_DIR_NAME = "MAPS";

	private static MapManager instance;

	public static MapManager getInstance() {
		if (MapManager.instance == null) {
			MapManager.instance = new MapManager();
		}
		return MapManager.instance;
	}

	private MapManager() {
	}

	private void createMaps() {
	}
}
