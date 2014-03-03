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
package org.lolin1.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;

import javax.net.ssl.HttpsURLConnection;

import org.lolin1.data.DataUpdater;

public abstract class Utils {

	private static final String API_KEY = "c236fa80-b223-487d-8358-41a4e94276b6",
			API_PARAM_NAME = "api_key";

	/**
	 * Recursively deletes a file or folder (even if it's not empty). This
	 * method firsts checks for the existence of the specified file, and then
	 * recursively deletes it, and, if any, all of its sub-folders.
	 * 
	 * @param pathToFile
	 *            {@link String} The file to be deleted.
	 * @return A boolean representing the success of the operation.
	 */
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
						Utils.delete(pathToFile + "/" + target);
					}
				}
			} catch (NullPointerException ex) {
			}
			return file.delete();
		}
		return false;
	}

	/**
	 * Note that, while this method is quite efficient, it's limited to files
	 * <16 MB.
	 * 
	 * @param url
	 * @param pathToFileToSaveTo
	 */
	public static File downloadFile(String url, Path pathToFileToSaveTo) {
		URL website = null;
		try {
			website = new URL(url.replaceAll(" ", "%20"));
		} catch (MalformedURLException e) {
			e.printStackTrace(System.err);
		}
		try (ReadableByteChannel rbc = Channels
				.newChannel(website.openStream());
				FileOutputStream fos = new FileOutputStream(
						pathToFileToSaveTo.toString());) {
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		} catch (IOException e) {
			e.printStackTrace(System.err);
			try {
				Thread.sleep(DataUpdater.getRetryDelayMillis());
			} catch (InterruptedException e1) {
				e1.printStackTrace(System.err);
			}
			return Utils.downloadFile(url, pathToFileToSaveTo);
		}

		return new File(pathToFileToSaveTo.toString());
	}

	public static final synchronized String performRiotGet(String url) {
		StringBuilder ret = new StringBuilder();
		URL obj = null;
		try {
			obj = new URL(url + ((url.contains("=")) ? "&" : "?")
					+ Utils.API_PARAM_NAME + "=" + Utils.API_KEY);
		} catch (MalformedURLException e) {
			e.printStackTrace(System.err);
		}

		HttpsURLConnection con = null;
		try {
			con = (HttpsURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
		} catch (IOException e) {
			e.printStackTrace(System.err);
		}

		try {
			if (con.getResponseCode() == 200) {

				BufferedReader in = new BufferedReader(new InputStreamReader(
						con.getInputStream()));
				String inputLine;
				StringBuilder response = new StringBuilder();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();

				ret.append(response.toString());
			}
		} catch (IOException e) {
			e.printStackTrace(System.err);
			try {
				Thread.sleep(DataUpdater.getRetryDelayMillis());
			} catch (InterruptedException e1) {
				e1.printStackTrace(System.err);
			}
			return Utils.performRiotGet(url);
		}

		return ret.toString();
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

	public static String toSystemJSON(String contentType, String content) {
		StringBuilder ret = new StringBuilder("{\"status\":\"ok\"");
		ret.append(",\"" + contentType + "\":\"" + content + "\"");
		ret.append("}");
		return ret.toString();
	}

	public static void writeFile(Path path, String contents) {
		try (PrintWriter pw = new PrintWriter(new FileWriter(path.toString(),
				Boolean.FALSE))) {
			pw.write(contents);
		} catch (IOException ex) {
			ex.printStackTrace(System.err);
		}
	}
}
