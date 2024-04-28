$(document).ready(function() {

});
/*$("#addUserForm").submit(function(event) {
    event.preventDefault(); // Ngăn chặn việc submit form
    submitForm(event)
});*/

$(document).on("click", "#add-submit", function () {
    submitForm(event)
})

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
    } else if (!roleId || !userName) {
        alert("Vui lòng nhập thông tin người dùng!");
    } else {
        // Thêm người dùng
        addUser(userName, fullName, password, roleId);
    }
}
function addUser(userName, fullName, password, roleId) {
    var user = {
        userName: userName,
        fullName: fullName,
        password: password
    };

    $.ajax({
        type: "POST",
        url: "http://localhost:8081/addUser?roleId=" + roleId,
        data: JSON.stringify(user),
        contentType: "application/json",
        /*headers: {
            "roleId": roleId
        },*/
        dataType: "json",
        success: function (response) {
            console.log("Thêm người dùng thành công:", response);
            alert("Thêm người dùng thành công!");
            loadListUser();
            /*if (response.id) {
                alert("Thêm người dùng thành công!");
                loadListUser();
            } else {
                console.log("Lỗi thêm người dùng: Có lỗi xảy ra:", response);
                alert("Lỗi thêm người dùng: Có lỗi xảy ra");
            }*/
        },
        error: function (error) {
            console.error("Lỗi khi thêm người dùng:", error);
            if (error.status === 400 && error.responseText === "Người dùng đã tồn tại") {
                alert("Người dùng đã tồn tại, Vui lòng thêm lại");
            } else {
                alert("Lỗi khi thêm người dùng: Dữ liệu không hợp lệ");
            }
        }
        /*success: function (response) {
            console.log("Thêm người dùng thành công:", response);
            if (response) {
                console.log("Thêm người dùng thành công:", response);
                alert("Thêm người dùng thành công!");
                loadListUser()
            } else {
                console.error("Lỗi a thêm người dùng:", response.message);
                alert("Lỗi thêm người dùng: " + response.message);
            }
        },
        error: function (error) {
            // console.error("Lỗi b khi thêm người dùng:", error.responseJSON);
            alert("Lỗi khi thêm người dùng: " + error.responseJSON.message);
        }*/
    });
}
$(document).on("click", ".cancel-addUser", function () {
    loadUseListPage()
})

function loadUseListPage() {
    showLoading()
    $.get("/listUser", function (data) {
        $("#main-content").html(data);
        hideLoading()
    })
}
