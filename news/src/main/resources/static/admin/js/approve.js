$(document).ready(function () {
    function fetchAndDisplayData(pageNumber, limit) {
        $.ajax({
            type: "GET",
            url: "http://localhost:8081/new/approve",
            data: {
                page: pageNumber,
                limit: limit
            },
            dataType: "json",
            success: function (data) {
                updatePagination(data.totalPage)
                updataTable(data.listResult)
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

    function updataTable(newByStatus){
        // Xóa nội dung hiện có của tbody trước khi thêm mới
        $("table tbody").empty();
        // Duyệt qua mỗi phần tử trong dữ liệu trả về
        $.each(newByStatus, function (index, item) {
            // Tạo một hàng mới trong bảng
            var row = "<tr>" +
                "<td><input type='checkbox' class='checkbox-del' data-id="+ item.id +" id='checkbox_" + item.id + "' value='" + item.id + "'></td>" +
                "<td>" + item.title + "</td>" +
                "<td>" + item.content + "</td>" +
                "<td>" + item.shortDescription + "</td>" +
                "<td><a class=\"updateNews\" href=\"#\" title='Chi tiết' data-id=" + item.id + ">\n" +
                "       <i class=\"fa-regular fa-pen-to-square\"></i>\n" +
                // "       <i class=\"fa fa-pencil-square-o\" aria-hidden=\"true\"></i>\n" +
                "    </a></td>" +
                "<td><a class = \"approveNew\" href=\"#\" title='Duyệt' data-id=" + item.id + ">\n" +
                "<i class=\"fa-solid fa-check\"></i>\n" +
                "</a></td>"+
                "</tr>";
            // Thêm hàng mới vào tbody của bảng
            $("table tbody").append(row);
        });
    }

    // Gọi hàm fetchAndDisplayData để lấy và hiển thị dữ liệu khi trang được tải
    fetchAndDisplayData(1, 5);
})

function updateStatus(id) {
    // Gửi yêu cầu duyệt thông qua AJAX với ID của bài viết
    $.ajax({
        type: "PUT",
        url: "http://localhost:8081/new/" + id + "/approve",
        success: function (result) {
            // alert("Duyệt thành công")
            loadIndexContent()
            console.log("Đã duyệt bài viết có ID:", id);
        },
        error: function (error) {
            console.error("Lỗi:", error);
        }
    });
}

function loadIndexContent() {
    // Sử dụng jQuery để tải nội dung từ index.html
    $.get("/teststatus", function (data) {

        // Hide the alert after 3 seconds
        setTimeout(function() {
            alert("Duyệt thành công")
        }, 3000);

        // Thay đổi nội dung của thẻ <main>
        $("#main-content").html(data);
    });
}

$(document).on("click", ".approveNew", function (e) {
    e.preventDefault();

    // Lấy giá trị id từ thuộc tính data-id
    var id = $(this).data("id");
    // Chuyển đổi các chuỗi số thành số nguyên
    // var numericIds = id.map(Number);
    updateStatus(id)
})
