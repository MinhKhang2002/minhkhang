<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglib.jsp"%>	
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Trang chi tiết</title>
</head>
<body>
<%
    // Lấy giá trị id từ request
    String id = request.getParameter("id");

    // Chuyển id thành newId
    Long newId = Long.parseLong(id);
%>

<!-- Sử dụng newId trong trang -->
<p>New ID: <%= newId %></p>

    <!-- Product section-->
    <section class="py-5">
        <div class="container px-4 px-lg-5 my-5">
            <div class="row gx-4 gx-lg-5 align-items-center">
<%--                <div class="col-md-6"><img class="card-img-top mb-5 mb-md-0" src="https://dummyimage.com/600x700/dee2e6/6c757d.jpg" alt="..." /></div>--%>
                <div class="col-md-6"><img class="card-img-top mb-5 mb-md-0" src="${model.thumbnail}" alt="..." /></div>
                <div class="col-md-6">
                    <div class="small mb-1">
                        <c:choose>
                            <c:when test="${not empty model.categoryCode}">
                                <c:forEach var="item" items="${categories}">
                                    <c:if test="${item.code == model.categoryCode}">
                                        <p style="font-size: 20px;">Thể loại: ${item.name}</p>
                                    </c:if>
                                </c:forEach>
                            </c:when>
                        </c:choose>
                    </div>
                    <h1 class="display-5 fw-bolder">${model.title}</h1>
<%--                    <p class="abc">${model}</p>--%>
                    <div class="fs-5 mb-5">
                        <span class="text-decoration-line-through">Mô tả ngắn</span>
                        <span>${model.shortDescription}</span>
                    </div>
                    <p class="lead">${model.content}</p>
                    <%--<div class="d-flex">
                        <input class="form-control text-center me-3" id="inputQuantity" type="num" value="1" style="max-width: 3rem" />
                        <button class="btn btn-outline-dark flex-shrink-0" type="button">
                            <i class="bi-cart-fill me-1"></i>
                            Add to cart
                        </button>
                    </div>--%>
                </div>
            </div>
            <%--Comments--%>
            <form id="formSubmit">
                <div class="mt-5">
                    <div class="d-flex justify-content-center row">
                        <div class="col-md-12">
                            <div class="d-flex flex-column comment-section">
                                <%--<div class="bg-white p-2">
                                    <div class="d-flex flex-row user-info"><img class="rounded-circle" src="https://i.imgur.com/jHeZKNv.jpg" width="40">
                                        <div class="d-flex flex-column justify-content-start ml-2"><span class="d-block font-weight-bold name">Minh Khang</span><span class="date text-black-50">Shared publicly - Jan 2020</span></div>
                                    </div>
                                    <div class="mt-2">
                                        <p class="comment-text">Good</p>
                                    </div>
                                </div>--%>
                                    <c:forEach var="comment" items="${comments}">
                                        <div class="bg-white p-2">
                                            <div class="d-flex flex-row user-info">
                                                <img class="rounded-circle" src="https://i.imgur.com/jHeZKNv.jpg" width="40">
                                                <div class="d-flex flex-column justify-content-start ml-2">
                                                    <span class="d-block font-weight-bold name">${comment.fullName}</span>
                                                    <span class="date text-black-50">${comment.createdDate}</span>
                                                </div>
                                            </div>
                                            <div class="mt-2">
                                                <p class="comment-text">${comment.content}</p>
                                            </div>
                                        </div>
                                    </c:forEach>
                                <div class="bg-white">
                                    <div class="d-flex flex-row fs-12">
                                        <div class="like p-2 cursor"><i class="fa fa-thumbs-o-up"></i><span class="ml-1">Like</span></div>
                                        <div class="like p-2 cursor"><i class="fa fa-commenting-o"></i><span class="ml-1">Comment</span></div>
                                        <div class="like p-2 cursor"><i class="fa fa-share"></i><span class="ml-1">Share</span></div>
                                    </div>
                                </div>
                                <div class="bg-light p-2">
                                    <div class="d-flex flex-row align-items-start"><img class="rounded-circle" src="https://i.imgur.com/jHeZKNv.jpg" width="40"><textarea class="form-control ml-1 shadow-none textarea"></textarea></div>
                                    <div class="mt-2 text-right"><button class="btn btn-primary btn-sm shadow-none" type="button">Post comment</button><button class="btn btn-outline-primary btn-sm ml-1 shadow-none" type="button">Cancel</button></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </section>

<script>

</script>
</body>
</html>