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
package org.lolin1.services.champions;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.lolin1.data.DataAccessObject;

@Path("/champions/version/{realm}")
@Produces("application/json")
public final class VersionService {

	@GET
	public final String get(@PathParam("realm") String realm) {
		return DataAccessObject.getJSONVersion(realm);
	}
}
