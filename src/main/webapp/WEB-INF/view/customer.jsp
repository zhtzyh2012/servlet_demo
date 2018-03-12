<%--
  Created by IntelliJ IDEA.
  User: neutron
  Date: 18-3-9
  Time: 上午7:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
    <title>客户管理</title>
</head>
<body>
    <h1>客户列表</h1>

    <table>
        <tr>
            <th>客户名称</th>
            <th>联系人</th>
            <th>电话号码</th>
            <th>邮箱</th>
        </tr>

        <c:forEach var="customer" items="${customerList}">
            <tr>
                <td>${customer.name}</td>
                <td>${customer.contact}</td>
                <td>${customer.telephone}</td>
                <td>${customer.email}</td>
            </tr>
        </c:forEach>

    </table>
</body>
</html>
