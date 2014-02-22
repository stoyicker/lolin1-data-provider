package org.lolin1.models;

import java.util.Map;

public class Champion {

	private final class Spell {
		private final String cooldown;
		private final String detail;
		private final String name;

		private Spell() {

		}
	}

	private final String id, name, title, attackrange, mpperlevel, mp,
			attackdamage, hp, hpperlevel, attackdamageperlevel, armor,
			mpregenperlevel, hpregen, critperlevel, spellblockperlevel,
			mpregen, attackspeedperlevel, spellblock, movespeed,
			attackspeedoffset, crit, hpregenperlevel, armorperlevel, lore;
	private final String[] tags;

	private final Spell[] spells;

	public Champion(Map<String, String> descriptor) {
		for (String x : descriptor.keySet()) {
			System.out.println("Key " + x + " mapped to value "
					+ descriptor.get(x));
		}
	}
}
