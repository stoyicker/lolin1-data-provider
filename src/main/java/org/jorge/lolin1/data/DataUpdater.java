package org.jorge.lolin1.data;

import lol4j.client.impl.Lol4JClientImpl;
import lol4j.protocol.dto.lolstaticdata.ChampionDto;
import lol4j.protocol.dto.lolstaticdata.ChampionListDto;
import lol4j.util.Region;
import lol4j.util.lolstaticdata.ChampData;
import org.jorge.lolin1.control.ListManager;
import org.jorge.lolin1.models.champion.Champion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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

public abstract class DataUpdater {

    //    private static final String REALM_PLACE_HOLDER = "LOWERCASE_REALM_HERE",
//            LOCALE_PLACE_HOLDER = "LOCALE_MIXED_CASE_HERE",
//            REALM_FILE_URL = "https://prod.api.pvp.net/api/lol/static-data/"
//                    + DataUpdater.REALM_PLACE_HOLDER + "/v1/realm",
//            ALL_CHAMPIONS_URL = "https://prod.api.pvp.net/api/lol/static-data/"
//                    + DataUpdater.REALM_PLACE_HOLDER + "/v1/champion?locale="
//                    + DataUpdater.LOCALE_PLACE_HOLDER
//                    + "&champData=lore,tags,stats,spells,passive,image,skins",
//            INFO_WRAPPER = "n", VERSION_KEY = "champion", CDN_KEY = "cdn";
    private static final long RETRY_DELAY_MILLIS = 300000;

    private static Boolean UPDATING = Boolean.TRUE; // If it starts being TRUE,
    // the server start-up is
    // immediate

    public static long getRetryDelayMillis() {
        return DataUpdater.RETRY_DELAY_MILLIS;
    }

    public static boolean isUpdating() {
        return DataUpdater.UPDATING;
    }

    @SuppressWarnings("unchecked")
    private static void performUpdate(final Region realm, String locale,
                                      String newVersion) {
        DataUpdater.UPDATING = Boolean.TRUE;
        ListManager.getInstance().createChampionListsDirectory(realm, locale);
        ChampionListDto rawChampions;
        Collection<ChampionDto> champions;
        try {
            rawChampions = Lol4JClientImpl.getInstance().getChampionList(realm, locale, null, Arrays.asList(new ChampData[]{ChampData.ALL}));
            champions = rawChampions.getData().values();
        } catch (NullPointerException ex) {
            // No internet, so wait and retry
            try {
                Thread.sleep(DataUpdater.getRetryDelayMillis());
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
            DataUpdater.performUpdate(realm, locale, newVersion);
            return;
        }
        List<Champion> targetChampions = new ArrayList<>();
        for (ChampionDto champion : champions) {
            final Champion thisChampion = new Champion(champion, locale);
            targetChampions.add(thisChampion);
        }
        ListManager.getInstance().setChampions(realm, locale, "{\"status\":\"ok\", \"list\":" + DataAccessObject.formatChampionListAsJSON(targetChampions) + "}");
        DataAccessObject.setChampionsVersion(realm, newVersion);
        DataUpdater.UPDATING = Boolean.FALSE;
    }

    public static void updateData() {
        for (Region realm : DataAccessObject.getSupportedRealms().keySet()) {
            String newVersion = Lol4JClientImpl.getInstance().getRealm(realm).getDataTypeVersionMap().get("champion");
            if (!DataAccessObject.getVersion(realm)
                    .contentEquals(newVersion)) {
                DataAccessObject.putCDN(realm, Lol4JClientImpl.getInstance().getRealm(realm).getCdnBaseUrl());
                for (String locale : DataAccessObject.getSupportedRealms().get(
                        realm)) {
                    DataUpdater.performUpdate(realm, locale, newVersion);
                }
            }
        }
    }
}
