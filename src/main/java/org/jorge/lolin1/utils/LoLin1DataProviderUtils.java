package org.jorge.lolin1.utils;

import lol4j.util.Region;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
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
public abstract class LoLin1DataProviderUtils {

    public static boolean delete(String pathToFile) {
        File file, fileAux;

        if ((file = new File(pathToFile)).exists()) {
            try {
                for (String target : file.list()) {
                    if ((fileAux = new File(target)).isFile()) {
                        if (!fileAux.delete()) {
                            return false;
                        }
                    } else {
                        LoLin1DataProviderUtils.delete(Paths.get(pathToFile, target).toString());
                    }
                }
            } catch (NullPointerException ex) {
            }
            return file.delete();
        }
        return false;
    }

    public static List<String> doubleListAsStringList(List<Double> list) {
        List<String> ret = new ArrayList<>();
        for (Double x : list) {
            ret.add(x + "");
        }
        return ret;
    }

    public static List<String> integerListAsStringList(List<Integer> list) {
        List<String> ret = new ArrayList<>();
        for (Integer x : list) {
            ret.add(x + "");
        }
        return ret;
    }


    public static String joinIfDifferent(List<String> target, String joint) {
        if (target == null || target.isEmpty())
            return "";
        HashSet<String> testForDuplicates = new HashSet<>(target);
        if (testForDuplicates.size() != target.size())
            return target.get(0);//Duplicates detected!
        else {
            return StringUtils.join(target, joint);
        }
    }

    public static String readFile(Path path) {
        String pathToFile = path.toString(), line = "";
        try (BufferedReader br = new BufferedReader(new FileReader(pathToFile))) {
            while (true) {
                try {
                    line = line.concat(br.readLine());
                } catch (IOException ex) {
                    ex.printStackTrace(System.err);
                } catch (NullPointerException ex) {
                    break;
                }
                line = line.concat("\n");
            }
        } catch (final IOException ex) {
            if (ex instanceof FileNotFoundException) {
                return null;
            } else {
                ex.printStackTrace(System.err);
            }
        }

        try {
            return line.substring(0, line.length() - 1);
        } catch (final IndexOutOfBoundsException ex) {
            // If the file is empty.
            return "";
        }
    }

    public static void writeFile(Path path, String contents) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(path.toString(),
                Boolean.FALSE))) {
            pw.write(contents);
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
    }

    public static Region regionalizeString(String realm) {
        Region ret;
        switch (realm.toLowerCase()) {
            case "euw":
                ret = Region.EUW;
                break;
            case "eune":
                ret = Region.EUNE;
                break;
            case "br":
                ret = Region.BR;
                break;
            case "na":
                ret = Region.NA;
                break;
            case "tr":
                ret = Region.TR;
                break;
            case "ru":
                ret = Region.RU;
                break;
            case "kr":
                ret = Region.KR;
                break;
            case "oce":
                ret = Region.OCE;
                break;
            case "lan":
                ret = Region.LAN;
                break;
            case "las":
                ret = Region.LAS;
                break;
            default:
                ret = Region.UNKNOWN;
        }
        return ret;
    }
}
