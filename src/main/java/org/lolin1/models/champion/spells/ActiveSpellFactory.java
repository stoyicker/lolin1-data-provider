/**
 * This file is part of lolin1-data-provider.

    lolin1-data-provider is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    lolin1-data-provider is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with lolin1-data-provider.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.lolin1.models.champion.spells;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class ActiveSpellFactory {

	private static final String DUMMY_ERROR_MESSAGE = "The Riot API is still a little bit buggy...";

	public static ActiveSpell createActiveSpell(JSONObject jsonObject) {
		ActiveSpell ret;
		String name = null, imageName = null, cooldown = null, range = null, cost = null, tooltip = null;
		JSONArray varsArray = null, effectsArray = null;
		// It's key to manage the exceptions in different try-catch blocks to
		// make sure that one variable not being found doesn't forbid the others
		// from being parsed
		try {
			name = jsonObject.getString("name");
		} catch (JSONException e) {
			name = ActiveSpellFactory.DUMMY_ERROR_MESSAGE;
			e.printStackTrace(System.err);
		}
		try {
			tooltip = jsonObject.getString("tooltip")
					.replaceAll("\"", "\\\\\"");
		} catch (JSONException e) {
			tooltip = ActiveSpellFactory.DUMMY_ERROR_MESSAGE;
		}
		try {
			imageName = ((JSONObject) jsonObject.get("image"))
					.getString("full");
		} catch (JSONException e) {
			imageName = ActiveSpellFactory.DUMMY_ERROR_MESSAGE;
		}
		try {
			cooldown = jsonObject.getString("cooldownBurn");
		} catch (JSONException e) {
			cooldown = ActiveSpellFactory.DUMMY_ERROR_MESSAGE;
		}
		try {
			range = jsonObject.getString("rangeBurn");
		} catch (JSONException e) {
			range = ActiveSpellFactory.DUMMY_ERROR_MESSAGE;
		}
		try {
			cost = jsonObject.getString("resource").replace(
					"\\{\\{ cost \\}\\}", jsonObject.getString("costBurn"));
		} catch (JSONException e) {
			// No explicitly stated cost, it's already reported to Riot Games
		}
		try {
			varsArray = jsonObject.getJSONArray("vars");
		} catch (JSONException e) {
			// No scaling, it's fine
		}
		try {
			effectsArray = jsonObject.getJSONArray("effectBurn");
		} catch (JSONException e) {
			// No base states, it's fine
		}
		List<String> as = new ArrayList<>(), es = new ArrayList<>(), fs = new ArrayList<>();
		if (varsArray != null) /* Avoid this if the spell doesn't scale */{
			for (int i = 0, aCounter = 0, fCounter = 0; i < varsArray.length(); i++) {
				try {
					if (((JSONObject) varsArray.get(i)).getString("key")
							.contains("a")) {
						if (aCounter >= as.size()) {
							for (int k = 0; k <= (aCounter - as.size()); k++) {
								as.add("");// Avoid IndexOutOfBoundsException
							}
						}
						as.set(aCounter, ((JSONObject) varsArray.get(i))
								.getString("coeff"));
						aCounter++;
					} else {
						if (fCounter >= fs.size()) {
							for (int k = 0; k <= (fCounter - fs.size()); k++) {
								fs.add("");// Avoid IndexOutOfBoundsException
							}
						}
						fs.set(fCounter, ((JSONObject) varsArray.get(i))
								.getString("coeff"));
						fCounter++;
					}
				} catch (JSONException e) {
					e.printStackTrace(System.err);
				}
			}
			for (int i = 1; i <= as.size(); i++) {
				tooltip = tooltip.replaceAll("\\{\\{ a" + i + " \\}\\}",
						as.get(i - 1));
			}
			for (int i = 1; i <= fs.size(); i++) {
				tooltip = tooltip.replaceAll("\\{\\{ f" + i + " \\}\\}",
						fs.get(i - 1));
			}
		}
		if (effectsArray != null)/*
								 * Avoid this if the spell doesn't have any base
								 * stats
								 */{
			for (int i = 0; i < effectsArray.length(); i++) {
				try {
					if (i >= es.size()) {
						for (int k = 0; k <= (i - es.size()); k++) {
							es.add("");// Avoid IndexOutOfBoundsException
						}
					}
					es.set(i, effectsArray.getString(i));
				} catch (JSONException e) {
					e.printStackTrace(System.err);
				}
			}
			for (int i = 1; i <= es.size(); i++) {
				tooltip = tooltip.replaceAll("\\{\\{ e" + i + " \\}\\}",
						es.get(i - 1));
			}
		}
		ret = new ActiveSpell(name, tooltip, imageName, cooldown, range, cost);
		return ret;
	}
}
