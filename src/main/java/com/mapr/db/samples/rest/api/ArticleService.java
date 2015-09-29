package com.mapr.db.samples.rest.api;

import com.mapr.db.Condition;
import com.mapr.db.DBDocument;
import com.mapr.db.MapRDB;
import com.mapr.db.Mutation;
import com.mapr.db.Table;
import com.mapr.db.samples.rest.helper.JSONHelper;
import com.mapr.db.samples.rest.helper.MaprDBHelper;
import com.mapr.db.samples.rest.model.Article;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.ojai.Document;
import org.ojai.DocumentStream;

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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.mapr.db.Condition.Op.EQUAL;

@Api(value = "/articles", description = "API for Articles")
@Path("/articles")
@Produces(MediaType.APPLICATION_JSON)
public class ArticleService {

  public final static String TABLE_NAME = "/apps/blog/articles";

  Table table = null;

  public ArticleService() throws IOException {
    table = MaprDBHelper.getTable(TABLE_NAME);
  }

  @PUT
  @Path("/")
  @Consumes(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Create an article")
  public Response createCustomer(Article article) throws IOException, URISyntaxException {
    // TODO: use POJO when API supports it
    Document record = MapRDB.newDocument(article);
    table.insertOrReplace(record);
    table.flush();

    return Response.created(new URI("/api/articles/" + ((DBDocument)record).getIdString())).build();
  }


  @GET
  @Path("/")
  @ApiOperation(value = "Return all articles.<br/> See how to get all document")
  public Response getArticles() throws IOException {
    List<Map<String, Map>> items = new ArrayList<Map<String, Map>>();
    for (DBDocument document : table.find()) {
      items.add(JSONHelper.toMap(document));
    }
    return Response.ok(items).build();
  }


  @GET
  @Path("/{id}")
  @ApiOperation(value = "Return one Article based on his _id.<br/>This operation shows a simple get by id ")
  public Response getArticleById(@PathParam("id") String id) throws IOException {
    DBDocument record = table.findById(id);

    if (record == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }

    // TODO: fix to adap to v1)
    return Response.ok( JSONHelper.toMap(record) ).build();

  }

  @DELETE
  @Path("/{id}")
  @ApiOperation(value = "Delete one article by _id. <br/>This operation shows how to delete a document from MaprDB")
  public Response deleteArticleById(@PathParam("id") String id) throws IOException {
    table.delete(id);
    return Response.ok().build();
  }


  @POST
  @Path("/{id}/tags/{value}")
  @ApiOperation(value = "Add tag to an article with id.<br/>This operation shows how to add/update a array field ")
  public Response addInderest(
          @PathParam("id") String id,
          @PathParam("value") String value) throws IOException {

    // The following code will udpate the customer document and add/append an interest
    Mutation mutation = MapRDB.newMutation()
            .append("tags", Arrays.asList(new Object[]{value}))
            .build();
    table.update(id, mutation);
    table.flush();

    return Response.ok().build();

  }


  @PUT
  @Path("/like/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Like this article.<br/> This show how you can store dynamic content (Map)")
  public Response likeThisArticle(@PathParam("id") String id, Map likeAsMap) throws IOException, URISyntaxException {
    // TODO: use POJO when API supports it

    Mutation mutation = MapRDB.newMutation()
                                .increment("likes", 1);

    Timestamp ts =  new Timestamp( (new java.util.Date()).getTime()  );
    if ( likeAsMap.containsKey("author") ) {
        Map<String, Object> author =  ( Map<String, Object>)likeAsMap.get("author");
        author.put( "dateOfLike", ts  );
        mutation.append("likes_record", Arrays.asList(author));
    } else {
      Map<String, Object> newLike = new HashMap<String, Object>();
      newLike.put( "author.name", "anonymous" );
      newLike.put( "author._id", "anonymous" );
      newLike.put( "dateOfLike", ts );
      mutation.append("likes_record", Arrays.asList(newLike));
    }
    mutation.build();

    table.update(id, mutation);
    table.flush();

    return Response.created(new URI("/api/articles/" + id )).build();
  }


  @GET
  @Path("/by_tag/{tag}")
  @ApiOperation(value = "Find Articles by Tag.<br/>This operation shows how to query document on a specific field")
  public Response getArticlesByTag(@PathParam("tag") String tag) throws IOException {

    List<Map<String, Map>> articles = new ArrayList<Map<String, Map>>();

    // Create a condition
    // here it is a simple equal on a field OR a list
    Condition condition = MapRDB.newCondition()
            .or()
            .is("tags[]", EQUAL, tag) //  search in case it is an array
            .is("tags", EQUAL, tag) // search in case it is a scalar
            .close()
            .build();

    for (DBDocument document : table.find(condition)) {
      articles.add(JSONHelper.toMap(document));
    }


    return Response.ok(articles).build();

  }



}
