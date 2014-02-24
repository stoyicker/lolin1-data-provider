package org.lolin1.models.champion.spells;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class ActiveSpellFactory {

	public static ActiveSpell createActiveSpell(JSONObject jsonObject) {
		ActiveSpell ret;
		String name = null, imageName = null, cooldown = null, range = null, cost = null, tooltip = null;
		JSONArray varsArray = null, effectsArray = null;
		try {
			name = jsonObject.getString("name");
			tooltip = jsonObject.getString("tooltip")
					.replaceAll("\"", "\\\\\"");
			imageName = ((JSONObject) jsonObject.get("image"))
					.getString("full");
			cooldown = jsonObject.getString("cooldownBurn");
			range = jsonObject.getString("rangeBurn");
		} catch (JSONException e) {
			e.printStackTrace(System.err);
		}

		// It's key to manage the exceptions in different try-catch blocks to
		// make sure that one variable not being found doesn't forbid the others
		// from being parsed
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
