package org.lolin1.models.champion.spells;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class ActiveSpellFactory {

	@SuppressWarnings("unchecked")
	public static ActiveSpell createActiveSpell(JSONObject jsonObject) {
		ActiveSpell ret;
		String name = null, imageName = null, cooldown = null, range = null, cost = null, tooltip = null;
		JSONArray varsArray = null, effectsArray = null;
		try {
			name = jsonObject.getString("name");
			imageName = ((JSONObject) jsonObject.get("image"))
					.getString("full");
			cooldown = jsonObject.getString("cooldownBurn");
			range = jsonObject.getString("rangeBurn");
			cost = jsonObject.getString("resource").replace("{{ cost }}",
					jsonObject.getString("costBurn"));
			tooltip = jsonObject.getString("tooltip");
			varsArray = jsonObject.getJSONArray("vars");
			effectsArray = jsonObject.getJSONArray("effectsArray");
		} catch (JSONException e) {
			e.printStackTrace(System.err);
		}
		// TODO Download the images with the URL out of the file
		List<String> as = new ArrayList<>(), es = new ArrayList<>(), fs = new ArrayList<>();
		for (int i = 0, aCounter = 0, fCounter = 0; i < varsArray.length(); i++) {
			try {
				if (((HashMap<String, String>) varsArray.get(i)).get("key")
						.contains("a")) {
					as.set(aCounter, ((HashMap<String, String>) varsArray
							.get(i)).get("coeff"));
					aCounter++;
				} else {
					fs.set(fCounter, ((HashMap<String, String>) varsArray
							.get(i)).get("coeff"));
					fCounter++;
				}
			} catch (JSONException e) {
				e.printStackTrace(System.err);
			}
		}
		for (int i = 0; i < as.size(); i++) {
			tooltip = tooltip.replaceAll("{{ a" + i + " }}", as.get(i - 1));
		}
		for (int i = 0; i < fs.size(); i++) {
			tooltip = tooltip.replaceAll("{{ f" + i + " }}", fs.get(i - 1));
		}
		for (int i = 0; i < effectsArray.length(); i++) {
			try {
				es.set(i, effectsArray.getString(i).replaceAll("\\", ""));
			} catch (JSONException e) {
				e.printStackTrace(System.err);
			}
		}
		for (int i = 0; i < es.size(); i++) {
			tooltip = tooltip.replaceAll("{{ e" + i + " }}", es.get(i - 1));
		}
		ret = new ActiveSpell(name, tooltip, imageName, cooldown, range, cost);
		return ret;
	}
}
