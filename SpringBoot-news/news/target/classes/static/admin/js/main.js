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
        $.ajax({
            type: "GET",
            url: "http://localhost:8081/new",
            data: {
                page: pageNumber,
                limit: limit
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
                fetchAndDisplayData(page, 3); // Gọi hàm để lấy và hiển thị dữ liệu cho trang mới
            }
        });
    }

    fetchAndDisplayData(1, 3);
});

// Xử lý sự kiện click vào nút hiển thị form
$(document).on("click", ".showFromAddNews", function () {
    var formContainer = $("#formContainer");
    var categorySelect = $("#categorySelect");
    $("#displayThumbnail").hide();
    formContainer.toggle();
    $("#overlay").toggle();

    // Làm mới (reset) form
    formContainer[0].reset();

    categorySelect.append($("<option>", { value: '', text: 'Chọn thể loại' }));

    // Load thể loại vào thẻ select
    loadCategoriesSelect();
});

// Xử lý sự kiện click vào nút cập nhật form
$(document).on("click", ".updateNews", function () {
    $("#formContainer").toggle();
    $("#overlay").toggle();
});

// Xử lý sự kiện click vào overlay
$("#overlay").click(function () {
    $("#formContainer").hide();
    $("#overlay").hide();
});

// Khi click vào checkALL
document.getElementById('checkAll').addEventListener('change', function () {
    var checkboxes = document.getElementsByClassName('checkbox-del');
    var visibleCheckboxes = Array.prototype.filter.call(checkboxes, function (checkbox) {
        return checkbox.offsetParent !== null;
    });

    for (var i = 0; i < visibleCheckboxes.length; i++) {
        visibleCheckboxes[i].checked = this.checked;
    }
});

// ===========================================================================

// Load lại trang khi thêm, sửa, xoá
function loadIndexContent() {
    // Sử dụng jQuery để tải nội dung từ index.html
    $.get("/ds-bai-viet", function (data) {
        // Thay đổi nội dung của thẻ <main>
        $("#main-content").html(data);

        // Hiển thị thông báo xoá thành công
        $(".alert-success").text("Thành công!").show();

        // Hide the alert after 3 seconds
        setTimeout(function() {
            $(".alert-success").hide();
        }, 3000);
    });
}

// Xử lý khi nhấn vào nút cập nhật bài viết, lọc ID bài viết và đổ dữ liệu về thông qua ID
$(document).on("click", ".updateNews", function () {
    var idToUpdate = $(this).data("id");
    var urlThumbnail = $("#thumbnailUrl").val();
    console.log("Url từ input ẩn:", urlThumbnail)

    // Lấy dữ liệu từ API hoặc nguồn dữ liệu khác dựa trên idToUpdate
    // Sau đó, điền dữ liệu vào các trường input của form
    $.ajax({
        type: "GET",
        url: "http://localhost:8081/new/" + idToUpdate,
        dataType: "json",
        success: function (data) {

            console.log("Data:", data)
            // Điền dữ liệu vào các trường input của form
            $("#title").val(data.title);
            $("#content").val(data.content);
            $("#shortDescription").val(data.shortDescription);
            displaySelectedThumbnail(data.thumbnail)

            // Set data-id cho form để sử dụng trong quá trình submit
            $("#formContainer").data("id", idToUpdate);
            // gán categoryCode vào selectedCategoryCode
            selectedCategoryCode = data.categoryCode;

            // Hiển thị form
            $("#formContainer").show();
            $("#overlay").show();

            // Load thể loại vào thẻ select
            loadCategoriesSelect(selectedCategoryCode);
        },
        error: function (error) {
            console.error("Lỗi khi lấy dữ liệu bài viết:", error);
        }
    });
});

// Xử lý sự kiện click vào nút xoá bài viết
$(document).on("click", "#deleteNews", function (e) {
    e.preventDefault();

    var ids = $('tbody input[type=checkbox]:checked').map(function () {
        return $(this).val();
    }).get();

    // Chuyển đổi các chuỗi số thành số nguyên
    var numericIds = ids.map(Number);

    deleteNew(numericIds);
});

// Hàm gửi request xoá bài viết
function deleteNew(ids) {
    // Gửi yêu cầu xoá thông qua AJAX với danh sách các ID
    $.ajax({
        type: "DELETE",
        url: "http://localhost:8081/new",
        contentType: "application/json",
        data: JSON.stringify(ids),
        success: function (result) {
            console.log("Xoá thành công:", result);

            loadIndexContent();

            // Sau khi xoá, có thể làm mới dữ liệu bảng
            // fetchAndDisplayData(1, 3);
        },
        error: function (error) {
            console.error("Lỗi khi xoá dữ liệu:", error);

            // Hiển thị thông báo lỗi xoá
            $(".alert-danger").text("Lỗi khi xoá dữ liệu!").show();
        }
    });
}

