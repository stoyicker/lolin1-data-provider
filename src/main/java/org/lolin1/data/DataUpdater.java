package org.lolin1.data;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.util.ajax.JSON;
import org.lolin1.control.Controller;
import org.lolin1.models.champion.Champion;
import org.lolin1.utils.Utils;

public abstract class DataUpdater {

	private static final String REALM_PLACE_HOLDER = "LOWERCASE_REALM_HERE",
			LOCALE_PLACE_HOLDER = "LOCALE_MIXED_CASE_HERE",
			REALM_FILE_URL = "https://prod.api.pvp.net/api/lol/static-data/"
					+ DataUpdater.REALM_PLACE_HOLDER + "/v1/realm",
			ALL_CHAMPIONS_URL = "https://prod.api.pvp.net/api/lol/static-data/"
					+ DataUpdater.REALM_PLACE_HOLDER + "/v1/champion?locale="
					+ DataUpdater.LOCALE_PLACE_HOLDER
					+ "&champData=lore,tags,stats,spells,passive,image",
			INFO_WRAPPER = "n", VERSION_KEY = "champion", CDN_KEY = "cdn";
	private static String CDN;
	private static Boolean UPDATING = Boolean.FALSE;

	protected static boolean isUpdating() {
		return DataUpdater.UPDATING;
	}

	@SuppressWarnings("unchecked")
	private static void performUpdate(String realm, String locale,
			String newVersion) {
		DataUpdater.UPDATING = Boolean.TRUE;
		Utils.createImagesDirectory();
		String IMAGES_URL = DataUpdater.CDN + "/" + newVersion + "/img/";
		List<Champion> champions = new ArrayList<>();
		Map<String, Object> map = (Map<String, Object>) ((Map<String, Object>) JSON
				.parse(Utils.performRiotGet(DataUpdater.ALL_CHAMPIONS_URL
						.replace(DataUpdater.REALM_PLACE_HOLDER, realm)
						.replace(DataUpdater.LOCALE_PLACE_HOLDER, locale))))
				.get("data");
		for (String key : map.keySet()) {
			final Champion thisChampion = new Champion(
					(HashMap<String, Object>) map.get(key));
			champions.add(thisChampion);
			ExecutorService executor = Executors.newFixedThreadPool(6);
			final File bustImage = Utils.downloadChampionBustImage(
					thisChampion, IMAGES_URL);
			executor.submit(new Runnable() {

				@Override
				public void run() {
					Controller.getController().setImageHash(
							Controller.IMAGE_TYPE_BUST,
							thisChampion.getImageName(),
							Utils.getFileMD5(bustImage));
				}
			});
			final File passiveImage = Utils.downloadChampionPassiveImage(
					thisChampion, IMAGES_URL);
			executor.submit(new Runnable() {

				@Override
				public void run() {
					Controller.getController().setImageHash(
							Controller.IMAGE_TYPE_PASSIVE,
							thisChampion.getPassiveImageName(),
							Utils.getFileMD5(passiveImage));
				}
			});
			final File[] spellImages = Utils.downloadChampionSpellImages(
					thisChampion, IMAGES_URL);
			for (final File x : spellImages) {
				executor.submit(new Runnable() {

					@Override
					public void run() {
						Controller.getController().setImageHash(
								Controller.IMAGE_TYPE_SPELL,
								thisChampion.getSpellImageNames()[Arrays
										.asList(spellImages).indexOf(x)],
								Utils.getFileMD5(x));
					}
				});
			}
			executor.shutdown();
			Controller.getController().setChampions(locale, realm, champions);
			try {
				executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace(System.err);
			}
		}
		DataAccessObject.setChampionsVersion(realm, newVersion);
		DataUpdater.UPDATING = Boolean.FALSE;
	}

	@SuppressWarnings("unchecked")
	private static String retrieveDragonMagicVersion(String realm) {
		String ret = "", wholeFile = Utils
				.performRiotGet(DataUpdater.REALM_FILE_URL.replace(
						DataUpdater.REALM_PLACE_HOLDER, realm.toLowerCase()));
		Map<String, Object> realmJson = (Map<String, Object>) JSON
				.parse(wholeFile);
		String version = ((HashMap<String, String>) realmJson
				.get(DataUpdater.INFO_WRAPPER)).get(DataUpdater.VERSION_KEY);
		DataUpdater.CDN = realmJson.get(DataUpdater.CDN_KEY).toString();
		ret = (version == null) ? "" : version;
		return ret;
	}

	public static void updateData() {
		for (String realm : DataAccessObject.getSupportedRealms().keySet()) {
			String newVersion;
			if (!DataAccessObject.getVersion(realm)
					.contentEquals(
							(newVersion = DataUpdater
									.retrieveDragonMagicVersion(realm)))) {
				for (String locale : DataAccessObject.getSupportedRealms().get(
						realm)) {
					DataUpdater.performUpdate(realm, locale, newVersion);
				}
				DataAccessObject.setChampionsVersion(realm, newVersion);
			}
		}
	}
}
