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

    // Kiểm tra xem đang thực hiện thêm mới hoặc cập nhật
    var isUpdating = !!$("#formContainer").data("id");

    if (!isUpdating) {
        // Nếu đang thực hiện thêm mới, kiểm tra xem đã có URL từ Cloudinary chưa
        var thumbnailUrl = $("#thumbnailUrl").val();

        if (!thumbnailUrl) {
            console.error("Vui lòng tải ảnh lên Cloudinary trước khi submit form.");
            return;
        }
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

// Thêm sự kiện cho input file thumbnail
/*$("#thumbnail").on("change", function (e) {
    const file = e.target.files[0];

    if (file) {
        // Gọi hàm uploadToCloudinary để tải ảnh lên Cloudinary
        uploadToCloudinary(file)
    }
    $("#thumbnail").click();
});*/
$("#thumbnail").on("change", function (e) {
    const file = e.target.files[0];

    if (file) {
        // Lấy URL của ảnh cũ từ trường input hidden thumbnailUrl
        const oldImageUrl = $("#thumbnailUrl").val();

        // Kiểm tra xem có URL của ảnh cũ hay không
        if (oldImageUrl) {
            // Lấy public ID của ảnh cũ từ URL của nó
            const publicId = getPublicIdFromImageUrl(oldImageUrl);

            // Gọi hàm deleteFromCloudinary để xóa ảnh cũ từ Cloudinary
            deleteFromCloudinary(publicId)
                .then(() => {
                    // Sau khi xóa ảnh cũ thành công, tiếp tục tải lên ảnh mới
                    uploadToCloudinary(file);
                })
                .catch((error) => {
                    console.error("Error deleting old image from Cloudinary:", error);
                });
        } else {
            // Nếu không có URL của ảnh cũ, tiếp tục tải lên ảnh mới
            uploadToCloudinary(file);
        }
    }
    // Gọi click để người dùng có thể chọn lại cùng một tệp ảnh sau khi đã xóa ảnh cũ
    $("#thumbnail").click();
});

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

function deleteFromCloudinary(imagePublicId) {
    const cloudinaryUrl = `https://api.cloudinary.com/v1_1/dd1grolgr/image/destroy`;

    const requestBody = {
        public_id: imagePublicId,
        api_key: "165223227289875",
        api_secret: "KuEgKknBTrJ7-FdsAHGJYa_Jx4c",
    };

    return fetch(cloudinaryUrl, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(requestBody),
    })
        .then((response) => response.json())
        .then((data) => {
            console.log("Cloudinary delete response:", data);
            return data;
        })
        .catch((error) => {
            console.error("Error deleting from Cloudinary:", error);
            return null;
        });
}