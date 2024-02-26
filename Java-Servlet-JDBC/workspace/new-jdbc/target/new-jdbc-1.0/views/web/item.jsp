<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglib.jsp"%>
<c:url var="APIcmt" value="/api-web-comment"/>
<c:url var ="HomeURL" value="/trang-chu"/>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Trang chi tiết</title>
</head>
<body>
    <!-- Product section-->
    <section class="py-5">
        <div class="container px-4 px-lg-5 my-5">
            <div class="row gx-4 gx-lg-5 align-items-center">
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
                    <h3 class="display-5 fw-bolder">${model.title}</h3>
<%--                    <p class="abc">${model}</p>--%>
                    <div class="fs-5 mb-5 shortDescription" style="margin-bottom: 0.5rem!important;">
                        <span class="text-decoration-line-through" style="font-weight: bold;">Mô tả ngắn: </span>
                        <span>${model.shortDescription}</span>
                    </div>
                    <h5 class="headding" style="margin-bottom: 0.5rem!important;">Nội dung: </h5>
                    <p class="lead">${model.content}</p>
                </div>
            </div>
            <%--Comments--%>
            <div class="mt-5">
                <div class="d-flex justify-content-center row">
                    <div class="col-md-12">
                        <div class="d-flex flex-column comment-section">
                                <c:forEach var="comments" items="${comments}">
                                    <div class="bg-white p-2">
                                        <div class="d-flex flex-row user-info">
                                            <img class="rounded-circle" src="https://i.imgur.com/jHeZKNv.jpg" width="40">
                                            <div class="d-flex flex-column justify-content-start ml-2">
                                                <span class="d-block font-weight-bold name" id="">${comments.user.fullName}</span>
                                                <span class="date text-black-50" id="createDate" name="createDate">${comments.createDate}</span>
                                            </div>
                                        </div>
                                        <div class="mt-2">
                                            <p class="comment-text">${comments.content}</p>
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
                                <form id="formSubmitComment">
                                    <div class="d-flex flex-row align-items-start">
                                        <img class="rounded-circle" src="https://i.imgur.com/jHeZKNv.jpg" width="40">
    <%--                                        <textarea class="form-control ml-1 shadow-none textarea"></textarea>--%>
                                        <input type="text" class="form-control" id="content" name="content" value="">
                                    </div>
                                    <div class="mt-2 text-right">
                                        <button class="btn btn-primary btn-sm shadow-none" id="btnPostComment" type="button">Post comment</button>
                                        <button class="btn btn-outline-primary btn-sm ml-1 shadow-none" type="button">Cancel</button>
                                    </div>
<%--                                    <input type="hidden" value="${userId}" id="userId" name="userId"/>--%>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <%
        // Lấy giá trị id từ request
        String id = request.getParameter("id");

        // Chuyển id thành newId
        Long newId = Long.parseLong(id);
    %>
<!-- Các mã JavaScript -->
<script>
    <%--var userId = ${userId};--%>
    var userId = ${user.id};
    var newId =<%= newId %>;

    var isLoggedIn = true;
    // Gửi bình luận qua AJAX
    $('#btnPostComment').click(function () {
        var commentContent = $('#content').val();
        if (commentContent) {
            var data = {
                "content": commentContent,
                "userId": userId,
                "newId": newId
            };
        }
        addComment(data);
    });
    function addComment(data) {
        $.ajax({
            url: '${APIcmt}',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(data),
            dataType: 'json',
            success: function (result) {
                alert("Them binh luan thanh cong");
                window.location.href = "${HomeURL}?type=item&id="+result.newId+"";
                // appendComment(result);
            },
            error: function (error) {
                alert("Lỗi không thể thêm bình luận!");
            },
        });
    }

    // Hàm thêm bình luận mới vào danh sách hiển thị
    /*function appendComment(comment) {
        // Tạo HTML cho bình luận mới
        var newComment = '<div class="bg-white p-2"> ' +
            '<div class="d-flex flex-row user-info">' +
            '<img class="rounded-circle" src="https://i.imgur.com/jHeZKNv.jpg" width="40">' +
            '<div class="d-flex flex-column justify-content-start ml-2">' +
            '<span class="d-block font-weight-bold name" id=""></span>' +
            '<span class="date text-black-50" id="createDate" name="createDate">' + comment.createDate + '</span>' +
            '</div>' +
            '</div>' +
            '<div class="mt-2">' +
            '<p class="comment-text">' + comment.content + '</p>' +
            '</div>' +
            '</div>';

        // Thêm bình luận mới vào phần hiển thị
        $('.comment-section').append(newComment);

        // Xóa nội dung trong ô nhập
        $('#content').val('');
    }*/
</script>
</body>
</html>