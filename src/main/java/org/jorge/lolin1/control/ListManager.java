package org.jorge.lolin1.control;


import lol4j.util.Region;
import org.jorge.lolin1.data.DataAccessObject;
import org.jorge.lolin1.utils.LoLin1DataProviderUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
public class ListManager {

    private final static String LISTS_DIR_NAME = "lists",
            CHAMPIONS_LIST_FILE_NAME = "champions.json";

    private static ListManager instance;

    public static String getChampionsListFileName() {
        return ListManager.CHAMPIONS_LIST_FILE_NAME;
    }

    public static ListManager getInstance() {
        if (ListManager.instance == null) {
            ListManager.instance = new ListManager();
        }
        return ListManager.instance;
    }

    public static String getListsDirName() {
        return ListManager.LISTS_DIR_NAME;
    }

    private ListManager() {
    }

    public void createChampionListsDirectory(Region realm, String locale) {
        LoLin1DataProviderUtils.delete(Paths.get(DataAccessObject.getChampionsDirName(),
                ListManager.getListsDirName(), realm.getName().toLowerCase(), locale).toString());
        try {
            Files.createDirectories(Paths.get(
                    DataAccessObject.getChampionsDirName(),
                    ListManager.getListsDirName(), realm.getName().toLowerCase(), locale));
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    public void setChampions(Region realm, String locale, String data) {
        LoLin1DataProviderUtils.writeFile(Paths.get(DataAccessObject.getChampionsDirName(),
                ListManager.getListsDirName(), realm.getName().toLowerCase(), locale,
                ListManager.getChampionsListFileName()), data.toString());
    }
}
