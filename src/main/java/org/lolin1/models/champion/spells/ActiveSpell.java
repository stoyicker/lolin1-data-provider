package org.lolin1.models.champion.spells;

public class ActiveSpell extends PassiveSpell {
	/**
	 * Passive spells are not considered by Riot to have a cooldownBurn nor a
	 * rangeBurn
	 */
	@SuppressWarnings("unused")
	// Used in superclass's reflection
	private final String cooldownBurn, rangeBurn, costBurn;

	protected ActiveSpell(String _name, String _detail, String _imageName,
			String _cd, String _range, String _cost) {
		super(_name, _detail, _imageName);
		this.cooldownBurn = _cd;
		this.rangeBurn = _range;
		this.costBurn = _cost;
	}
}
