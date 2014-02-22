package org.lolin1.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public abstract class Utils {

	private static final String API_KEY = "c236fa80-b223-487d-8358-41a4e94276b6", // TODO
																					// Replace
																					// this
																					// from
																					// the
																					// public
																					// repo
																					// upon
																					// deployment
																					// for
																					// "YOUR_API_KEY_HERE"
			API_PARAM_NAME = "api_key";

	public static final synchronized String performRiotGet(String url) {
		StringBuilder ret = new StringBuilder();
		URL obj = null;
		try {
			obj = new URL(url + ((url.contains("=")) ? "&" : "?")
					+ Utils.API_PARAM_NAME + "=" + Utils.API_KEY);
		} catch (MalformedURLException e) {
			e.printStackTrace(System.err);
		}

		HttpURLConnection con = null;
		try {
			con = (HttpURLConnection) obj.openConnection();
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
		}

		return ret.toString();
	}
}
