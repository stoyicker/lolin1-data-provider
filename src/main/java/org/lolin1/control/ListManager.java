package org.lolin1.control;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.lolin1.data.DataAccessObject;
import org.lolin1.utils.Utils;

public class ListManager {

	private final static String LISTS_DIR_NAME = "lists",
			CHAMPIONS_LIST_FILE_NAME = "champions.json";

	private static ListManager instance;

	public static String getChampionsListFileName() {
		return ListManager.CHAMPIONS_LIST_FILE_NAME;
	}

	public static ListManager getInstance() {
		if (ListManager.instance == null) {
			ListManager.instance = new ListManager();
		}
		return ListManager.instance;
	}

	public static String getListsDirName() {
		return ListManager.LISTS_DIR_NAME;
	}

	private ListManager() {
	}

	public void createChampionListsDirectory(String realm, String locale) {
		Utils.delete(Paths.get(DataAccessObject.getChampionsDirName(),
				ListManager.getListsDirName(), realm, locale).toString());
		try {
			Files.createDirectories(Paths.get(
					DataAccessObject.getChampionsDirName(),
					ListManager.getListsDirName(), realm, locale));
		} catch (IOException e) {
			e.printStackTrace(System.err);
		}
	}

	public void setChampions(String realm, String locale, String data) {
		Utils.writeFile(Paths.get(DataAccessObject.getChampionsDirName(),
				ListManager.getListsDirName(), realm, locale,
				ListManager.getChampionsListFileName()), data.toString());
	}
}
