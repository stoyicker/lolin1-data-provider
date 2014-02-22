package org.lolin1.models;

import java.lang.reflect.Field;
import java.util.Map;

public class Champion {

	private class PassiveSpell extends Spell {

		@Override
		public String toString() {
			StringBuilder ret = new StringBuilder("\"passive\":{");
			ret.append("\"detail\":\"").append(this.detail)
					.append("\",\"name\":\"").append(this.name).append("\"");
			ret.append("}");
			return ret.toString();
		}
	}

	private class Spell {
		protected final String imageName, detail, name;
		private final String cooldown, range; // Passive spells are not
												// considered by Riot to have a
												// cooldown nor a range

		private Spell() {

		}

		@Override
		public String toString() {
			StringBuilder ret = new StringBuilder("{");
			Field[] fields = this.getClass().getDeclaredFields();
			for (int i = 0; i < fields.length;) {
				Field x = fields[i];
				x.setAccessible(Boolean.TRUE);
				try {
					ret.append("\"" + x.getName() + "\":\"" + x.get(this)
							+ "\"");
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace(System.err);
				}
				if (++i < fields.length) {
					ret.append(",");
				}
			}
			ret.append("}");
			return ret.toString();
		}
	}

	private final String key, name, title, attackrange, mpperlevel, mp,
			attackdamage, hp, hpperlevel, attackdamageperlevel, armor,
			mpregenperlevel, hpregen, critperlevel, spellblockperlevel,
			mpregen, attackspeedperlevel, spellblock, movespeed,
			attackspeedoffset, crit, hpregenperlevel, armorperlevel, lore;
	private final String[] tags;
	private final Spell[] spells;
	private final PassiveSpell passive;

	public Champion(Map<String, String> descriptor) {
		for (String x : descriptor.keySet()) {
			System.out.println("Key " + x + " mapped to value "
					+ descriptor.get(x));
		}
	}

	@Override
	public String toString() {
		StringBuilder ret = new StringBuilder("{");
		Field[] fields = this.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length;) {
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
			}
			if (++i < fields.length) {
				ret.append(",");
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
