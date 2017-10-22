<%--
  Created by IntelliJ IDEA.
  User: tg
  Date: 17-9-26
  Time: 下午3:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!Doctype html>
<head>
    <title>注册页面</title>
    <%--从cdn引入font-awdsome字体css--%>
    <%--<link href="http://cdn.bootcss.com/font-awesome/4.6.3/css/font-awesome.min.css" rel="stylesheet">--%>
    <link href="/resources/css/font-awesome.min.css" rel="stylesheet">
    <link href="/resources/css/userPage.css" rel="stylesheet">
    <%@include file="common/navbar.jsp"%>
</head>
<body>
<div class="container">
    <div class="row">
        <div class="col-md-offset-3 col-md-6">
            <form class="form-horizontal" action="/seckill/register" method="post">
                <span class="heading">用户注册</span>
                <div class="form-group">
                    <input type="text" class="form-control" id="inputEmail3" name="phone" placeholder="手机号码" autocomplete="off">
                    <i class="glyphicon glyphicon-earphone"></i>
                </div>
                <div class="form-group">
                    <input type="text" class="form-control" id="inputName" name="name" placeholder="用户名" autocomplete="off">
                    <i class="glyphicon glyphicon-user"></i>
                </div>
                <div class="form-group help">
                    <input type="password" class="form-control" name="password" id="inputPassword3" autocomplete="off" placeholder="密　码">
                    <i class="glyphicon glyphicon-lock"></i>
                </div>
                <div class="form-group">
                    <button type="submit" class="btn btn-default btn-block">注册</button>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
<script src="/resources/js/jquery.min.js"></script>
<script src="/resources/js/bootstrap.min.js"></script>
</html>
