package org.jorge.lolin1dp.datamodel;

import java.util.ArrayList;
import java.util.List;

/**
 * This file is part of lolin1dp-data-provider.
 * <p/>
 * lolin1dp-data-provider is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * <p/>
 * lolin1dp-data-provider is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * <p/>
 * You should have received a copy of the GNU General Public License along with
 * lolin1dp-data-provider. If not, see <http://www.gnu.org/licenses/>.
 */
public abstract class Realm {

	public enum RealmEnum {
		NA, EUW, EUNE, BR, LAN, LAS, TR, RU, OCE
	}

	public static String getNewsUrl(RealmEnum realmId, String locale) {
		StringBuilder realmUrl;

		System.out.println("Getting news url for realm " + realmId.name());

		switch (realmId) {
		case NA:
			realmUrl = new StringBuilder("http://na.leagueoflegends.com/en");
			break;
		case EUW:
			if (locale == null)
				throw new IllegalArgumentException("Locale required.");

			realmUrl = new StringBuilder("http://euw.leagueoflegends.com/");
			realmUrl.append(locale.toLowerCase().substring(0, 2));
			break;
		case EUNE:
			if (locale == null)
				throw new IllegalArgumentException("Locale required.");
			realmUrl = new StringBuilder("http://eune.leagueoflegends.com/");
			realmUrl.append(locale.toLowerCase().substring(0, 2));
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

		System.out.println("Returning news url " + realmUrl.toString()
				+ "/news/");

		return realmUrl.append("/news/").toString();
	}

	public static String getBaseUrl(RealmEnum realmId) {
		StringBuilder baseUrl;

		switch (realmId) {
		case NA:
			baseUrl = new StringBuilder("http://na.leagueoflegends.com");
			break;
		case EUW:
			baseUrl = new StringBuilder("http://euw.leagueoflegends.com");
			break;
		case EUNE:
			baseUrl = new StringBuilder("http://eune.leagueoflegends.com");
			break;
		case BR:
			baseUrl = new StringBuilder("http://br.leagueoflegends.com");
			break;
		case LAN:
			baseUrl = new StringBuilder("http://lan.leagueoflegends.com");
			break;
		case LAS:
			baseUrl = new StringBuilder("http://las.leagueoflegends.com");
			break;
		case TR:
			baseUrl = new StringBuilder("http://tr.leagueoflegends.com");
			break;
		case RU:
			baseUrl = new StringBuilder("http://ru.leagueoflegends.com");
			break;
		case OCE:
			baseUrl = new StringBuilder("http://oce.leagueoflegends.com");
			break;
		default:
			throw new IllegalStateException("Illegal RealmEnum");
		}

		return baseUrl.toString();
	}

	public static List<String> getLocales(RealmEnum realmId) {
		List<String> ret = new ArrayList<>();

		switch (realmId) {
		case NA:
			ret.add("en_US");
			break;
		case EUW:
			ret.add("en_US");
			ret.add("de_DE");
			ret.add("es_ES");
			ret.add("fr_FR");
			ret.add("it_IT");
			break;
		case EUNE:
			ret.add("en_US");
			ret.add("pl_PL");
			ret.add("el_GR");
			ret.add("ro_RO");
			ret.add("cs_CZ");
			ret.add("hu_HU");
			break;
		case BR:
			ret.add("pt_PT");
			break;
		case LAN:
			ret.add("es_ES");
			break;
		case LAS:
			ret.add("es_ES");
			break;
		case TR:
			ret.add("tr_TR");
			break;
		case RU:
			ret.add("ru_RU");
			break;
		case OCE:
			ret.add("en_US");
			break;
		default:
			throw new IllegalStateException("Illegal RealmEnum");
		}

		return ret;
	}
}
