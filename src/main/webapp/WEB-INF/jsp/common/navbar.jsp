<%--
  Created by IntelliJ IDEA.
  User: tg
  Date: 17-9-26
  Time: 下午2:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="java.util.*" %>
<% HttpSession Pagesession = request.getSession();
Object origin =  Pagesession.getAttribute("userName");
String userName = "";
if(origin!=null){
    userName = origin.toString();
}
%>
<!Doctype html>
<head>
    <link href="/resources/css/index.css" rel="stylesheet"/>
    <%@include file="head.jsp" %>
</head>
<body>
<!-- 导航栏 -->
<nav class="navbar navbar-default navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar"
                    aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#">秒杀系统</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
            <ul class="nav navbar-nav">
                <li><a href="/seckill/list">首页</a></li>
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                       aria-expanded="false">我的秒杀<span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li><a href="#">未完成订单</a></li>
                        <li><a href="#">已完成订单</a></li>
                    </ul>
                </li>
            </ul>
            <ul class="nav navbar-nav navbar-right hidden" id="per-center">
                <li><a class="text-primary" type="button"><%=userName%></a></li>
                <li>
                    <a id="logout" type="button" href="/seckill/logout">退出</a>
                </li>
            </ul>
            <ul class="nav navbar-nav navbar-right" id="need-login">
                <li><a href="/seckill/register">注册</a></li>
                <li><a href="/seckill/login">登录</a></li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <form class="navbar-form navbar-right" action="/seckill/query" method="get">
                    <input type="text" class="form-control" name="orderName" placeholder="搜索">
                </form>
            </ul>
        </div><!--/.nav-collapse -->
    </div><!--/.container-fluid -->
</nav>
</body>
<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
<%--<script src="https://cdn.bootcss.com/jquery/2.1.1/jquery.min.js"></script>--%>
<script src="/resources/js/jquery.min.js"></script>
<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<%--<script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>--%>
<script src="/resources/js/bootstrap.min.js"></script>
<%--使用CDN 获取公共js http://www.bootcdn.cn/--%>
<%--jQuery Cookie操作插件--%>
<%--<script src="http://cdn.bootcss.com/jquery-cookie/1.4.1/jquery.cookie.min.js"></script>--%>
<script src="/resources/js/jquery.cookie.min.js"></script>
<script src="/resources/script/seckill.js"></script>
<script type="text/javascript">
    $(function(){
        // 判断是否登录
        var userName = "${userName}";
        seckill.judge_login(userName);
    })
</script>
</html>
