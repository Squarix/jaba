package servlets;

import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;


@WebServlet(name = "ManipulatorDB", urlPatterns = "/database")
public class ManipulatorDB extends HttpServlet {
    // JDBC driver name and database URL
    private static String driverName = "com.mysql.jdbc.Driver";
    private static String url = "jdbc:postgresql://localhost:5432/";

    // defined and set value in  dbName, userName and password variables
    private static String dbName = "lab18";
    private static String userName = "admin";
    private static String password = "admin";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // create Connection con, and Statement stmt
        Connection con;
        Statement stmt=null;
        try{
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(url+dbName, userName, password);
            try{
                String faculty = request.getParameter("faculty");
                String name = request.getParameter("name");
                stmt = con.createStatement();
                String query = String.format("call insertToT1('%s', '%s')", faculty, name);
                ResultSet rs=stmt.executeQuery(query);
                // close ResultSet rs
                rs.close();
                response.getWriter().println("insert");
            } catch(SQLException s){
                s.printStackTrace();
                response.getWriter()
                        .println(s.getMessage());
            }
            // close Connection and Statement
            con.close();
            stmt.close();
        }catch (Exception e){
            e.printStackTrace();
            response.getWriter()
                    .println(e.getMessage());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // create Connection con, and Statement stmt
        Connection con=null;
        Statement stmt=null;
        try{
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(url+dbName, userName, password);
            try{
                stmt = con.createStatement();
                String query = "SELECT * FROM faculty limit " + request.getParameter("limit");
                ResultSet rs=stmt.executeQuery(query);
                //Extact result from ResultSet rs

                StringBuilder resultMessage = new StringBuilder();
                while(rs.next()){
                    resultMessage
                            .append("<div>")
                            .append(rs.getString("faculty")).append("   ").append(rs.getString("faculty_name"))
                            .append("</div>");
                }
                // close ResultSet rs
                rs.close();
                response.getWriter().println(resultMessage);
            } catch(SQLException s){
                s.printStackTrace();
                response.getWriter()
                        .println(s.getMessage());
            }
            // close Connection and Statement
            con.close();
            stmt.close();
        }catch (Exception e){
            e.printStackTrace();
            response.getWriter()
                    .println(e.getMessage());
        }
    }
}
