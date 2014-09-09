package org.jorge.lolin1.models.champion.spells;

import lol4j.protocol.dto.lolstaticdata.ChampionSpellDto;
import lol4j.protocol.dto.lolstaticdata.SpellVarsDto;
import org.jorge.lolin1.utils.LoLin1DataProviderUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private static final Map<String, String> SPELL_DAMAGE_LOCALIZATION_MAP = new HashMap<>(), BONUS_ATTACK_DAMAGE_LOCALIZATION_MAP = new HashMap<>();

    public static ActiveSpell createActiveSpell(ChampionSpellDto championSpellDto, String locale) {
        System.out.println("------------- START - createActiveSpell -----------");
        String pureTooltip = championSpellDto.getSanitizedTooltip(), finalTooltip = pureTooltip;
        System.out.println("pureTooltip: " + pureTooltip);
        if (pureTooltip != null && !pureTooltip.isEmpty()) {
            List<SpellVarsDto> vars = championSpellDto.getVars();
            for (SpellVarsDto var : vars) {
                String firstPart = LoLin1DataProviderUtils.joinIfDifferent(LoLin1DataProviderUtils.doubleListAsStringList(var.getCoeff()), "/");
                String secondPart = " " + stringifySpellScaling(var.getLink(), locale);
                finalTooltip = finalTooltip.replace("{{ " + var.getKey() + " }}", firstPart + secondPart).replace(" + ", " ");
            }
            List<List<Integer>> effects = championSpellDto.getEffect();
            System.out.println("Effects size: " + effects);
            for (int i = 1; i <= effects.size(); i++) {
                System.out.println(finalTooltip);
                final String replacement = LoLin1DataProviderUtils.joinIfDifferent(LoLin1DataProviderUtils.integerListAsStringList(effects.get(i - 1)), "/");
                System.out.println(replacement);
                finalTooltip = finalTooltip.replace("{{ e" + i + " }}", replacement);
            }
        }
        System.out.println("------------- FINISH - createActiveSpell -----------");
        ActiveSpell ret = new ActiveSpell(championSpellDto.getName(), finalTooltip, championSpellDto.getImage().getFull(), championSpellDto.getCooldownBurn(), championSpellDto.getRangeBurn(), championSpellDto.getCostBurn());
        System.out.println("------------- Object to return created - createActiveSpell -----------");
        return ret;
    }

    private static String stringifySpellScaling(String link, String locale) {
        String ret;
        switch (link) {
            case "spelldamage":
                ret = SPELL_DAMAGE_LOCALIZATION_MAP.get(locale);
                break;
            case "bonusattackdamage":
            case "attackdamage":
                ret = BONUS_ATTACK_DAMAGE_LOCALIZATION_MAP.get(locale);
                break;
            default:
                return "";
        }
        return ret;
    }

    public static void initLocalizationMaps() {
        BONUS_ATTACK_DAMAGE_LOCALIZATION_MAP.put("en_US", "AttackDamage");
        BONUS_ATTACK_DAMAGE_LOCALIZATION_MAP.put("es_ES", "Daño de Ataque");
        BONUS_ATTACK_DAMAGE_LOCALIZATION_MAP.put("it_IT", "attacco fisico");
        BONUS_ATTACK_DAMAGE_LOCALIZATION_MAP.put("fr_FR", "des dégâts d'attaque");
        BONUS_ATTACK_DAMAGE_LOCALIZATION_MAP.put("de_DE", "Angriffsschaden");
        BONUS_ATTACK_DAMAGE_LOCALIZATION_MAP.put("el_GR", "Ζημιάς Επίθεσης");
        BONUS_ATTACK_DAMAGE_LOCALIZATION_MAP.put("pl_PL", "obrażeń od ataku");
        BONUS_ATTACK_DAMAGE_LOCALIZATION_MAP.put("ro_RO", "daune din atac");
        BONUS_ATTACK_DAMAGE_LOCALIZATION_MAP.put("pt_PT", "de Dano de Ataque");
        BONUS_ATTACK_DAMAGE_LOCALIZATION_MAP.put("tr_TR", "Saldırı Gücü");
        SPELL_DAMAGE_LOCALIZATION_MAP.put("en_US", "Ability Power");
        SPELL_DAMAGE_LOCALIZATION_MAP.put("es_ES", "Poder de Habilidad");
        SPELL_DAMAGE_LOCALIZATION_MAP.put("it_IT", "potere magico");
        SPELL_DAMAGE_LOCALIZATION_MAP.put("fr_FR", "de la puissance");
        SPELL_DAMAGE_LOCALIZATION_MAP.put("de_DE", "Fähigkeitsstärke");
        SPELL_DAMAGE_LOCALIZATION_MAP.put("el_GR", "Ισχύος Ικανότητας");
        SPELL_DAMAGE_LOCALIZATION_MAP.put("pl_PL", "mocy umiejętności");
        SPELL_DAMAGE_LOCALIZATION_MAP.put("ro_RO", "puterea abilităţilor");
        SPELL_DAMAGE_LOCALIZATION_MAP.put("pt_PT", "de Poder de Habilidade");
        SPELL_DAMAGE_LOCALIZATION_MAP.put("tr_TR", "Yetenek Gücü");
    }
}
