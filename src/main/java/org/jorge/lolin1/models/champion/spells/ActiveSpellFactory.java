package org.jorge.lolin1.models.champion.spells;

import lol4j.protocol.dto.lolstaticdata.ChampionSpellDto;

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
public class ActiveSpellFactory {

    public static ActiveSpell createActiveSpell(ChampionSpellDto championSpellDto) {
        //MAYBE Touch here for the values to be replaced?
        return new ActiveSpell(championSpellDto.getName(), championSpellDto.getSanitizedDescription(), championSpellDto.getImage().getFull(), championSpellDto.getCooldownBurn(), championSpellDto.getRangeBurn(), championSpellDto.getCostBurn());
    }
}
