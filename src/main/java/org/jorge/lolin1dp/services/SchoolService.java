package org.jorge.lolin1dp.services;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jorge.lolin1dp.data.DataAccessObject;
import org.jorge.lolin1dp.data.DataUpdater;

import java.io.IOException;

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
public final class SchoolService extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8391286236902795916L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (DataUpdater.isUpdating()) {
			resp.sendError(HttpServletResponse.SC_CONFLICT);
			return;
		}

		resp.getWriter().print(DataAccessObject.getJSONSchoolAsString());
	}
}
