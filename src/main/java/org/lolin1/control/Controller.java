/**
 * This file is part of lolin1-data-provider.

    lolin1-data-provider is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    lolin1-data-provider is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with lolin1-data-provider.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.lolin1.control;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lolin1.models.champion.Champion;

public final class Controller {

	private class Pair {
		private final String partOne, partTwo;

		private Pair(String _one, String _two) {
			this.partOne = _one;
			this.partTwo = _two;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return Boolean.TRUE;
			}
			if (!(o instanceof Pair)) {
				return Boolean.FALSE;
			}
			Pair target = (Pair) o;
			return this.partOne.contentEquals(target.partOne)
					&& this.partTwo.contentEquals(target.partTwo);
		}

		@Override
		public int hashCode() {
			int sum = 0, i = 1;
			StringBuilder everything = new StringBuilder(this.partOne);
			everything.append(this.partTwo);
			for (char x : everything.toString().toCharArray()) {
				sum += Character.getNumericValue(x) * i;
				i++;
			}
			return sum;
		}
	}

	private final static String CHAMPION_DIR_NAME = "champs";

	private static Controller singleton;

	private final static Map<Pair, List<Champion>> CHAMPIONS = new HashMap<>();

	public static String getChampionsDirName() {
		return Controller.CHAMPION_DIR_NAME;
	}

	public static Controller getInstance() {
		if (Controller.singleton == null) {
			Controller.singleton = new Controller();
		}
		return Controller.singleton;
	}

	private Controller() {
	}

	public List<Champion> getChampions(String locale, String realm) {
		return Controller.CHAMPIONS.get(new Pair(locale, realm));
	}

	public boolean isPairSupported(String locale, String realm) {
		return Controller.CHAMPIONS.containsKey(new Pair(locale, realm));
	}

	public void setChampions(String locale, String realm,
			List<Champion> champions) {
		Controller.CHAMPIONS.put(new Pair(locale, realm), champions);
	}
}
