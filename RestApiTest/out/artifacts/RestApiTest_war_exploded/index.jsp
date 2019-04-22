<%-- Created by IntelliJ IDEA. --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>来，我来测试一下springmvc</title>
    <script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
    <script>
      $(function () {
        alert();
        $.ajax({
          url : "http://localhost:8080/v1/ajax",
          type : "post",
          data : '{ "username": "doudou", "password": "123" }',
          contentType: 'application/json',
          success : function (data) {
            console.log(data);
            alert(data.username);
          }

        });
      });

    </script>
  </head>
  <body>

  <form method="post" action="http://localhost:8080/v1/test">
    <input type="text" name="username" />
    <input type="password" name="password" />
    <input type="submit" value="登录"/>
  </form>

  </body>
</html>