// Hàm để lấy danh sách thể loại và cập nhật thẻ select
function loadCategoriesSelect(selectedCategoryCode) {
    var categorySelect = $("#categorySelect");
    categorySelect.empty();

    // Thêm tùy chọn mặc định (nếu cần)
    categorySelect.append($("<option>", { value: '', text: 'Chọn thể loại' }));

    $.ajax({
        type: "GET",
        url: "http://localhost:8081/categories",
        dataType: "json",
        success: function (categories) {
            // Kiểm tra xem selectedCategoryCode có tồn tại hay không
            if (selectedCategoryCode) {
                // Tìm thể loại tương ứng với selectedCategoryCode
                var selectedCategory = categories.find(function(category) {
                    return category.code === selectedCategoryCode;
                });

                // Nếu tìm thấy thể loại, thêm vào thẻ select
                if (selectedCategory) {
                    var option = $("<option>", { value: selectedCategory.code, text: selectedCategory.name });
                    categorySelect.append(option);
                }
            }

            $.each(categories, function (index, category) {
                var option = $("<option>", { value: category.code, text: category.name });

                // Kiểm tra xem category.code có trùng với selectedCategoryCode không
                if (category.code === selectedCategoryCode) {
                    // Nếu trùng, đặt selected cho option
                    option.prop("selected", true);
                }

                categorySelect.append(option);
            });
        },
        error: function (error) {
            console.error("Lỗi khi lấy dữ liệu thể loại:", error);
        }
    });
}

// Thêm sự kiện cho input file thumbnail
$("#thumbnail").on("change", function (e) {
    const file = e.target.files[0];

    if (file) {
        // Gọi hàm uploadToCloudinary để tải ảnh lên Cloudinary
        uploadToCloudinary(file)
    }
    $("#thumbnail").click();
});

// Thêm image vào Cloudinary
function uploadToCloudinary(file) {
    const cloudinaryUrl = "https://api.cloudinary.com/v1_1/dd1grolgr/image/upload";
    const uploadPreset = "auto-tag";
    const formData = new FormData();

    formData.append("file", file);
    formData.append("upload_preset", uploadPreset);

    return fetch(cloudinaryUrl, {
        method: "POST",
        body: formData
    })
    .then((response) => response.json())
    .then((data) => {
        console.log("Cloudinary response:", data);

        // Kiểm tra xem selector có lỗi không
        console.log("Selector exists:", $("#thumbnailUrl").length);

        try {
            // Cập nhật trường thumbnail của form với URL trả về từ Cloudinary
            $("#thumbnailUrl").val(data.secure_url);
            displaySelectedThumbnail(data.secure_url);
            console.log("Test: ",$("#thumbnailUrl").val(data.secure_url))
        } catch (error) {
            console.error("Error updating thumbnailUrl:", error);
        }

        // Trả về URL an toàn của tệp
        return data.secure_url;
    })
    .catch((error) => {
        console.error("Error uploading to Cloudinary:", error);
        return null;
    });
}

// Hàm hiển thị ảnh đã chọn ngay trên form
function displaySelectedThumbnail(imageUrl) {
    // Ẩn thẻ hiển thị ảnh nếu nó đang ẩn
    $("#displayThumbnail").show();

    // Cập nhật src của thẻ img
    $("#displayThumbnail").attr("src", imageUrl);
}

// Xử lý sự kiện submit form
$("#formContainer").submit(function (e) {
    e.preventDefault();

    var idToUpdate = $(this).data("id");
    var title = $("#title").val();
    console.log("Title: ", title)
    var content = $("#content").val();
    var shortDescription = $("#shortDescription").val();
    var categoryCode = $("#categorySelect").val();
    var thumbnail = $("#thumbnailUrl").val();
    console.log("Type of Thumbnail URL:", typeof $("#thumbnailUrl").val());
    console.log("Type of Thumbnail:", thumbnail);

    var newData = {
        title: title,
        content: content,
        shortDescription: shortDescription,
        categoryCode: categoryCode,
        thumbnail: thumbnail
    };

    // Gọi hàm submitForm để thực hiện thêm mới hoặc cập nhật
    submitForm(idToUpdate, newData);
});

// Hàm chung để submit form
function submitForm(idToUpdate, newData) {
    var form = $("#formContainer");
    var overlay = $("#overlay");

    if (form.data('isSubmitted')) {
        // Form đã được gửi, không thực hiện thêm lần nữa
        return;
    }

    // Kiểm tra xem đã có URL từ Cloudinary chưa
    var thumbnailUrl = $("#thumbnailUrl").val();

    if (!thumbnailUrl) {
        console.error("Vui lòng tải ảnh lên Cloudinary trước khi submit form.");
        return;
    }

    form.data('isSubmitted', true);

    var ajaxSettings = {
        contentType: "application/json",
        success: function (result) {
            // Thực hiện các hành động sau khi thêm/cập nhật thành công
            console.log("Thành công:", result);
            // Sau khi thêm/cập nhật, có thể làm mới dữ liệu bảng

            // Ẩn form
            form.hide();
            overlay.hide();

            loadIndexContent();

            // Xóa dữ liệu 'isSubmitted' để cho phép gửi form một lần nữa
            form.removeData('isSubmitted');
        },
        error: function (error) {
            console.error("Lỗi khi thực hiện:", error);

            // Xóa dữ liệu 'isSubmitted' để cho phép gửi form một lần nữa
            form.removeData('isSubmitted');
        }
    };

    if (!idToUpdate) {
        // Nếu không có idToUpdate hoặc idToUpdate rỗng, thực hiện thêm mới
        ajaxSettings.type = "POST";
        ajaxSettings.url = "http://localhost:8081/new";
        newData.thumbnail = thumbnailUrl; // Thêm thumbnail vào dữ liệu trước khi gửi
        ajaxSettings.data = JSON.stringify(newData);
    } else {
        // Nếu có idToUpdate, thực hiện cập nhật
        ajaxSettings.type = "PUT";
        ajaxSettings.url = "http://localhost:8081/new/" + idToUpdate;
        newData.thumbnail = thumbnailUrl; // Thêm thumbnail vào dữ liệu trước khi gửi
        ajaxSettings.data = JSON.stringify(newData);
    }

    $.ajax(ajaxSettings);
}
