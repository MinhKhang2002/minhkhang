<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@include file="/common/taglib.jsp"%>
<c:url var="APIurl" value="/api-web-user"/>
<c:url var ="HomeURL" value="/trang-chu"/>
<c:url var ="RegisterURL" value="/dang-ky"/>
<c:url var ="LoginURL" value="/dang-nhap"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Đăng ký</title>
</head>
<body>
<div class="container">
	<h1 class="form-heading">Register Form</h1>
	<div class="login-form">
		<div class="main-div">
<%--			<div class="panel">--%>
<%--				<h2>Đăng ký tài khoản</h2>--%>
<%--			</div>--%>
			<c:if test="${not empty message}">
				<div class="alert alert-${alert}">
						${message}
				</div>
			</c:if>
			<form action="<c:url value='/dang-ky'/>" id="formRegister" method="post">
				<div class="form-group">
					<input type="text" class="form-control" id="userName" name="userName"
						   placeholder="Tên đăng nhập">
				</div>
				<div class="form-group">
					<input type="text" class="form-control" id="fullName" name="fullName"
						   placeholder="Họ và tên">
				</div>

				<div class="form-group">
					<input type="password" class="form-control" id="password" name="password"
						   placeholder="Mật khẩu">
				</div>

				<%--<div class="form-group">
					<input type="password" class="form-control" id="enterPassword" name="enterPassword"
						   placeholder="Nhập lại Mật khẩu">
				</div>--%>
<%--				<input type="hidden" value="register" name="action"/>--%>
				<input type="hidden" value="1" name="status" id="status"/>
				<input type="hidden" value="2" name="roleId" id="roleId"/>
				<input type="hidden" value="${model.id}" name="id" id="id"/>
				<button type="submit" class="btn btn-primary" id="btnRegister">Đăng ký</button>
			</form>
		</div>
	</div>
</div>
<script>
	$('#btnRegister').click(function (e) {
		e.preventDefault();

		var userData = {};
		var formData = $('#formRegister').serializeArray();
		var id = $('#id').val();

		$.each(formData, function (i, field) {
			userData[field.name] = field.value;
		});

		$.ajax({
			url: '${APIurl}', // Đặt URL của API thêm người dùng ở đây
			type: 'POST',
			contentType: 'application/json',
			data: JSON.stringify(userData),
			dataType: 'json',
			// Các thiết lập khác
			success: function (result) {
				if (result.id > 0) {
					alert("Đã đăng ký tài khoản thành công.");
					// Redirect hoặc thực hiện hành động khác sau khi đăng ký thành công
					window.location.href = "${LoginURL}?action=login&message=username_password_register&alert=success";
				}
			},
			error: function (error) {
				alert("Tên người dùng đã tồn tại trong hệ thống vui lòng đăng ký lại");
				window.location.href = "${RegisterURL}?action=register&message=not_register&alert=danger";
			}
		});
	});
</script>
</body>
</html>
