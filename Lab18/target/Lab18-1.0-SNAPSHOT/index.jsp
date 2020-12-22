<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
      <title>Lab18</title>
      <link rel="stylesheet" type="text/css" href="css/style.css">
      <script src="script/apiReferences.js" type="text/javascript"></script>
      <script src="script/general.js" type="text/javascript"></script>
      <script src="script/apiComments.js" type="text/javascript"></script>
  </head>
  <body onload="getRole()">
    <h1>Useful web-sites references</h1>
    <p>Username:<input type="text" id="authUsername"></p>
    <p>Password:<input type="text" id="authPassword"></p>
    <p>
        <input type="button" onclick="getRole()" value="Sign In">
        <input type="button" onclick="clearContent('form')" value="Cancel">
    </p>
    <div id="buttons"></div>
    <div id="form"></div><br>
    <div id="references"></div>
  </body>
</html>
