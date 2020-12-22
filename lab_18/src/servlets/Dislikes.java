package servlets;


import com.google.gson.Gson;
import models.Dislike;
import models.Like;
import services.PostService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "Dislikes", urlPatterns = "/dislikes")
public class Dislikes extends HttpServlet {
  private PostService postService = new PostService();

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    req.setCharacterEncoding("UTF-8");

    Dislike body = new Gson().fromJson(req.getReader(), Dislike.class);
    postService.dislikePost(body.PostId);
  }
}
