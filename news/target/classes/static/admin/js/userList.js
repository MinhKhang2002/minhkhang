$(document).ready(function () {

    function updateTable(usersList) {
        $("table tbody").empty(); // Xóa nội dung của tbody trước khi thêm hàng mới

        // Sử dụng Thymeleaf để tạo dòng HTML cho mỗi tin tức
        $.each(usersList, function (index, users) {

            // Convert ngày sang yyyy/MM/dd
            var createdDate = moment(users.createdDate).format('YYYY-MM-DD');

            // Tạo dòng HTML
            var row = "<tr>" +
                "<td><input type='checkbox' class='checkbox-del' data-id="+ users.id +" id='checkbox_" + users.id + "' value='" + users.id + "'></td>" +
                "<td>" + users.fullName + "</td>" +
                "<td>" + users.userName + "</td>" +  // Sử dụng hàm này để lấy thẻ <p> đầu tiên
                "<td>" + createdDate + "</td>" +
                "<td style='display: none'><a class=\"updateNews\" href=\"#\" title='Cập nhật' data-id=" + users.id + ">\n" +
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
            error: function (error) {
                console.error("Lỗi khi lấy dữ liệu từ API:", error);
            }
        });
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
})

$(document).on("click", "#showFromAddUser", function () {
    showFormAddUser()
})

function showFormAddUser() {
    $.get("/addUser", function (data) {
        $("#main-content").html(data);
    })
}