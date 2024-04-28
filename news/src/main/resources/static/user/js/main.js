$(document).ready(function () {

    // Hàm để thực hiện yêu cầu AJAX và cập nhật bảng khi thành công
    /*function fetchAndDisplayData(pageNumber, limit) {
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
                if(data.listResult.thumbnail == ""){
                    // Gán giá trị mặc định cho thumbnail
                    data.listResult.thumbnail = "https://dummyimage.com/250x150/dee2e6/6c757d.jpg";
                }
                updatePagination(data.totalPage);
                updateTable(data.listResult);
            },
            error: function (error) {
                console.error("Lỗi khi lấy dữ liệu từ API:", error);
            }
        });
    }*/

    /*document.addEventListener("DOMContentLoaded", function() {
        let dropdownItems = document.querySelectorAll(".dropdown-menu .dropdown-item");

        dropdownItems.forEach(function(item) {
            item.addEventListener("click", function(event) {
                event.preventDefault(); // Ngăn chặn hành động mặc định của thẻ <a>

                let category = event.target.getAttribute('data-category'); // Lấy giá trị từ thuộc tính data-category
                console.log("Clicked category:", category); // Kiểm tra giá trị category đã lấy được

                let pageNumber = 1; // Trang số 1
                let limit = 5; // Giới hạn bản ghi

                FilterNewByCategory(category, pageNumber, limit);
            });
        });
    });*/
    var paginationInitialized = false; // Khởi tạo cờ để kiểm tra phân trang đã được khởi tạo chưa

    function changeCategory() {
        // Gắn sự kiện nghe cho thẻ select
        $('#categorySelectHome').change(function() {
            FilterNewByCategory(1, 8);
        });
    }

    /*function updatePagination(totalPages) {
        console.log("Update trang: ", totalPages)
        window.pagObj = $('#pagination').twbsPagination({
            totalPages: totalPages, // Số trang tổng cộng
            visiblePages: 10, // Số trang hiển thị
            onPageClick: function (event, page) {
                FilterNewByCategory(page, 8); // Gọi hàm để lấy và hiển thị dữ liệu cho trang mới
            }
        });
    }*/
    /*function updatePagination(totalPages) {
        // Đảm bảo totalPages không nhỏ hơn 1
        totalPages = Math.max(totalPages, 1);

        if ($('#pagination').length > 0) { // Kiểm tra xem phần tử có tồn tại hay không
            if (!window.pagObj) {
                // Nếu chưa có phân trang, tạo mới
                window.pagObj = $('#pagination').twbsPagination({
                    totalPages: totalPages,
                    visiblePages: 10,
                    onPageClick: function (event, page) {
                        FilterNewByCategory(page, 8);
                    }
                });
            } else {
                // Nếu đã có phân trang, chỉ cập nhật totalPages
                window.pagObj.twbsPagination('updateTotalPages', totalPages);
            }
        } else {
            console.error("Phần tử '#pagination' không tồn tại trong DOM.");
        }
    }*/
    function updatePagination(totalPages) {
        // Đảm bảo totalPages không nhỏ hơn 1
        totalPages = Math.max(totalPages, 1);

        if (!window.pagObj || window.pagObj.totalPages !== totalPages) {
            // Nếu chưa có phân trang hoặc totalPages thay đổi, tạo mới phân trang
            if (window.pagObj) {
                // $('#pagination').twbsPagination('destroy');
            }
            window.pagObj = $('#pagination').twbsPagination({
                totalPages: totalPages,
                visiblePages: 10,
                onPageClick: function (event, page) {
                    FilterNewByCategory(page, 8);
                }
            });
        }
    }

    function FilterNewByCategory(pageNumber, limit) {
        // Lấy giá trị đã chọn từ thẻ select
        var selectedCategory = $('#categorySelectHome').val();

        $.ajax({
            type: "GET",
            url: "http://localhost:8081/newPaging/categoryCode",
            data: {
                categoryCode: selectedCategory,
                page: pageNumber,
                limit: limit
            },
            dataType: "json",
            success: function (data) {
                // console.log(data)
                updatePagination(data.totalPage);
                console.log("totalPage: ", data.totalPage);
                updateTable(data.listResult);
            },
            error: function (error) {
                console.error("Lỗi khi lấy dữ liệu từ API:", error);
            }
        });
    }

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
            // Kiểm tra nếu thumbnail là chuỗi rỗng
            if (news.thumbnail === "") {
                // Gán một giá trị mặc định cho thumbnail
                productItem.find(".thumbnail-item").attr("src", "https://dummyimage.com/250x150/dee2e6/6c757d.jpg");
            } else {
                // Nếu không phải là chuỗi rỗng, sử dụng giá trị thumbnail đã cung cấp
                productItem.find(".thumbnail-item").attr("src", news.thumbnail);
            }
            productItem.find(".title-item").text(news.title);

            // Thêm sản phẩm vào container
            productContainer.append(productItem);
        });
    }

    FilterNewByCategory(1, 8)
    loadCategoriesSelectHome()
    changeCategory()
});

// Hàm để lấy danh sách thể loại và cập nhật thẻ select
function loadCategoriesSelectHome(selectedCategoryCode) {
    $.ajax({
        url: "http://localhost:8081/categories",
        type: "GET",
        dataType: "json",
        success: function(data) {
            // Lấy select element từ DOM
            let $selectCategory = $("#categorySelectHome");

            // Xóa các option cũ trong select
            $selectCategory.empty();

            // Thêm option mặc định
            let defaultOption = $("<option>").val("").text("Tất cả thể loại").prop("selected", true).prop("selected", true);
            $selectCategory.append(defaultOption);

            // Kiểm tra xem selectedCategoryCode có tồn tại hay không
            if (selectedCategoryCode) {
                // Tìm thể loại tương ứng với selectedCategoryCode
                var selectedCategory = data.find(function(category) {
                    return category.code === selectedCategoryCode;
                });

                // Nếu tìm thấy thể loại, thêm vào thẻ select
                if (selectedCategory) {
                    var option = $("<option>", { value: selectedCategory.code, text: selectedCategory.name });
                    $selectCategory.append(option);
                }
            }

            // Thêm các option từ Set vào select
            $.each(data, function(index, category) {
                let option = $("<option>").val(category.code).text(category.name);
                $selectCategory.append(option);
            });
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.error("Error loading role codes:", textStatus, errorThrown);
        }
    });
}