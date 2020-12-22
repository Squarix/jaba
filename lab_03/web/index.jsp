<%--
  Created by IntelliJ IDEA.
  User: Sergei
  Date: 29.09.2017
  Time: 18:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Start Page</title>
  </head>
  <body>

    <div>
      <div>getRequestDispatcher</div>
      <a href="sss?fname=Vlados&lname=Poltos">GET</a>
      <form action="sss" method="post">
        <input type="hidden" name="fname" value="Vlados">
        <input type="hidden" name="lname" value="Poltos">
        <input type="submit" value="POST">
      </form>
    </div>

  <div>
    <div>getRedirect</div>
    <a href="ppp?fname=Vlados&lname=Poltos">GET</a>
    <form action="ppp" method="post">
      <input type="hidden" name="fname" value="Vlados">
      <input type="hidden" name="lname" value="Poltos">
      <input type="submit" value="POST">
    </form>
  </div>

  <div>
    <div>HttpClient</div>
    <a href="fff">send</a>
  </div>

  </body>
</html>
