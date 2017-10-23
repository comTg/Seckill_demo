<%--
  Created by IntelliJ IDEA.
  User: tg
  Date: 17-10-21
  Time: 下午3:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!Doctype html>
<head>
    <title>登录</title>
    <%--从cdn引入font-awdsome字体css--%>
    <%--<link href="http://cdn.bootcss.com/font-awesome/4.6.3/css/font-awesome.min.css" rel="stylesheet">--%>
    <link href="/resources/css/font-awesome.min.css" rel="stylesheet">
    <link href="/resources/css/userPage.css" rel="stylesheet" />
    <%@include file="common/navbar.jsp"%>
</head>
<body>
<div class="container">
    <div class="row">
        <div class="col-md-offset-3 col-md-5">
            <form class="form-horizontal" action="/seckill/login" method="post">
                <span class="heading">用户登录</span>
                <!-- 出错提示信息 -->
                <span class="glyphicon error-tips" id="errorTips"><label class="label label-danger">${error}</label></span>
                <div class="form-group">
                    <input type="text" class="form-control" id="phone" name="phone" placeholder="手机号码">
                    <i class="glyphicon glyphicon-earphone"></i>
                </div>
                <div class="form-group help">
                    <input type="password" class="form-control" name="password" id="password" placeholder="密　码">
                    <i class="glyphicon glyphicon-lock"></i>
                </div>
                <div class="form-group">
                    <div style="float:left; width: 60%;">
                        <input type="text" class="form-control" id="validateCode" name="validateCode" placeholder="验证码" >
                        <i class="glyphicon glyphicon-exclamation-sign"></i>
                    </div>
                    <div style="float: right;overflow: visible !important;">
                        <img src="/seckill/loadValidateCode" id="validateCodeImage" name="validateCodeImage"
                             style="width: 100px;height: 35px;"  onclick="seckill.loadValidateCode()" >
                    </div>
                </div>
                <div class="form-group">
                    <div class="main-checkbox">
                        <input type="checkbox" value="None" id="checkbox1" name="check"/>
                        <label for="checkbox1"></label>
                    </div>
                    <span class="text">Remember me</span>
                    <button type="submit" class="btn btn-default">登录</button>
                </div>
                <input type="hidden" name="url" value="${url}" />
                <input type="hidden" name="judgeDuplicate" value="${token}"/>
            </form>
        </div>
    </div>
</div>
</body>
<script type="text/javascript">
    $(function(){
        seckill.interceptform();
    })
</script>
</html>
