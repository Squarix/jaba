package servlets;

import com.google.gson.Gson;
import com.sun.org.apache.xpath.internal.operations.Bool;
import dto.PostRequestBody;

import models.Post;
import services.PostService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.ResultSet;

@WebServlet(name = "Posts", urlPatterns = "/posts")
public class Posts extends HttpServlet {
  private PostService postService = new PostService();

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    req.setCharacterEncoding("UTF-8");

    PostRequestBody body = new Gson().fromJson(req.getReader(), PostRequestBody.class);
    Post createdPost = postService.createPost(body.post, req.getSession().getId());
    postService.createTags(createdPost, body.tags);
  }


  @Override
  protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String postId = req.getParameter("postId");
    postService.deletePost(Integer.parseInt(postId));
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String filter = req.getParameter("filter");

    ResultSet posts = postService.getPosts(filter);
    StringBuilder resultMessage = new StringBuilder();

    HttpSession session = req.getSession();
    Boolean isAdmin = (Boolean)session.getAttribute("isAdmin");

    try {
      int prevId = 0;
      while (posts.next()) {
        int id = posts.getInt("id");
        if (id != prevId) {
          if (prevId != 0) {
            resultMessage
              .append("<button class=\"btn\" onclick=\"like(" + prevId + ")\">Like</button>")
              .append("<button class=\"btn\" onclick=\"dislike(" + prevId + ")\">Dislike</button>")
              .append("<button class=\"btn\" onclick=\"comments('comments" + prevId + "')\">Comments</button>")
              .append((isAdmin != null && isAdmin != false) ? "<button class=\"btn\" onclick=\"deletePost(" + prevId + ")\">Delete</button>" : "")
              .append("<div id=\"comments" + prevId + "\"></div>")
              .append("</div>").append("</div>");
          }

          resultMessage
            .append("<div class=\"post\">")
            .append("<span>" + id + ": ")
            .append("<a href=\"" + posts.getString("link") + "\">" + posts.getString("link") + "</a>")
            .append("<span class=\"like\">+" + posts.getInt("likes") + "</span>")
            .append("<span class=\"dislike\">-" + posts.getInt("dislikes") + "</span>")
            .append("</span>")
            .append("<div class=\"tags\">")
            .append("<span class=\"tag\" onclick=\"tagClicked(" + posts.getInt("tagId") + ")\">" + posts.getString("tagName") + "</span>");
        } else {
          resultMessage.append("<span class=\"tag\" onclick=\"tagClicked(" + posts.getInt("tagId") + ")\">" + posts.getString("tagName") + "</span>");
        }

        prevId = id;
      }


      resultMessage
        .append("<button class=\"btn\" onclick=\"like(" + prevId + ")\">Like</button>")
        .append("<button class=\"btn\" onclick=\"dislike(" + prevId + ")\">Dislike</button>")
        .append("<button class=\"btn\" onclick=\"comments('comments" + prevId + "')\">Comments</button>")
        .append((isAdmin != null && isAdmin != false) ? "<button class=\"btn\" onclick=\"deletePost(" + prevId + ")\">Delete</button>" : "")
        .append("<div id=\"comments" + prevId + "\"></div>")
        .append("</div>").append("</div>");

      posts.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

    resp.setContentType("text/html; charset=UTF-8");
    resp.getWriter().write(resultMessage.toString());
  }
}
