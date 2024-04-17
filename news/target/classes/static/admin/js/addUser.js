$(document).ready(function() {
    $("#addUserForm").submit(function(event) {
        submitForm(event)
    });
});
function submitForm(event) {
    event.preventDefault(); // Ngăn chặn việc submit form

    var userName = $("#userName").val();
    var password = $("#pwd").val();
    var confirmPassword = $("#pwdConfirm").val();
    var fullName = $("#fullName").val();
    var roleId = $("#RoleNameSelect").val();

    // Kiểm tra mật khẩu
    if (password !== confirmPassword) {
        alert("Mật khẩu và mật khẩu xác nhận không khớp!");
        return;
    }

    // Kiểm tra roleId
    if (!roleId) {
        alert("Vui lòng chọn vai trò!");
        return;
    }

    // Thêm người dùng
    addUser(userName, fullName, password, roleId);
}

function addUser(userName, fullName, password, roleId) {
    $.ajax({
        type: "POST",
        url: "http://localhost:8081/addUser?roleId=" + roleId,
        data: JSON.stringify({
            userName: userName,
            fullName: fullName,
            password: password
        }),
        contentType: "application/json",
        headers: {
            "roleId": roleId
        },
        dataType: "json",
        success: function (response) {
            console.log("Thêm người dùng thành công:", response);
            alert("Thêm người dùng thành công!");
        },
        error: function (error) {
            console.error("Lỗi khi thêm người dùng:", error.responseJSON);
            alert("Lỗi khi thêm người dùng: " + error.responseJSON.message);
        }
    });
}

$(document).on("click", ".cancel-addUser", function () {
    loadListUser()
})
