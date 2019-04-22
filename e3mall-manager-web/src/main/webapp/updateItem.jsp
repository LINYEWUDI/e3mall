<%--
  Created by IntelliJ IDEA.
  User: 28937
  Date: 2019/4/9
  Time: 22:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
</head>
<body>
    <script>
        function test(){
            $.ajax({
                type:"PUT",
                url:"http://localhost/v1/item?itemId=536563",
                // url:"http://localhost/v1/item",
                // data:{itemId:536563}
                dataType:"json",
                success : function (data) {
                    alert(data.toString());
                    alert(data.message);
                }
            });
        }


    </script>

    <button name="but1" onclick="test()">测试修改商品信息</button>
</body>
</html>
