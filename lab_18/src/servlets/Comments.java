package servlets;

import com.google.gson.Gson;
import dto.PostRequestBody;

import models.Comment;
import models.Post;
import services.PostService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;

@WebServlet(name = "Comments", urlPatterns = "/comments")
public class Comments extends HttpServlet {
  private PostService postService = new PostService();

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    req.setCharacterEncoding("UTF-8");

    Comment body = new Gson().fromJson(req.getReader(), Comment.class);
    postService.createComment(body, req.getSession().getId());
  }

  @Override
  protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    req.setCharacterEncoding("UTF-8");

    Comment body = new Gson().fromJson(req.getReader(), Comment.class);
    postService.updateComment(body);
  }

  @Override
  protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    postService.deleteComment(Integer.parseInt(req.getParameter("id")));
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String id = req.getParameter("postId");

    ResultSet comments = postService.getComments(Integer.parseInt(id));
    StringBuilder resultMessage = new StringBuilder();

    String sessionId = req.getSession().getId();

    try {
      while (comments.next()) {
        StringBuilder body = new StringBuilder();
        if (sessionId.equals(comments.getString("sessionId"))) {
          body.append("<textarea id=\"updateComment\">" + comments.getString("text") + "</textarea>");
          body
            .append("<button onclick=\"deleteComment("+ comments.getInt("id") +") \">Delete</button>")
            .append("<button onclick=\"updateComment(" + comments.getInt("id") + ")\">Update</button>");
        } else {
          body.append("<p class=\"comment-body\">" + comments.getString("text") + "</p>");
        }

        resultMessage
          .append("<div class=\"comment\"")
          .append("<h5 class=\"comment-nn\">Name: " + comments.getString("nickname") + "</h5>")
          .append(body)
          .append("</div>");
      }

      comments.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

    resp.setContentType("text/html; charset=UTF-8");
    resp.getWriter().write(resultMessage.toString());
  }
}
