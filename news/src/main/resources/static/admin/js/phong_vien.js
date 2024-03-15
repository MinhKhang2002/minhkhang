$(document).ready(function () {

    // Hàm để cập nhật nội dung của bảng
    function updateTable(newsList) {
        $("table tbody").empty(); // Xóa nội dung của tbody trước khi thêm hàng mới

        // Sử dụng Thymeleaf để tạo dòng HTML cho mỗi tin tức
        $.each(newsList, function (index, news) {
            var row = "<tr>" +
                // "<td>" + <input type="checkbox" id="checkbox_news.id" value="news.id"></input> + "</td>" +
                "<td><input type='checkbox' class='checkbox-del' data-id="+ news.id +" id='checkbox_" + news.id + "' value='" + news.id + "'></td>" +
                "<td>" + news.title + "</td>" +
                "<td>" + news.content + "</td>" +
                "<td>" + news.shortDescription + "</td>" +
                "<td><a class=\"updateNews\" href=\"#\" title='Cập nhật' data-id=" + news.id + ">\n" +
                "       <i class=\"fa-regular fa-pen-to-square\"></i>\n" +
                "    </a></td>" +
                "</tr>";
            $("table").append(row);
        });
    }

    // Hàm để thực hiện yêu cầu AJAX và cập nhật bảng khi thành công
    function fetchAndDisplayData(pageNumber, limit) {
        // Lấy categories từ local storage hoặc session
        var userName = localStorage.getItem('userName'); // hoặc sessionStorage.getItem('categories')
        console.log("Username", userName)
        $.ajax({
            type: "GET",
            url: "http://localhost:8081/new/userName",
            data: {
                page: pageNumber,
                limit: limit,
                category: userName // Truyền categories vào tham số query
            },
            dataType: "json",
            success: function (data) {
                updatePagination(data.totalPage);
                updateTable(data.listResult);
                console.log(data)
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
});