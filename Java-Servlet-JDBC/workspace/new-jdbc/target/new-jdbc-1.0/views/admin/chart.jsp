<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Trang chủ</title>
</head>
<body>
<div class="main-content">
    <div class="main-content-inner">
        <div class="breadcrumbs ace-save-state" id="breadcrumbs">
            <ul class="breadcrumb">
                <li>
                    <i class="ace-icon fa fa-home home-icon"></i>
                    <a href="#">Thống kê</a>
                </li>
            </ul><!-- /.breadcrumb -->
        </div>
        <div class="page-content">
            <%--<div class="row" >
                <div class="col-xs-12">
                    <div id="chart_div" style="width: 900px; height: 500px;"></div>
&lt;%&ndash;                    <c:forEach var="item" items="${chartDataComment}">&ndash;%&gt;
&lt;%&ndash;                        <span>Số lương comment trong ngày: </span>&ndash;%&gt;
&lt;%&ndash;                        <span>${item.numberOfComments} </span>&ndash;%&gt;
&lt;%&ndash;                        <!-- Sử dụng fmt:formatDate để định dạng ngày tháng -->&ndash;%&gt;
&lt;%&ndash;                        <fmt:formatDate value="${item.date}" pattern="yyyy-MM-dd" />&ndash;%&gt;
&lt;%&ndash;                    </c:forEach>&ndash;%&gt;
                </div>
            </div>--%>
                <div class="row">
                    <div class="col-xs-12">
                        <div id="chart_div_comments_and_news" style="height: 400px;"></div>
                    </div>
                </div>
        </div>
    </div>
</div><!-- /.main-content -->
</body>
</html>