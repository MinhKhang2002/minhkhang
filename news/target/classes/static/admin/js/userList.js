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
    $(document).on("click",".updateUser",function () {
        var idUpdate = $(this).data("id");
         var fullName = $(this).data("name");
        var userName = $(this).data("user");
        $("#user-id").val(idUpdate);
        $("#userName-update").val(userName);
        $("#fullName-update").val(fullName);
        $("#UpdateUserForm").show();
        $("#div-a").hide();
    });
    function updateUser(id,name,user_name,password,roleId) {
        // console.log(user_name);
        $.ajax({
            type: "PUT",
            url: "http://localhost:8081/userListUpdate/" + id,
            data: JSON.stringify({
                fullName: name,
                userName:user_name,
                password: password,
                roleId: roleId
            }),
            contentType: "application/json",
            success: function (response) {
                alert(response);
                fetchAndDisplayData(1, 5);
            },
            error: function (error) {
                alert("không thể sửa");
            }
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

    $("#UpdateUserForm").submit( "click",function (e) {
        e.preventDefault();
        var id =  $("#user-id").val();
        var fullName = $("#fullName-update").val();
        var userName = $("#userName-update").val();
        var Password = $("#pwd-update").val();
        var PasswordConfirm = $("#pwdConfirm-update").val();
        var roleId = $("#RoleNameSelect-update").val();
        console.log(id+fullName+userName+Password,roleId);
        if (Password==PasswordConfirm&&roleId!=null){
            updateUser(id,fullName,userName,Password,roleId);
        }else {
            alert("kiểm tra mật khẩu nhập lại và chọn vai trò ");
        }
    })
function loadRolesSelect() {
    var roleSelect = $("#RoleNameSelect-update");
    roleSelect.empty();
    roleSelect.append($("<option>", { value: '', text: 'Chọn vai trò' }));
    $.ajax({
        type: "GET",
        url: "http://localhost:8081/allRole",
        dataType: "json",
        success: function (roles) {
            // Thêm tất cả các vai trò vào thẻ select
            $.each(roles, function (index, role) {
                var option = $("<option>", { value: role.id, text: role.name });
                roleSelect.append(option);
            });
        },
        error: function (error) {
            console.error("Lỗi khi lấy dữ liệu role:", error);
        }
    });
}
loadRolesSelect()


})

$(document).on("click", "#showFromAddUser", function () {
    showFormAddUser()
})

function showFormAddUser() {
    $.get("/test", function (data) {
        $("#main-content").html(data);
    })
}


