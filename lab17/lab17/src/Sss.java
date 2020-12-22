import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "Sss", urlPatterns = "/sss")
public class Sss extends HttpServlet {
     protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String x =req.getParameter("x");
        String y =req.getParameter("y");
        PrintWriter printWriter = resp.getWriter();
        printWriter.println("x = " + x);
        printWriter.println("y = " + y);
        printWriter.close();
    }
}