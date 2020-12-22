package services;

import models.Comment;
import models.Post;
import models.Tag;

import javax.swing.plaf.nimbus.State;
import java.sql.*;

public class PostService {
  // JDBC driver name and database URL
  private static String driverName = "org.postgresql.Driver";
  private static String url = "jdbc:postgresql://localhost:5432/";

  // defined and set value in  dbName, userName and password variables
  private static String dbName = "final_lab";
  private static String userName = "admin";
  private static String password = "admin";


  public void updateComment(Comment comment) {
    try {
      Class.forName(driverName);

      Connection con = DriverManager.getConnection(url + dbName, userName, password);
      PreparedStatement stmt = con.prepareStatement("UPDATE comments SET text = ? WHERE id = ?");
      stmt.setString(1, comment.Text);
      stmt.setInt(2, comment.Id);

      stmt.executeUpdate();

      stmt.close();
      con.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  public void deleteComment(int commentId) {
    try {
      Class.forName(driverName);

      Connection con = DriverManager.getConnection(url + dbName, userName, password);
      Statement statement = con.createStatement();
      statement.executeUpdate("delete from comments where \"id\" = " + commentId + ";");

      statement.close();
      con.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void deletePost(int postId) {
    try {
      Class.forName(driverName);

      Connection con = DriverManager.getConnection(url + dbName, userName, password);
      Statement statement = con.createStatement();
      statement.executeUpdate("delete from posts where id = "+ postId + ";");
      statement.executeUpdate("delete from comments where \"postId\" = " + postId + ";");
      statement.executeUpdate("delete from posts_filters where \"postId\" = " + postId + ";");

      statement.close();
      con.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void createComment(Comment comment, String sessionId) {
    try {
      Class.forName(driverName);

      Connection con = DriverManager.getConnection(url + dbName, userName, password);
      PreparedStatement stmt = con.prepareStatement("INSERT INTO comments(text, \"postId\", nickname, \"sessionId\") VALUES (?,?,?, ?);");
      stmt.setString(1, comment.Text);
      stmt.setInt(2, comment.PostId);
      stmt.setString(3, comment.Nickname);
      stmt.setString(4, sessionId);

      stmt.executeUpdate();


      con.close();
      stmt.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void dislikePost(int postId) {
    try {
      Class.forName(driverName);

      Connection con = DriverManager.getConnection(url + dbName, userName, password);
      PreparedStatement stmt = con.prepareStatement("UPDATE posts SET dislikes = dislikes + 1 WHERE id = ?");
      stmt.setInt(1, postId);

      stmt.executeUpdate();


      con.close();
      stmt.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void likePost(int postId) {
    try {
      Class.forName(driverName);

      Connection con = DriverManager.getConnection(url + dbName, userName, password);
      PreparedStatement stmt = con.prepareStatement("UPDATE posts SET likes = likes + 1 WHERE id = ?");
      stmt.setInt(1, postId);

      stmt.executeUpdate();

      con.close();
      stmt.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void createTags(Post post, Tag[] tags) {
    Connection con = null;
    ResultSet resultSet = null;

    try {
      Class.forName(driverName);

      con = DriverManager.getConnection(url + dbName, userName, password);
      for (Tag tag: tags) {
        PreparedStatement select = con.prepareStatement("SELECT id from tags where name = ?");
        select.setString(1, tag.Name);
        ResultSet res = select.executeQuery();

        int tagId;

        if (res.next()) {
          tagId = res.getInt("id");
          select.close();
        } else {
          PreparedStatement stmt = con.prepareStatement("INSERT INTO tags(name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
          stmt.setString(1, tag.Name);

          stmt.executeUpdate();

          ResultSet keys = stmt.getGeneratedKeys();
          keys.next();

          tagId = keys.getInt(1);
          stmt.close();
        }

        PreparedStatement postTag = con.prepareStatement("INSERT INTO posts_tags(\"postId\", \"tagId\") VALUES(?,?)", Statement.RETURN_GENERATED_KEYS);
        postTag.setInt(1, post.Id);
        postTag.setInt(2, tagId);

        postTag.executeUpdate();

        postTag.close();
      }


      con.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public Post createPost(Post post, String sessionId) {
    Connection con = null;
    ResultSet resultSet = null;

    try {
      Class.forName(driverName);

      con = DriverManager.getConnection(url + dbName, userName, password);
      PreparedStatement stmt = con.prepareStatement("INSERT INTO posts(link, \"sessionId\") VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
      stmt.setString(1, post.Link);
      stmt.setString(2, sessionId);

      int affectedRows = stmt.executeUpdate();

      if (affectedRows == 1) {
          ResultSet generatedKeys = stmt.getGeneratedKeys();
          if (generatedKeys.next()) {
            post.Id = generatedKeys.getInt(1);
          }
      }

      con.close();
      stmt.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return post;
  }

  public ResultSet getPosts(String filter) {
    Connection con = null;
    Statement stmt = null;
    ResultSet resultSet = null;

    try {
      Class.forName(driverName);
      con = DriverManager.getConnection(url + dbName, userName, password);
      try {
        stmt = con.createStatement();
        String query = "Select p.id as id, link, likes, dislikes, t.id as \"tagId\", name as \"tagName\" from posts p left join posts_tags on posts_tags.\"postId\" = p.id left join tags t on t.id = posts_tags.\"tagId\" where posts_tags.\"tagId\" "
          + (filter == null ? "NOTNULL" : " = " + filter) + ";";

        resultSet = stmt.executeQuery(query);
      } catch (SQLException s) {
        s.printStackTrace();
      }

      con.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return resultSet;
  }

  public ResultSet getComments(int id) {
    Connection con = null;
    Statement stmt = null;
    ResultSet resultSet = null;

    try {
      Class.forName(driverName);
      con = DriverManager.getConnection(url + dbName, userName, password);
      try {
        stmt = con.createStatement();
        String query = "Select id, text, nickname, \"sessionId\" from comments where \"postId\" = " + id + ";";

        resultSet = stmt.executeQuery(query);
      } catch (SQLException s) {
        s.printStackTrace();
      }

      con.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return resultSet;
  }
}
