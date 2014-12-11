package org.jorge.lolin1dp.datamodel;

public abstract class Realm {

	public enum RealmEnum {
		NA, EUW, EUNE, BR, LAN, LAS, TR, RU, OCE
	}

	public static String getNewsUrl(RealmEnum realmId, String... locale) {
		StringBuilder realmUrl;

		switch (realmId) {
		case NA:
			realmUrl = new StringBuilder("http://na.leagueoflegends.com/en");
			break;
		case EUW:
			if (locale == null)
				throw new IllegalArgumentException("Locale required.");

			realmUrl = new StringBuilder("http://euw.leagueoflegends.com/");
			realmUrl.append(locale[0].substring(0, 2));
			break;
		case EUNE:
			if (locale == null)
				throw new IllegalArgumentException("Locale required.");
			realmUrl = new StringBuilder("http://eune.leagueoflegends.com/");
			realmUrl.append(locale[0].substring(0, 2));
			break;
		case BR:
			realmUrl = new StringBuilder("http://br.leagueoflegends.com/pt");
			break;
		case LAN:
			realmUrl = new StringBuilder("http://lan.leagueoflegends.com/es");
			break;
		case LAS:
			realmUrl = new StringBuilder("http://las.leagueoflegends.com/es");
			break;
		case TR:
			realmUrl = new StringBuilder("http://tr.leagueoflegends.com/tr");
			break;
		case RU:
			realmUrl = new StringBuilder("http://ru.leagueoflegends.com/ru");
			break;
		case OCE:
			realmUrl = new StringBuilder("http://oce.leagueoflegends.com/en");
			break;
		default:
			throw new IllegalStateException("Illegal RealmEnum");
		}

		return realmUrl.append("/news/").toString();
	}
}
