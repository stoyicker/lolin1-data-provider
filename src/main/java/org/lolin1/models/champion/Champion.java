package org.lolin1.models.champion;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.eclipse.jetty.util.ajax.JSON;
import org.lolin1.models.champion.spells.ActiveSpell;
import org.lolin1.models.champion.spells.ActiveSpellFactory;
import org.lolin1.models.champion.spells.PassiveSpell;

public class Champion {

	@SuppressWarnings("unused")
	// Used through reflection
	private String key, name, title, attackrange, mpperlevel, mp, attackdamage,
			hp, hpperlevel, attackdamageperlevel, armor, mpregenperlevel,
			hpregen, critperlevel, spellblockperlevel, mpregen,
			attackspeedperlevel, spellblock, movespeed, attackspeedoffset,
			crit, hpregenperlevel, armorperlevel, lore;
	private final String[] tags;
	private final ActiveSpell[] spells;
	private final PassiveSpell passive;

	@SuppressWarnings("unchecked")
	public Champion(String descriptor) {
		HashMap<String, Object> parsedDescriptor = (HashMap<String, Object>) JSON
				.parse(descriptor);

		Field[] fields = this.getClass().getDeclaredFields();
		for (Field x : fields) {
			if (x.getName().contentEquals("key")
					|| x.getName().contentEquals("name")
					|| x.getName().contentEquals("title")) {
				String fieldName = x.getName();
				x.setAccessible(Boolean.TRUE);
				try {
					x.set(this, parsedDescriptor.get(fieldName).toString());
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace(System.err);
				}
				x.setAccessible(Boolean.FALSE);
			} else if (x.getType() == String.class) {
				x.setAccessible(Boolean.TRUE);
				try {
					x.set(this,
							((HashMap<String, Object>) parsedDescriptor
									.get("stats")).get(x.getName()) + ""); // Force
																			// conversion
																			// to
																			// String
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace(System.err);
				}
				x.setAccessible(Boolean.FALSE);
			}
		}
		String _tags = JSON.toString(parsedDescriptor.get("tags"))
				.replace("[", "").replace("]", "").replace("\"", "\"");
		StringTokenizer tagsTokenizer = new StringTokenizer(_tags, ",");
		this.tags = new String[tagsTokenizer.countTokens()];
		for (int i = 0; i < this.tags.length; i++) {
			this.tags[i] = tagsTokenizer.nextToken();
		}
		this.passive = new PassiveSpell(
				(HashMap<String, Object>) parsedDescriptor.get("passive"));
		String _parsedSpells = JSON.toString(parsedDescriptor.get("spells"));
		JSONArray spellsArray = null;
		try {
			spellsArray = new JSONArray(_parsedSpells);
		} catch (JSONException e) {
			e.printStackTrace(System.err);
		}
		this.spells = new ActiveSpell[spellsArray.length()];
		for (int i = 0; i < this.spells.length; i++) {
			try {
				this.spells[i] = ActiveSpellFactory
						.createActiveSpell((spellsArray.getJSONObject(i)));
			} catch (JSONException e) {
				e.printStackTrace(System.err);
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder ret = new StringBuilder("{");
		Field[] fields = this.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field x = fields[i];
			if (!x.getName().contentEquals("tags")
					&& !x.getName().contentEquals("spells")) {
				x.setAccessible(Boolean.TRUE);
				try {
					ret.append("\"" + x.getName() + "\":\"" + x.get(this)
							+ "\"");
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace(System.err);
				}
				x.setAccessible(Boolean.FALSE);
				if ((i + 1) < fields.length) {
					ret.append(",");
				}
			}
		}
		ret.append("\"tags\":[");
		for (int i = 0; i < this.tags.length;) {
			ret.append("\"" + this.tags[i] + "\"");
			if (++i < this.tags.length) {
				ret.append(",");
			}
		}
		ret.append("],");
		ret.append(this.passive.toString()).append(",");
		for (int i = 0; i < this.spells.length;) {
			ret.append(this.spells[i].toString());
			if (++i < this.tags.length) {
				ret.append(",");
			}
		}
		return ret.append("}").toString();
	}
}
