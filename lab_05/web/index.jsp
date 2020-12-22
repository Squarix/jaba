<%--
  Created by IntelliJ IDEA.
  User: Sergei
  Date: 24.11.2017
  Time: 1:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="bva" uri="custom.tag.lib.tdl" %>
<html>
  <head>
    <title>Tag Lib</title>
  </head>

  <body>
    <bva:dossier>
      <bva:name id="name-tag" value="Vlados" />
      <bva:surname id="surname-tag" value="Poltos" />
      <bva:sex id="sex-tag" />
      <bva:submit/>
    </bva:dossier>
  </body>

</html>
