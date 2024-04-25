$(document).ready(function () {

    function updateTable(roleList) {
        $("table tbody").empty(); // Xóa nội dung của tbody trước khi thêm hàng mới

        // Sử dụng Thymeleaf để tạo dòng HTML cho mỗi tin tức
        $.each(roleList, function (index, role) {
            // Tạo dòng HTML
            var row = "<tr>" +
                "<td><input type='checkbox' class='checkbox-del' data-id="+ role.id +" id='checkbox_" + role.id + "' value='" + role.id + "'></td>" +
                "<td>" + role.name + "</td>" +
                "<td>" + role.code + "</td>" +
                "<td><a class=\"updateRole\" href=\"#\" id=\"update\" title='Cập nhật' data-id="+ role.id +" data-name="+role.name+" data-code="+role.code+" >\n" +
                "       <i class=\"fa-regular fa-pen-to-square\"></i>\n" +
                "    </a></td>" +
                "</tr>";
            $("table").append(row);
        });
    }

    // Hàm để thực hiện yêu cầu AJAX và cập nhật bảng khi thành công
    function fetchAndDisplayData() {
        $.ajax({
            type: "GET",
            url: "http://localhost:8081/allRole",
            data: {},
            dataType: "json",
            success: function (data) {

                console.log("Data: ", data)
                // updatePagination(data.totalPage);
                updateTable(data);
            },
            error: function (error) {
                console.error("Lỗi khi lấy dữ liệu từ API:", error);
            }
        });
    }

    /*function updatePagination(totalPages) {
        window.pagObj = $('#pagination').twbsPagination({
            totalPages: totalPages, // Số trang tổng cộng
            visiblePages: 10, // Số trang hiển thị
            onPageClick: function (event, page) {
                fetchAndDisplayData(page, 5); // Gọi hàm để lấy và hiển thị dữ liệu cho trang mới
            }
        });
    }

    fetchAndDisplayData(1, 5);*/

    fetchAndDisplayData();
})

$(document).on("click", "#add", function () {
    showAddRole()
})

function showAddRole() {
    $.get("/formAddRole", function (data){
        $("#main-content").html(data)
    })
}