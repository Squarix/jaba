package servlets;


import com.google.gson.Gson;
import dto.PostRequestBody;
import models.Like;
import services.PostService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "Likes", urlPatterns = "/likes")
public class Likes extends HttpServlet {
  private PostService postService = new PostService();

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    req.setCharacterEncoding("UTF-8");

    Like body = new Gson().fromJson(req.getReader(), Like.class);
    postService.likePost(body.PostId);
  }
}
