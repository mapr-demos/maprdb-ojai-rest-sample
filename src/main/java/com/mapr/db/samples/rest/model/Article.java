package com.mapr.db.samples.rest.model;

import com.mapr.db.samples.rest.helper.Util;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Article {

  private String _id = null;

  private String title = null;

  private String content = null;

  private List<String> tags = new ArrayList<String>();

  private Author author = null;

  private Date createdAt = new Date( new java.util.Date().getTime() );

  public Article() {
  }

  public String get_id() {
    if (_id == null & title != null) {
      _id = Util.toSlug(createdAt + " " + title);
    }
    return _id;
  }

  public void set_id(String _id) {
    this._id = _id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;

    // create _id if null
    if ( this._id == null ) {
      _id = Util.toSlug( createdAt +" "+ title);
    }


  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  public Author getAuthor() {
    return author;
  }

  public void setAuthor(Author author) {
    this.author = author;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  @Override
  public String toString() {
    return "Article{" +
            "_id='" + _id + '\'' +
            ", title='" + title + '\'' +
            ", content='" + content + '\'' +
            ", tags=" + tags +
            ", author=" + author +
            ", createdAt=" + createdAt +
            '}';
  }

  class Author {

    private String id = null;
    private String name = null;

    public Author() {
    }

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    @Override
    public String toString() {
      return "Author{" +
              "id='" + id + '\'' +
              ", name='" + name + '\'' +
              '}';
    }
  }

}
