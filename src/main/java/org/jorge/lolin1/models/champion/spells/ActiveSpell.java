package org.jorge.lolin1.models.champion.spells;

/**
 * This file is part of lolin1-data-provider.
 * <p/>
 * lolin1-data-provider is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * lolin1-data-provider is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with lolin1-data-provider.  If not, see <http://www.gnu.org/licenses/>.
 */
public class ActiveSpell extends PassiveSpell {
    /**
     * Passive spells are not considered by Riot to have a cooldownBurn nor a
     * rangeBurn
     */
    // Used in superclass's reflection
    @SuppressWarnings("unused")
    private final String cooldownBurn, rangeBurn, costBurn;

    protected ActiveSpell(String _name, String _detail, String _imageName,
                          String _cd, String _range, String _cost) {
        super(_name, _detail, _imageName);
        this.cooldownBurn = _cd;
        this.rangeBurn = _range;
        this.costBurn = _cost;
    }
}
