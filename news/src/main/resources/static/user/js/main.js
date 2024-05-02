$(document).ready(function () {
    FilterNewByCategory(1, 8)
    loadCategoriesSelectHome()
    // changeCategory()
});
// Phân trang
var currentPage = 1;
function updatePagination(totalPage) {
    // Xóa các nút phân trang hiện có
    $('#pagination').empty();

    // Tạo nút "Trang trước"
    var prevButton = $('<button>').text('Trang trước').addClass("border-page1").on('click', function() {
        // Kiểm tra nếu đang ở trang đầu tiên
        if (currentPage === 1) {
            return;
        }
        currentPage--;
        FilterNewByCategory(currentPage, 8);
    });

    // Thêm nút "Trang trước" và "Trang sau" vào phân trang
    $('#pagination').append(prevButton);

    // Tạo các nút phân trang từ trang 1 đến trang cuối cùng
    for (var i = 1; i <= totalPage; i++) {
        var pageButton = $('<button>').text(i).on('click', function() {
            currentPage = parseInt($(this).text());
            // Xóa lớp active từ tất cả các nút
            $("#pagination button").removeClass('active-pagination');
            // Thêm lớp active cho nút được click
            $(this).addClass('active-pagination');
            FilterNewByCategory(currentPage, 8);
        });
        // Thêm lớp active cho nút trang hiện tại
        if (i === currentPage) {
            pageButton.addClass('active-pagination');
        }
        $('#pagination').append(pageButton);
    }

    // Tạo nút "Trang sau"
    var nextButton = $('<button>').text('Trang sau').addClass("border-page2").on('click', function() {
        // Kiểm tra nếu đang ở trang cuối cùng
        if (currentPage === totalPage) {
            return;
        }
        currentPage++;
        FilterNewByCategory(currentPage, 8);
    });
    $('#pagination').append(nextButton);
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
            "<div class='text-center'><a class='btn btn-outline-dark mt-auto btn-viewsmore' href='/item?id="+news.id+"'>Xem chi tiết</a></div>" +
            "</div>" +
            "</div>" +
            "</div>");

        // Cập nhật thông tin cho sản phẩm mới
        // Kiểm tra nếu thumbnail là chuỗi rỗng
        if (news.thumbnail === null || news.thumbnail === "") {
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

function FilterNewByCategory(pageNumber, limit) {
    // Kiểm tra xem thẻ <a> được click và có class click-category không
    var selectedCategoryCode = '';
    var $selectedLink = $('#categoriesNav nav a.click-category');
    if ($selectedLink.length > 0) {
        selectedCategoryCode = $selectedLink.data('category-code');
    }

    $.ajax({
        type: "GET",
        url: "http://localhost:8081/newPaging/categoryCode",
        data: {
            categoryCode: selectedCategoryCode,
            page: pageNumber,
            limit: limit
        },
        dataType: "json",
        success: function (data) {
            // Cập nhật giao diện phân trang với số trang mới
            updatePagination(data.totalPage);
            updateTable(data.listResult);
        },
        error: function (error) {
            console.error("Lỗi khi lấy dữ liệu từ API:", error);
        }
    });
}

// Hàm để lấy danh sách thể loại và cập nhật thẻ select
function loadCategoriesSelectHome(selectedCategoryCode) {
    $.ajax({
        url: "http://localhost:8081/categories",
        type: "GET",
        dataType: "json",
        success: function(data) {

            // Lấy element nav từ DOM
            let $nav = $("#categoriesNav nav");

            // Xóa các thẻ <a> cũ trong nav
            $nav.empty();

            let $defaultCatrgory = $("<a>").addClass("p-2 text-muted click-category").attr("href", "#").text("Tất cả thể loại");
            $nav.append($defaultCatrgory)

            // Thêm thẻ <a> cho mỗi thể loại từ danh sách
            $.each(data, function(index, category) {
                let $link = $("<a>").addClass("p-2 text-muted").attr("href", "#").text(category.name);
                // Lưu categoryCode vào thuộc tính data-category-code của thẻ <a>
                $link.attr("data-category-code", category.code);
                $nav.append($link);
            });

            // Thêm sự kiện click cho các thẻ <a> trừ thẻ "Tất cả thể loại"
            $nav.find("a:not(:first)").click(function() {
                // Loại bỏ lớp 'selected' từ tất cả các thẻ <a>
                $nav.find("a").removeClass("click-category");
                // Thêm lớp 'selected' cho thẻ <a> được click
                $(this).addClass("click-category");
                // Loại bỏ CSS được áp dụng cho thẻ <a> "Tất cả thể loại"
                $defaultCatrgory.removeClass("click-category");
                FilterNewByCategory(1, 8);
            });

            // Sự kiện click cho thẻ "Tất cả thể loại"
            $defaultCatrgory.click(function() {
                // Loại bỏ lớp 'selected' từ tất cả các thẻ <a>
                $nav.find("a").removeClass("click-category");
                // Thêm lớp 'selected' cho thẻ "Tất cả thể loại"
                $defaultCatrgory.addClass("click-category");
                FilterNewByCategory(1, 8);
            });

            /*// Nếu có thể loại được chọn ban đầu, kích hoạt sự kiện click cho thẻ <a> tương ứng
            if (selectedCategoryCode) {
                $nav.find("a[data-category-code='" + selectedCategoryCode + "']").click();
            }*/


            /*// Lấy select element từ DOM
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
            });*/
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.error("Error loading role codes:", textStatus, errorThrown);
        }
    });
}
