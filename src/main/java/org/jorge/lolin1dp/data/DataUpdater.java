package org.jorge.lolin1dp.data;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.jorge.lolin1dp.io.file.FileUtils;

public abstract class DataUpdater {

	private enum RealmEnum {
		NA, EUW, EUNE, BR, LAN, LAS, TR, RU, OCE, KR
	}

	private static final Path FILES_PATH = Paths.get("files"),
			COMMUNITY_PATH=Paths.get(FILES_PATH.toString(),"COMMUNITY"),
			SCHOOL_PATH=Paths.get(FILES_PATH.toString(),"SCHOOL");

	public synchronized static void initFileStructure() {
		// TODO initFileStructure
		if (!Files.exists(FILES_PATH)) {
			if (!FILES_PATH.toFile().mkdirs())
				throw new IllegalStateException("Could not create files folder");
		}

		final RealmEnum[] values = RealmEnum.values();
		for (RealmEnum x : values) {
			FileUtils.createXMLFileIfNotExists(Paths.get(FILES_PATH.toString(),
					x.name()));
		}

		FileUtils.createXMLFileIfNotExists(COMMUNITY_PATH);
		FileUtils.createXMLFileIfNotExists(SCHOOL_PATH);
	}

	public synchronized static void updateData() {
		// TODO updateData
	}
}
