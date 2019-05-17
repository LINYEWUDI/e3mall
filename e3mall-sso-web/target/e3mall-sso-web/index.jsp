<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>
<script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
<script>

    function sendJson() {
        $.ajax({
            url : "http://localhost/v1/sso/user/jsonp/SESSION_ID:a25e1ee7-fece-4f14-aca5-53b7b5eb68cf?callback=tim",
            dataType : "jsonp",
            type : "GET",
            success : function (data) {
                if(data.status == 200){
                    alert(data.data.username);
                }
            }
        });
    }

</script>
<form method="post" action="http://localhost/v1/sso/user/login" >

    用户名：<input type="text" name="username" /><br><br>
    密码：<input type="password" name="password" /><br><br>
    <input type="submit">
    <br>
    <br>

    <input type="button" value="发送AJAX请求" onclick="sendJson()">
</form>
</body>
</html>
