<%--
  Created by IntelliJ IDEA.
  User: 28937
  Date: 2019/4/8
  Time: 9:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <h1>Hello World</h1>
</head>
<body>
    <form action="http://localhost/v1/pic/upload" method="post" enctype="multipart/form-data">
        <input type="file" name="uploadFile" />
        <button type="submit" id="but1">上传</button>
    </form>
</body>
</html>
