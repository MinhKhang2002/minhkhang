$(document).ready(function () {
    /*// Hàm để cập nhật nội dung của bảng
    function updateTable(newsList) {
        $("#product-item").empty(); // Xóa nội dung của tbody trước khi thêm hàng mới

        // Sử dụng Thymeleaf để tạo dòng HTML cho mỗi tin tức
        $.each(newsList, function (index, news) {
            var productHtml =
                "<div class='col mb-5'>" +
                    "<div class='card h-100'>" +
                // "<img class='card-img-top' src='https://dummyimage.com/450x300/dee2e6/6c757d.jpg' alt='...' />" +
                        "<img class='card-img-top' src=" + news.thumbnail + "/>" +
                        "<div class='card-body p-4'>" +
                            "<div class='text-center'>" +
                                "<h5 class='fw-bolder'>"+ news.title +"</h5>" +
                                "$40.00 - $80.00" +
                            "</div>" +
                        "</div>" +
                        "<div class='card-footer p-4 pt-0 border-top-0 bg-transparent'>" +
                            "<div class='text-center'><a class='btn btn-outline-dark mt-auto btn-viewsmore' href='/item?id="+news.id+"' data-id = " + news.id +">View options</a></div>" +
                        "</div>" +
                    "</div>" +
                "</div>";

            $("#product-item").append(productHtml); // Thêm sản phẩm vào phần tử có ID là "product-item"
        });
    }*/
    // Hàm để cập nhật nội dung của bảng
    /*function updateTable(newsList) {

        // Sử dụng Thymeleaf để tạo dòng HTML cho mỗi tin tức
        $.each(newsList, function (index, news) {
            $(".thumbnail-item").attr("src", news.thumbnail)
            $("#title-item").text(news.title)
        });
    }*/
    function updateTable(newsList) {
        // Lấy danh sách sản phẩm
        var productContainer = $("#product-item");

        // Xóa tất cả các sản phẩm hiện tại trong container
        productContainer.empty();

        // Lặp qua danh sách tin tức và tạo sản phẩm cho mỗi tin tức
        $.each(newsList, function (index, news) {
            // Tạo một sản phẩm mới từ mẫu HTML
            var productItem = $("<div class='col mb-5 product-item'>" +
                "<div class='card h-100'>" +
                "<img class='card-img-top thumbnail-item' src='' />" +
                "<div class='card-body p-4'>" +
                "<div class='text-center'>" +
                "<h5 class='fw-bolder title-item'></h5>" +
                // "$40.00 - $80.00" +
                "</div>" +
                "</div>" +
                "<div class='card-footer p-4 pt-0 border-top-0 bg-transparent'>" +
                "<div class='text-center'><a class='btn btn-outline-dark mt-auto btn-viewsmore' href='/item?id="+news.id+"'>View options</a></div>" +
                "</div>" +
                "</div>" +
                "</div>");

            // Cập nhật thông tin cho sản phẩm mới
            productItem.find(".thumbnail-item").attr("src", news.thumbnail);
            productItem.find(".title-item").text(news.title);

            // Thêm sản phẩm vào container
            productContainer.append(productItem);
        });
    }

    // Hàm để thực hiện yêu cầu AJAX và cập nhật bảng khi thành công
    function fetchAndDisplayData(pageNumber, limit) {
        $.ajax({
            type: "GET",
            url: "http://localhost:8081/new",
            data: {
                page: pageNumber,
                limit: limit
            },
            dataType: "json",
            success: function (data) {
                console.log(data)
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
                fetchAndDisplayData(page, 8); // Gọi hàm để lấy và hiển thị dữ liệu cho trang mới
            }
        });
    }

    fetchAndDisplayData(1, 8);
});