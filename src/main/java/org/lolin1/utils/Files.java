package org.lolin1.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public abstract class Files {

	public static final String CHAMPIONS_FILE = "champions.json";

	public static final String readFile(String pathToFile) throws IOException {
		String line = "";
		try (BufferedReader br = new BufferedReader(new FileReader(pathToFile))) {
			while (true) {
				try {
					line = line.concat(br.readLine());
				} catch (IOException ex) {
					throw ex;
				} catch (NullPointerException ex) {
					break;
				}
				line = line.concat("\n");
			}
		} catch (final IOException ex) {
			throw ex;
		}

		try {
			return line.substring(0, line.length() - 1);
		} catch (final IndexOutOfBoundsException ex) {
			// If the file is empty.
			return "";
		}
	}
}
