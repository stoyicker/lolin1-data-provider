package org.jorge.lolin1.models.champion;

import lol4j.protocol.dto.lolstaticdata.*;
import org.jorge.lolin1.models.champion.spells.ActiveSpell;
import org.jorge.lolin1.models.champion.spells.ActiveSpellFactory;
import org.jorge.lolin1.models.champion.spells.PassiveSpell;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

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
public class Champion {

    @SuppressWarnings("unused, FieldCanBeLocal")
    // Used through reflection
    private final String key, name, title, attackrange, mpperlevel, mp, attackdamage,
            hp, hpperlevel, attackdamageperlevel, armor, mpregenperlevel,
            hpregen, critperlevel, spellblockperlevel, mpregen,
            attackspeedperlevel, spellblock, movespeed, attackspeedoffset,
            crit, hpregenperlevel, armorperlevel, lore, imageName;
    private final String[] tags;
    private final ActiveSpell[] spells;
    private final PassiveSpell passive;
    private String[] skins;

    public Champion(ChampionDto championDto, String locale) {
        key = championDto.getKey();
        name = championDto.getName();
        title = championDto.getTitle();
        lore = championDto.getLore();
        imageName = championDto.getImage().getFull();
        List<String> championDtoTags = championDto.getTags();
        tags = new String[championDtoTags.size()];
        for (int i = 0; i < tags.length; i++)
            tags[i] = championDtoTags.get(i);
        List<SkinDto> rawSkins = championDto.getSkins();
        skins = new String[rawSkins.size()];
        for (int i = 0; i < skins.length; i++) {
            String value = rawSkins.get(i).getName();
            System.out.println("Skin " + value);
            skins[i] = value.contentEquals("default") ? this.name : value;
        }
        PassiveDto passiveDto = championDto.getPassive();
        this.passive = new PassiveSpell(passiveDto);
        List<ChampionSpellDto> spellDtos = championDto.getSpells();
        this.spells = new ActiveSpell[spellDtos.size()];
        for (int i = 0; i < spells.length; i++) {
            ChampionSpellDto thisDto = spellDtos.get(i);
            for (Iterator<List<Integer>> iterator = thisDto.getEffect().iterator(); iterator.hasNext(); ) {
                List<Integer> theseEffects = iterator.next();
                if (theseEffects == null || theseEffects.isEmpty())
                    iterator.remove();
            }
            spells[i] = ActiveSpellFactory.createActiveSpell(thisDto, locale);
            System.out.println("Spell " + spells[i].toString());
        }
        StatsDto statsDto = championDto.getStats();
        attackrange = statsDto.getAttackRange() + "";
        mpperlevel = statsDto.getMpPerLevel() + "";
        mp = statsDto.getMp() + "";
        attackdamage = statsDto.getAttackDamage() + "";
        hp = statsDto.getHp() + "";
        hpperlevel = statsDto.getHpPerLevel() + "";
        attackdamageperlevel = statsDto.getAttackDamagePerLevel() + "";
        armor = statsDto.getArmor() + "";
        mpregenperlevel = statsDto.getMpRegenPerLevel() + "";
        hpregen = statsDto.getHpRegen() + "";
        critperlevel = statsDto.getCritPerLevel() + "";
        spellblockperlevel = statsDto.getSpellBlockPerLevel() + "";
        mpregen = statsDto.getMpRegen() + "";
        attackspeedperlevel = statsDto.getAttackSpeedPerLevel() + "";
        spellblock = statsDto.getSpellBlock() + "";
        movespeed = statsDto.getMoveSpeed() + "";
        attackspeedoffset = statsDto.getAttackSpeedOffset() + "";
        crit = statsDto.getCrit() + "";
        hpregenperlevel = statsDto.getHpRegenPerLevel() + "";
        armorperlevel = statsDto.getArmorPerLevel() + "";
        System.out.println("Champion construction completed");
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder("{");
        Field[] fields = this.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field x = fields[i];
            if (!x.getName().contentEquals("tags")
                    && !x.getName().contentEquals("spells")
                    && !x.getName().contentEquals("passive")
                    && !x.getName().contentEquals("skins")) {
                x.setAccessible(Boolean.TRUE);
                try {
                    ret.append("\"").append(x.getName()).append("\":\"").append(x.get(this)).append("\"");
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
        for (int i = 0; i < this.tags.length; ) {
            ret.append("\"").append(this.tags[i]).append("\"");
            if (++i < this.tags.length) {
                ret.append(",");
            }
        }
        ret.append("],");
        ret.append("\"passive\":").append(this.passive.toString()).append(",");
        ret.append("\"spells\":[");
        for (int i = 0; i < this.spells.length; ) {
            ret.append(this.spells[i].toString());
            if (++i < this.spells.length) {
                ret.append(",");
            }
        }
        ret.append("],");
        ret.append("\"skins\":[");
        for (int i = 0; i < this.skins.length; ) {
            ret.append("{\"name\":\"").append(this.skins[i]).append("\"}");
            if (++i < this.skins.length) {
                ret.append(",");
            }
        }
        ret.append("]");
        ret.append("}");
        return ret.toString();
    }
}
