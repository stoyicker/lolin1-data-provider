import lol4j.client.impl.Lol4JClientImpl;
import lol4j.util.Region;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.jorge.lolin1.data.DataAccessObject;

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

    private static final long UPDATE_PERIOD_SECONDS = 60 * 60 * 6;

    public static void main(String[] args) throws Exception {
        // The port that we should run on can be set into an environment
        // variable
        // Look for that variable and default to 8080 if it isn't there.
        String webPort = System.getenv("PORT");
        if ((webPort == null) || webPort.isEmpty()) {
            webPort = "8080";
        }

        Server server = new Server(Integer.valueOf(webPort));
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        context.addServlet(new ServletHolder(new Main()), "/*"); //TODO Set up the servlets properly

        DataAccessObject.initRealms();

        server.start();

        ScheduledExecutorService updateService = Executors
                .newScheduledThreadPool(1);
        updateService.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
//                DataUpdater.updateData();
                System.out.println(Lol4JClientImpl.getInstance().getRealm(Region.EUW).getDataTypeVersionMap().get("champion"));
            }
        }, 0, Main.UPDATE_PERIOD_SECONDS, TimeUnit.SECONDS);

        server.join();
    }
}
