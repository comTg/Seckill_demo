<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: tg
  Date: 17-10-4
  Time: 下午2:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>秒杀列表页</title>
    <%--<jsp:include page="common/navbar.jsp" flush="true" />--%>
    <%@include file="common/navbar.jsp"%>
    <%@include file="common/tag.jsp"%>
</head>
<body>
<!-- 页面显示部分 -->
<div class="container">
    <div class="panel panel-default">
        <div class="panel-heading text-center">
            <h2>${type}列表</h2>
        </div>
        <div class="panel-body">
            <table class="table table-hover">
                <thead>
                    <tr>
                        <th>名称</th>
                        <th>库存</th>
                        <th>开始时间</th>
                        <th>结束时间</th>
                        <th>创建时间</th>
                        <th>详情页</th>
                    </tr>
                </thead>
                <tbody>
                     <c:forEach var="sk" items="${list}">
                         <tr class="orderList">
                             <td>${sk.name}</td>
                             <td>${sk.number}</td>
                             <td><fmt:formatDate value="${sk.startTime}"  pattern="yyyy-MM-dd HH:mm:ss" /> </td>
                             <td><fmt:formatDate value="${sk.endTime}" pattern="yyyy-MM-dd HH:mm:ss" /> </td>
                             <td><fmt:formatDate value="${sk.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /> </td>
                             <td><a class="btn btn-info" href="/seckill/${sk.seckillId}/detail" target="_blank">详情</a></td>
                         </tr>
                     </c:forEach>
                </tbody>
            </table>
            <span class="text-center center-block emptyTips">${listEmpty}</span>
        </div>
        <div class="row clearfix">
            <div class="text-center center-block">
                <ul class="pagination">
                    <c:choose>
                        <c:when test="${page==null}">
                        </c:when>
                        <c:when test="${page!=null}">
                            <c:if test="${page.pageNow-1>0}">
                                <li>
                                    <a href="/seckill/list?page=${page.pageNow-1}">&lt;</a>
                                </li>
                            </c:if>
                            <c:forEach var="i" begin="1" end="${page.totalPageCount}">
                                <li <c:if test="${page.pageNow==i}">
                                    <c:out value="class=active" />
                                </c:if>>
                                    <a href="/seckill/list?page=${i}">${i}</a>
                                </li>
                            </c:forEach>
                            <c:if test="${page.pageNow-page.totalPageCount<0}">
                                <li>
                                    <a href="/seckill/list?page=${page.pageNow+1}">&gt;</a>
                                </li>
                            </c:if>
                        </c:when>
                    </c:choose>
                </ul>
            </div>
        </div>
    </div>
</div>
</body>
</html>