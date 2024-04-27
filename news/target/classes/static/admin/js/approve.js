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
                "<td>" + extractFirstParagraph(item.content) + "</td>" +
                "<td>" + item.shortDescription + "</td>" +
                "<td style='display: none'><a class=\"updateNews\" href=\"#\" title='Chi tiết' data-id=" + item.id + ">\n" +
                "       <i class=\"fa-regular fa-pen-to-square\"></i>\n" +
                // "       <i class=\"fa fa-pencil-square-o\" aria-hidden=\"true\"></i>\n" +
                "    </a></td>" +
                "<td><a class = \"approveNew\" id = \"Duyet\" href=\"#\" title='Duyệt' data-id=" + item.id + ">\n" +
                "<i class=\"fa-solid fa-check\"></i>\n" +
                "</a></td>"+
                "</tr>";
            // Thêm hàng mới vào tbody của bảng
            $("table tbody").append(row);
        });
    }



// Hàm để lấy nội dung của thẻ <p> đầu tiên
    function extractFirstParagraph(content) {
        var temp = document.createElement("div");
        temp.innerHTML = content;
        var firstParagraph = temp.querySelector("p");
        return firstParagraph ? firstParagraph.innerText : content;
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

$(document).on("click", "#Duyet", function (e) {
    e.preventDefault();

    // Lấy giá trị id từ thuộc tính data-id
    var id = $(this).data("id");
    // Chuyển đổi các chuỗi số thành số nguyên
    // var numericIds = id.map(Number);
    updateStatus(id)
})

/*
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

function displaySelectedThumbnail(imageUrl) {
    // Ẩn thẻ hiển thị ảnh nếu nó đang ẩn
    $("#displayThumbnail").show();

    // Cập nhật src của thẻ img
    $("#displayThumbnail").attr("src", imageUrl);
}

$("#overlay").click(function () {
    $("#formContainer").hide();
    $("#overlay").hide();
});

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
});*/
