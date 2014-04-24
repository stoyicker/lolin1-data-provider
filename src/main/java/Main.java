import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.jorge.lolin1.data.DataAccessObject;
import org.jorge.lolin1.data.DataUpdater;
import org.jorge.lolin1.services.champions.CDNService;
import org.jorge.lolin1.services.champions.ListService;
import org.jorge.lolin1.services.champions.VersionService;
import sun.misc.Version;

import javax.servlet.http.HttpServlet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

public class Main extends HttpServlet {

    private static final long UPDATE_FREQUENCY_SECONDS = 60 * 60 * 6;

    public static void main(String[] args) throws Exception {
        String webPort = System.getenv("PORT");
        if ((webPort == null) || webPort.isEmpty()) {
            webPort = "8080";
        }

        Server server = new Server(Integer.valueOf(webPort));
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        context.addServlet(CDNService.class, "/*");
        context.addServlet(ListService.class, "/*");
        context.addServlet(VersionService.class,"/*");
        DataAccessObject.initRealms();

        server.start();

        ScheduledExecutorService updateService = Executors
                .newScheduledThreadPool(1);
        updateService.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                DataUpdater.updateData();
            }
        }, 0, Main.UPDATE_FREQUENCY_SECONDS, TimeUnit.SECONDS);

        server.join();
    }
}
