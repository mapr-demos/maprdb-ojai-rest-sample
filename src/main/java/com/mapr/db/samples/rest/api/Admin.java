package com.mapr.db.samples.rest.api;

import com.mapr.db.MapRDB;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * This service is used to do admin tasks
 */
@Api(value = "/admin", description = "Admin tools (should be protected in production)")
@Path("/admin")
@Produces(MediaType.APPLICATION_JSON)
public class Admin {

  /**
   * Delete tables
   * @return
   */
  @POST
  @Path("/reset/")
  @ApiOperation(value = "Reset demonstration - delete tables")
  public Response reset() {

    if (MapRDB.tableExists( ArticleService.TABLE_NAME )) {
      MapRDB.deleteTable( ArticleService.TABLE_NAME );
    }

    if (MapRDB.tableExists( UserService.TABLE_NAME )) {
      MapRDB.deleteTable( UserService.TABLE_NAME );
    }

    return Response.ok().build();
  }

}
