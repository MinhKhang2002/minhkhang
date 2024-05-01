$(document).ready(function () {

    function updateTable(usersList) {
        $("table tbody").empty(); // Xóa nội dung của tbody trước khi thêm hàng mới

        // Sử dụng Thymeleaf để tạo dòng HTML cho mỗi tin tức
        $.each(usersList, function (index, users) {
            $("table").append(row);
            // Convert ngày sang yyyy/MM/dd
            var createdDate = moment(users.createdDate).format('YYYY-MM-DD');
            // Tạo dòng HTML
            var row = "<tr>" +
                "<td><input type='checkbox' class='checkbox-del' data-id="+ users.id +" id='checkbox_" + users.id + "' value='" + users.id + "'></td>" +
                "<td>" + users.fullName + "</td>" +
                "<td>" + users.userName + "</td>" +  // Sử dụng hàm này để lấy thẻ <p> đầu tiên
                "<td>" + createdDate + "</td>" +
                "<td><a class=\"updateUser\" href=\"#\" id=\"updateUser\" title='Cập nhật' data-id='"+ users.id +"' data-name='"+ users.fullName +"' data-user='"+ users.userName +" '>\n" +
                "       <i class=\"fa-regular fa-pen-to-square\"></i>\n" +
                "    </a></td>" +
                "</tr>";
            $("table").append(row);
        });
    }
    // Hàm để thực hiện yêu cầu AJAX và cập nhật bảng khi thành công
    function fetchAndDisplayData(pageNumber, limit) {
        $.ajax({
            type: "GET",
            url: "http://localhost:8081/userList",
            data: {
                page: pageNumber,
                limit: limit,
            },
            dataType: "json",
            success: function (data) {

                updatePagination(data.totalPage);
                updateTable(data.listResult);
            },
            error: function (xhr, status, errorThrown) {
                if (errorThrown === 'timeout') {
                    $(".alert-danger").text("Lỗi mạng: Không thể kết nối đến máy chủ").show();
                } else {
                    $(".alert-danger").text(xhr.responseText).show(); // Hiển thị nội dung lỗi từ phản hồi của máy chủ
                }
                alert(errorThrown); // In ra lỗi nếu có

            }
        });
    }
    function deleteUser(ids) {
        if (ids.length > 0) {
            console.log(ids);
            $.ajax({
                type: "DELETE",
                url: "http://localhost:8081/userList",
                contentType: "application/json",
                data: JSON.stringify(ids),
                success: function (response) {
                    alert(response);
                    loadListUser()
                },
                error: function (xhr, status, errorThrown) {
                    if (errorThrown === 'timeout') {
                        $(".alert-danger").text("Lỗi mạng: Không thể kết nối đến máy chủ").show();
                    } else {
                        $(".alert-danger").text(xhr.responseText).show(); // Hiển thị nội dung lỗi từ phản hồi của máy chủ
                    }
                    alert(errorThrown); // In ra lỗi nếu có

                }
            });

        }
    }
    function updatePagination(totalPages) {
        window.pagObj = $('#pagination').twbsPagination({
            totalPages: totalPages, // Số trang tổng cộng
            visiblePages: 10, // Số trang hiển thị
            onPageClick: function (event, page) {
                fetchAndDisplayData(page, 5); // Gọi hàm để lấy và hiển thị dữ liệu cho trang mới
            }
        });
    }

    fetchAndDisplayData(1, 5);

    $("#deleteUser").on("click", function () {
        var ids = $('tbody input[type=checkbox]:checked').map(function () {
            return $(this).val() }).get();
        var numbericIds = ids.map(Number);
        if (numbericIds.length > 0) {
            deleteUser(numbericIds);
        } else {
            alert("Bạn hãy chọn người dùng muốn xóa");
        }
    });

    $(document).on("click","#cancel-update" ,function () {
        $("#UpdateUserForm").hide();
        $("#div-a").show();
    });
    $(document).on("click", "#updateUser", function () {
        var idUser = $(this).data("id");
        console.log("id :", idUser)
        $("#formContainer").toggle()
        $("#overlay").toggle()
        var roleId = $("#RoleNameSelect").val();
        console.log("roleId: ", roleId);
        detailUser(idUser)
    });
});

$("#formContainer").submit(function (e) {
    e.preventDefault();
    var idUser = $(this).data("id");
    var fullName = $("#fullName").val();
    var userName = $("#userName").val();
    var roleId = $("#RoleNameSelect").val();
    console.log("roleId: ", roleId);
    console.log("ID: ", idUser)

    var userData = {
        id: idUser,
        fullName: fullName,
        userName: userName
    }
    if (roleId != null){
        updateUser(userData,roleId);
    }else {
        console.log("Vui lòng trọn vai trò")
    }
})

function updateUser(userData,roleId) {
    $.ajax({
        type: "PUT",
        url: "http://localhost:8081/userListUpdate/" + userData.id,
        data: JSON.stringify({
            fullName: userData.fullName,
            userName: userData.userName,
            roleId: roleId
        }),
        contentType: "application/json",
        success: function (response) {
            loadListUser()
        },
        error: function (error) {
            console.log("Lỗi Update User")
        }
    });
}

function detailUser(userId) {
    $.ajax({
        type: "GET",
        url: "http://localhost:8081/user/" + userId,
        dataType: "json",
        success: function (data) {
            $("#fullName").val(data.fullName);
            $("#userName").val(data.userName);
            $("#formContainer").data("id", userId);

            // Lưu danh sách vai trò vào biến roles
            var roleName = data.roles[0].name;
            console.log("roleName: ", roleName);

            // Load vai trò vào thẻ select
            loadRolesSelectDetail(roleName);
        },
        error: function (error) {
            console.error("Lỗi khi lấy dữ liệu bài viết:", error);
        }
    });
}

function loadRolesSelectDetail(roleName) {
    console.log("Role Name ", roleName)
    var roleSelect = $("#RoleNameSelect");
    roleSelect.empty();
    roleSelect.append($("<option>", { value: '', text: 'Chọn vai trò' }));
    $.ajax({
        type: "GET",
        url: "http://localhost:8081/allRole",
        dataType: "json",
        success: function (roles) {
            $.each(roles, function (index, role) {
                var option = $("<option>", { value: role.id, text: role.name });
                // Kiểm tra xem tên vai trò có trùng với roleName không
                if (role.name === roleName) {
                    option.prop("selected", true); // Đặt option này là option được chọn
                }
                roleSelect.append(option);
            });
        },
        error: function (error) {
            console.error("Lỗi khi lấy dữ liệu role:", error);
        }
    });
}

$(document).on("click", "#showFromAddUser", function () {
    showFormAddUser()
})

$("#overlay, .cancel").click(function () {
    $("#formContainer").hide()
    $("#overlay").hide()
})

function showFormAddUser() {
    $.get("/fromAddUser", function (data) {
        $("#main-content").html(data);
    })
}

function loadListUser() {
    showLoading()
    $.get("/listUser", function (data) {
        $("#main-content").html(data);

        hideLoading()

        $(".alert-success").text("Thành công").show()

        setTimeout(function () {
            $(".alert-success").hide()
        }, 3000)
    })
}


