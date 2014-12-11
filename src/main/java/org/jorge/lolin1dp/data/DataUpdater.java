package org.jorge.lolin1dp.data;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.jorge.lolin1dp.datamodel.Realm;
import org.jorge.lolin1dp.datamodel.Realm.RealmEnum;
import org.jorge.lolin1dp.io.file.FileUtils;
import org.jorge.lolin1dp.io.net.Internet;

public abstract class DataUpdater {

	private static final Path FILES_PATH = Paths.get("files"),
			COMMUNITY_PATH = Paths.get(FILES_PATH.toString(), "COMMUNITY"),
			SCHOOL_PATH = Paths.get(FILES_PATH.toString(), "SCHOOL");

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
		Internet.getNews(Realm.getBaseUrl(RealmEnum.NA),
				Realm.getNewsUrl(RealmEnum.NA));
	}
}
