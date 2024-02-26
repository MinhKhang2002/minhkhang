<%@include file="/common/taglib.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<c:url var="APIurl" value="/api-admin-comment"/>
<c:url var="NewURL" value="/admin-new"/>
    <!DOCTYPE html>
    <html>

    <head>
        <meta charset="UTF-8">
        <title>Danh sách bình luận</title>
    </head>

    <body>
        <div class="main-content">
            <form action="<c:url value='/admin-new'/>" id="formSubmit" method="get">
                <div class="main-content-inner">
                    <div class="breadcrumbs ace-save-state" id="breadcrumbs">
                        <ul class="breadcrumb">
                            <li>
                                <i class="ace-icon fa fa-home home-icon"></i>
                                <a href="#">Trang chủ</a>
                            </li>
                        </ul>
                        <!-- /.breadcrumb -->
                    </div>
                    <div class="page-content">
                        <div class="row">
                            <div class="col-xs-12">
                                <c:if test="${not empty messageResponse}">
                                    <div class="alert alert-${alert}">
                                            ${messageResponse}
                                    </div>
                                </c:if>
                                <div class="widget-box table-filter">
                                    <div class="table-btn-controls">
                                        <div class="pull-right tableTools-container">
                                            <div class="dt-buttons btn-overlap btn-group">
                                                <button id="btnDelete" type="button"
                                                        class="dt-button buttons-html5 btn btn-white btn-primary btn-bold" data-toggle="tooltip" title='Xóa bình luận'>
																<span>
																	<i class="fa fa-trash-o bigger-110 pink"></i>
																</span>
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-xs-12">
                                        <div class="table-responsive">
                                            <table class="table table-bordered">
                                                <thead>
                                                <tr>
                                                    <th><input type="checkbox" id="checkAll"></th>
                                                    <th>Nội dung bình luận</th>
                                                    <th>Tên bài viết</th>
                                                    <th>Full Name</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <c:forEach var="comment" items="${comment.listResult}">
                                                    <tr>
                                                        <td><input type="checkbox" id="checkbox_${comment.id}" value="${comment.id}"></td>
<%--                                                        <td>${comment.title}</td>--%>
                                                        <td>${comment.content}</td>
                                                        <td>${comment.news.title}</td>
                                                        <td>${comment.user.fullName}</td>
                                                    </tr>
                                                </c:forEach>
                                                </tbody>
                                            </table>
                                            <ul class="pagination" id="pagination"></ul>
                                            <input type="hidden" value="" id="page" name="page">
                                            <input type="hidden" value="" id="maxPageItem" name="maxPageItem">
                                            <input type="hidden" value="" id="sortName" name="sortName">
                                            <input type="hidden" value="" id="sortBy" name="sortBy">
                                            <input type="hidden" value="" id="type" name="type">
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </form>
        </div>
        <!-- /.main-content -->
        <script>
            var currentPage = ${comment.page};
            var totalPages = ${comment.totalPage};
            var limit = 2;
            $(function () {
                window.pagObj = $('#pagination').twbsPagination({
                    totalPages: totalPages,
                    visiblePages: 10,
                    startPage: currentPage,
                    onPageClick: function (event, page) {
                    	if(currentPage != page) {
                            $('#maxPageItem').val(limit);
                            $('#page').val(page);
                            $('#sortName').val('u.id');
                            $('#sortBy').val('desc');
                            $('#type').val('comment');
                            $('#formSubmit').submit();
                    	}
                    }
                });
            });

            $("#btnDelete").click(function () {
                var data = {};
                // lấy dữ liệu từ trong tbody nào đã check và map dữ liệu đã lấy vào ids
                var ids = $('tbody input[type=checkbox]:checked').map(function () {
                    return $(this).val();
                }).get();
                data['ids'] = ids;
                deleteNew(data);
            });

            // call api
            function deleteNew(data) {
                $.ajax({
                    url: '${APIurl}',
                    type: 'DELETE',
                    contentType: 'application/json',
                    data: JSON.stringify(data),
                    success: function (result) {
                        window.location.href = "${NewURL}?type=comment&maxPageItem=2&page=1&message=delete_success";
                    },
                    error: function (error) {
                        window.location.href = "${NewURL}?type=comment&maxPageItem=2&page=1&message=error_system";
                    },
                });
            }
        </script>
    </body>

    </html>