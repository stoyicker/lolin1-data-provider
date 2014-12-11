package org.jorge.lolin1dp.io.file;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This file is part of lolin1dp-data-provider.
 * <p/>
 * lolin1dp-data-provider is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * <p/>
 * lolin1dp-data-provider is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * <p/>
 * You should have received a copy of the GNU General Public License along with
 * lolin1dp-data-provider. If not, see <http://www.gnu.org/licenses/>.
 */
public abstract class FileUtils {

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
						FileUtils.delete(Paths.get(pathToFile, target)
								.toString());
					}
				}
			} catch (NullPointerException ex) {
			}
			return file.delete();
		}
		return false;
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

	public static void createXMLFileIfNotExists(Path path) {
		if (!Files.exists(path)) {
			try {
				new File(path.toString()).createNewFile();
			} catch (IOException e) {
				throw new IllegalStateException("File " + path.toAbsolutePath()
						+ " could not be created.");
			}
			writeFile(path, "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
		} else
			System.out.println("File " + path.toString()
					+ " already exists, so not recreated");
	}
}
