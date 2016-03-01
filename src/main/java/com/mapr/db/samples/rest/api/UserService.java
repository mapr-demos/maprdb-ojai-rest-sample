package com.mapr.db.samples.rest.api;

import static org.ojai.store.QueryCondition.Op.EQUAL;

import com.mapr.db.MapRDB;
import com.mapr.db.Table;

import com.mapr.db.samples.rest.helper.MaprDBHelper;
import com.mapr.db.samples.rest.model.Address;
import com.mapr.db.samples.rest.model.User;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.ojai.Document;
import org.ojai.DocumentStream;
import org.ojai.store.DocumentMutation;
import org.ojai.store.QueryCondition;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Api(value = "/users", description = "API for the Users")
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserService {

  public final static String TABLE_NAME = "/apps/blog/users";

  Table table = null;


  public UserService() throws IOException {
    table = MaprDBHelper.getTable(TABLE_NAME);
  }


  @PUT
  @Path("/")
  @Consumes(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Create a user from a POJO")
  public Response createUser(User user) throws IOException, URISyntaxException {

    // TODO: use POJO when API supports it
    Document custRecord = MapRDB.newDocument(user);

    table.insertOrReplace(custRecord);
    table.flush();
    return Response.created(new URI("/api/users/" + ((Document)custRecord).getIdString())).build();
  }

  @PUT
  @Path("/from_string")
  @Consumes(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Create a user, from the JSON String without parsing.<br/>This is just for the demonstration to show flexible schema")
  public Response createUserFromString(String user) throws IOException, URISyntaxException {
    // TODO: use POJO when API supports it
    Document custRecord = MapRDB.newDocument(user);
    table.insertOrReplace(custRecord);
    table.flush();
    return Response.created(new URI("/api/users/" + ((Document)custRecord).getIdString() )).build();
  }

  @POST
  @Path("/{id}/nickname/{value}")
  @ApiOperation(value = "Set or update the nickname.<br/>This operation shows how to add/update a simple field ")
  public Response setNickName(
          @PathParam("id") String id,
          @PathParam("value") String value) throws IOException {

    // The following code will udpate the user document and add/append an interest
    DocumentMutation mutation = MapRDB.newMutation()
            .setOrReplace("nick_name", value);

    table.update(id, mutation);
    table.flush();
    return Response.ok().build();

  }

  @DELETE
  @Path("/{id}/nickname")
  @ApiOperation(value = "Delete nickname from user with id.<br/>This operation shows how to remove a field from a document")
  public Response removeNickname(
          @PathParam("id") String id) throws IOException {

    // The following code will udpate the user document and add/append an interest
    DocumentMutation mutation = MapRDB.newMutation()
            .delete("nick_name");

    table.update(id, mutation);
    table.flush();
    return Response.ok().build();

  }

  @POST
  @Path("/{id}/interests/{value}")
  @ApiOperation(value = "Add interest to user with id.<br/>This operation shows how to add/update a array field ")
  public Response addInderest(
          @PathParam("id") String id,
          @PathParam("value") String value) throws IOException {

    // The following code will udpate the user document and add/append an interest
    DocumentMutation mutation = MapRDB.newMutation()
            .append("interests", Arrays.asList(new Object[]{value}));

    table.update(id, mutation);
    table.flush();

    return Response.ok().build();

  }

  @DELETE
  @Path("/{id}/interests")
  @ApiOperation(value = "Delete all interest to user with id.<br/>This operation shows how to remove a field from a document")
  public Response removeInterests(
          @PathParam("id") String id) throws IOException {

    // The following code will udpate the user document and add/append an interest
    DocumentMutation mutation = MapRDB.newMutation()
            .delete("interests");

    table.update(id, mutation);
    table.flush();
    return Response.accepted().build();

  }

  @POST
  @Path("/{id}/addresses/")
  @Consumes(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Add an address to a user<br/>This operation shows how to create a sub document in a list ")
  public Response addAddress(
          @PathParam("id") String id,
          Address address) throws IOException {


    // Today it is not possible to add a Record into a Record... a Document into a Document
    // the work around is to use a Map
    // see http://10.250.1.5/bugzilla/show_bug.cgi?id=19733
    Map<String, String> addressAsMap = new HashMap<String, String>();
    addressAsMap.put("type", address.getType());
    addressAsMap.put("line", address.getLine());
    addressAsMap.put("city", address.getTown());
    addressAsMap.put("zip_code", address.getPostcode());
    addressAsMap.put("country", address.getCountry());

    // The following code will udpate the user document and add/append an interest
    DocumentMutation mutation = MapRDB.newMutation()
            .append("addresses", Arrays.asList(new Object[]{addressAsMap}));

    table.update(id, mutation);
    table.flush();


    return Response.ok().build();

  }

  @DELETE
  @Path("/{id}/addresses")
  @ApiOperation(value = "Delete all addresses to user with id.<br/>This operation shows how to remove a field from a document")
  public Response removeAddresses(
          @PathParam("id") String id) throws IOException {

    // The following code will update the user document and add/append an interest
    DocumentMutation mutation = MapRDB.newMutation()
            .delete("addresses");

    table.update(id, mutation);
    table.flush();
    return Response.ok().build();

  }


  @GET
  @Path("/{id}")
  @ApiOperation(value = "Return one User based on his _id.<br/>This operation shows a simple get by id ")
  public Response getUserById(@PathParam("id") String id) throws IOException {
    Document record = table.findById(id);

    if (record == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }

    return Response.ok( record.asMap() ).build();

  }


  @DELETE
  @Path("/{id}")
  @ApiOperation(value = "Delete one user by _id. <br/>This operation shows how to delete a document from MaprDB")
  public Response deleteUserById(@PathParam("id") String id) throws IOException {
    table.delete(id);
    return Response.ok().build();
  }


  @GET
  @Path("/")
  @ApiOperation(value = "Return all users.<br/> See how to get all document and do a projection")
  public Response getusers() throws Exception {

    List<Map<String, Object>> users = new ArrayList<Map<String, Object>>();
    DocumentStream rs = table.find();
    Iterator<Document> itrs = rs.iterator();
    Document document;
    while (itrs.hasNext()) {
      document = itrs.next();
      users.add(document.asMap());
    }
    rs.close();

    return Response.ok(users).build();
  }

  @GET
  @Path("/by_age/{age}")
  @ApiOperation(value = "Find User by Age.<br/>This operation shows how to query document on a specific field")
  public Response getusersByAge(@PathParam("age") int age) throws Exception {
    List<Map<String, Object>> users = new ArrayList<Map<String, Object>>();

    // Create a condition
    // here it is a simple equal on a field
    QueryCondition condition = MapRDB.newCondition()
            .is("age", EQUAL, age)
            .build();

    DocumentStream rs = table.find(condition);
    Iterator<Document> itrs = rs.iterator();
    Document document;
    while (itrs.hasNext()) {
      document = itrs.next();
      users.add(document.asMap());
    }
    rs.close();

    return Response.ok(users).build();

  }


  @GET
  @Path("/by_interest/{interest}")
  @ApiOperation(value = "Find User by Interest.<br/>This operation shows how to query document on a specific field")
  public Response getusersByAge(@PathParam("interest") String interest) throws Exception {

    List<Map<String, Object>> users = new ArrayList<Map<String, Object>>();
    List<String> results = new ArrayList<String>();

    // Create a condition
    // here it is a simple equal on a field OR a list
    QueryCondition condition = MapRDB.newCondition()
            .or()
            .is("interests[]", EQUAL, interest) //  search in case it is an array
            .is("interests", EQUAL, interest) // search in case it is a scalar
            .close()
            .build();

    DocumentStream rs = table.find(condition);
    Iterator<Document> itrs = rs.iterator();
    Document document;
    while (itrs.hasNext()) {
      document = itrs.next();
      users.add(document.asMap());
    }
    rs.close();

    return Response.ok(users).build();

  }


  @DELETE
  @Path("/delete_table")
  @ApiOperation(value = "Delete user table - This is a utility endpoint that should not be visible")
  public Response deleteUser() throws IOException {
    MaprDBHelper.deleteTable(TABLE_NAME);
    return Response.noContent().build();
  }







}
