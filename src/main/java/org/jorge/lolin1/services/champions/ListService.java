package org.jorge.lolin1.services.champions;

import org.jorge.lolin1.data.DataAccessObject;
import org.jorge.lolin1.data.DataUpdater;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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

public class ListService extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String ret, realm = req.getParameter("realm"), locale = req.getParameter("locale");
        if (realm == null || realm.isEmpty() || locale == null || locale.isEmpty())
            ret = DataAccessObject.getResponseUnsupported();
        else if (DataUpdater.isUpdating()) {
            resp.sendError(HttpServletResponse.SC_CONFLICT);
            return;
        } else
            ret = DataAccessObject.getJSONList(realm, locale);
        resp.getWriter().print(ret);
    }
}
