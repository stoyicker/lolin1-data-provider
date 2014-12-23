package org.jorge.lolin1dp.services;

import javax.servlet.ServletException;
import javax.ws.rs.Produces;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jorge.lolin1dp.data.DataAccessObject;
import org.jorge.lolin1dp.datamodel.Realm;
import org.jorge.lolin1dp.datamodel.Realm.RealmEnum;

import java.io.IOException;
import java.util.List;

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
public final class NewsService extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6604410253121443798L;

	@Override
	@Produces("text/json")
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String parameterRealm, parameterLocale;
		try {
			parameterRealm = req.getParameter("realm").toLowerCase();
			parameterLocale = req.getParameter("locale").toLowerCase();
		} catch (NullPointerException ex) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		RealmEnum realm;
		if ((realm = convertStringToRealmEnum(parameterRealm)) == null
				|| parameterLocale == null
				|| !localeBelongsTo(parameterLocale, realm)) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		resp.setCharacterEncoding("UTF-8");
		resp.getWriter().print(
				DataAccessObject.getJSONNewsAsString(realm, parameterLocale));
	}

	private boolean localeBelongsTo(String locale, RealmEnum realm) {
		List<String> realmLocales = Realm.getLocales(realm);

		for (String x : realmLocales) {
			if (x.toLowerCase().contentEquals(locale.toLowerCase()))
				return Boolean.TRUE;
		}

		return Boolean.FALSE;
	}

	private RealmEnum convertStringToRealmEnum(String lowercaseRealmId) {
		RealmEnum ret;

		switch (lowercaseRealmId) {
		case "na":
			ret = RealmEnum.NA;
			break;
		case "euw":
			ret = RealmEnum.EUW;
			break;
		case "eune":
			ret = RealmEnum.EUNE;
			break;
		case "br":
			ret = RealmEnum.BR;
			break;
		case "lan":
			ret = RealmEnum.LAN;
			break;
		case "las":
			ret = RealmEnum.LAS;
			break;
		case "tr":
			ret = RealmEnum.TR;
			break;
		case "ru":
			ret = RealmEnum.RU;
			break;
		case "oce":
			ret = RealmEnum.OCE;
			break;
		default:
			ret = null;
		}
		return ret;
	}
}
