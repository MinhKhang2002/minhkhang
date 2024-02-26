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
          <a href="#">Thống kê News</a>
        </li>
      </ul><!-- /.breadcrumb -->
    </div>
    <div class="page-content">
      <div class="row" >
        <div class="col-xs-12">
          <div id="chart_news" style="width: 900px; height: 500px;"></div>
              <c:forEach var="item" items="${chartDataComment}">
                  <span>Số lương news trong ngày: </span>
                  <span>${item.numberOfPosts} </span>
                  <!-- Sử dụng fmt:formatDate để định dạng ngày tháng -->
                  <fmt:formatDate value="${item.date}" pattern="yyyy-MM-dd" />
              </c:forEach>
        </div>
      </div>
    </div>
  </div>
</div><!-- /.main-content -->
</body>
</html>