<%--
  Created by IntelliJ IDEA.
  User: 28937
  Date: 2019/4/8
  Time: 20:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

    <form id="itemAddForm" class="itemForm" method="post" action="http://localhost/v1/item" enctype="multipart/form-data">
        <table cellpadding="5">
            <tr>
                <td>
                    <input type="hidden" name="cid"></input>
                </td>
            </tr>
            <tr>
                <td>商品标题:</td>
                <td><input class="easyui-textbox" type="text" name="title"></input></td>
            </tr>
            <tr>
                <td>商品卖点:</td>
                <td><input class="easyui-textbox" name="sellPoint"></input></td>
            </tr>
            <tr>
                <td>商品价格:</td>
                <td><input class="easyui-numberbox" type="text" name="price"/>
                    <input type="hidden" name="price"/>
                </td>
            </tr>
            <tr>
                <td>库存数量:</td>
                <td><input class="easyui-numberbox" type="text" name="num"/></td>
            </tr>
            <tr>
                <td>条形码:</td>
                <td>
                    <input class="easyui-textbox" type="text" name="barcode"/>
                </td>
            </tr>
            <tr>
                <td>商品图片:</td>
                <td>
                    <input type="file" name="uploadFile" />
                </td>
            </tr>
            <tr>
                <td>商品描述:</td>
                <td>
                    <textarea name="desc"></textarea>
                </td>
            </tr>
            <tr class="params hide">

            </tr>
        </table>
        <input type="hidden" name="itemParams"/>

        <input type="submit" value="提交">
    </form>

</body>
</html>
