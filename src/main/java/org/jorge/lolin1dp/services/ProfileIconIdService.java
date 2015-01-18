package org.jorge.lolin1dp.services;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.jorge.lolin1dp.io.net.HTTPRequestsSingleton;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

/**
 * This file is part of lolin1dp-data-provider.
 * <p>
 * lolin1dp-data-provider is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * <p>
 * lolin1dp-data-provider is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * <p>
 * You should have received a copy of the GNU General Public License along with
 * lolin1dp-data-provider. If not, see <http://www.gnu.org/licenses/>.
 */
public class ProfileIconIdService extends HttpServlet {

    private final static String BASE_REQUEST_URL = "https://%s.api.pvp.net/api/lol/%s/v1" +
            ".4/summoner/by-name/%s?api_key=%s";
    private final String mApiKey;

    public ProfileIconIdService(String api_key) {
        mApiKey = api_key;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        final String username = req.getParameter("username"), realm = req.getParameter("realm");
        if (username == null || realm == null) {
            resp.getWriter().print("err");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        final String url = String.format
                (Locale.ENGLISH,
                        BASE_REQUEST_URL, realm, realm, username, mApiKey).toLowerCase(Locale.ENGLISH);
        System.out.println("URL: " + url);
        final Response r = HTTPRequestsSingleton.getInstance().performRequest(new Request.Builder().url(url).get()
                .build());
        resp.getWriter().print(r.body().string());
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
