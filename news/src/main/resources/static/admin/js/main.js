$(document).ready(function () {
    $("#showFromAddNews").click(function () {
        showFormAddNew()
    });

    // Hàm để thực hiện yêu cầu AJAX và cập nhật bảng khi thành công
    function fetchAndDisplayData(pageNumber, limit) {
        // Lấy categories từ local storage hoặc session
        var categories = localStorage.getItem('categories'); // hoặc sessionStorage.getItem('categories')
        console.log("Thể loại người quản lý", categories)
        $.ajax({
            type: "GET",
            url: "http://localhost:8081/new/category",
            data: {
                page: pageNumber,
                limit: limit,
                category: categories // Truyền categories vào tham số query
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
    };

    function updatePagination(totalPages) {
        window.pagObj = $('#pagination').twbsPagination({
            totalPages: totalPages, // Số trang tổng cộng
            visiblePages: 10, // Số trang hiển thị
            onPageClick: function (event, page) {
                fetchAndDisplayData(page, 5); // Gọi hàm để lấy và hiển thị dữ liệu cho trang mới
            }
        });
    };

    fetchAndDisplayData(1, 5);

    // Xử lý sự kiện click vào nút xoá bài viết
    $(document).on("click", "#deleteNews", function (e) {
        e.preventDefault();

        var ids = $('tbody input[type=checkbox]:checked').map(function () {
            return $(this).val();
        }).get();

        // Chuyển đổi các chuỗi số thành số nguyên
        var numericIds = ids.map(Number);

        if(numericIds.length === 0) {
            $(".alert-danger").text("Vui lòng chọn bài viết cần xoá!").show()

            setTimeout(function () {
                $(".alert-danger").hide()
            }, 3000)
        } else {
            deleteNew(numericIds);
        }
    });

// Xử lý sự kiện click vào nút cập nhật form
    $(document).on("click", ".updateNews", function () {
        $("#btn-submit").text("Sửa")
        $("#formContainer").toggle();
        $("#overlay").toggle();
    });

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


    $("#notApprovedPostsCard").mouseenter(function() {
        loadNotApprovedPosts();
    }).mouseleave(function() {
        $("#notApprovedPostsList").hide();
    });
});

function updateTable(newsList) {
    $("table tbody").empty(); // Xóa nội dung của tbody trước khi thêm hàng mới

    // Sử dụng Thymeleaf để tạo dòng HTML cho mỗi tin tức
    $.each(newsList, function (index, news) {
        // Tạo dòng HTML
        var row = "<tr>" +
            "<td><input type='checkbox' class='checkbox-del' data-id="+ news.id +" id='checkbox_" + news.id + "' value='" + news.id + "'></td>" +
            "<td>" + news.title + "</td>" +
            "<td class='truncated-content'>" + extractFirstParagraph(news.content) + "</td>" +  // Sử dụng hàm này để lấy thẻ <p> đầu tiên
            "<td>" + news.shortDescription + "</td>" +
            "<td><a class=\"updateNews\" href=\"#\" title='Cập nhật' data-id=" + news.id + ">\n" +
            "       <i class=\"fa-regular fa-pen-to-square\"></i>\n" +
            "    </a></td>" +
            "</tr>";
        $("table").append(row);
    });
}

// Hàm để lấy nội dung của thẻ <p> đầu tiên
function extractFirstParagraph(content) {
    var temp = document.createElement("div");
    temp.innerHTML = content;
    var firstParagraph = temp.querySelector("p");
    return firstParagraph ? firstParagraph.innerText : content;
}

function btnAddText() {
    $("#btn-submit").text("Thêm")
}

function btnUpdateText() {
    $("#btn-submit").text("Sửa")
}

// Xử lý sự kiện click vào nút hiển thị form
function showFormAddNew() {
    btnAddText()
    var formContainer = $("#formContainer");
    // Làm mới (reset) form
    formContainer[0].reset();
    // Làm mới lại trình soạn thảo CKEditor
    var editor = window.editor;
    if (editor) {
        // Đặt nội dung của trình soạn thảo về rỗng
        editor.setData('');
    } else {
        console.error('Trình soạn thảo CKEditor chưa được khởi tạo.');
    }
    var categorySelect = $("#categorySelect");
    $("#displayThumbnail").hide();
    formContainer.toggle();
    $("#overlay").toggle();

    // Load thể loại vào thẻ select
    loadCategoriesSelect();
}

// Xử lý sự kiện click vào overlay
$("#overlay, .cancel").click(function () {
    cancelForm()
});

// Đóng form
function cancelForm() {
    $("#formContainer").hide();
    $("#overlay").hide();
}

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
    // Hiển thị loading
    showLoading()
    // Sử dụng jQuery để tải nội dung từ index.html
    $.get("/ds-bai-viet", function (data) {
        // Thay đổi nội dung của thẻ <main>
        $("#main-content").html(data);

        hideLoading()

        $(".alert-success").text("Thành công").show();

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
    var editor = $("#editor");
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
            // $("#content").val(data.content);
            $("#shortDescription").val(data.shortDescription);
            displaySelectedThumbnail(data.thumbnail)
            // data.images.forEach(image => {
            //     displaySelectedThumbnail(image.thumbnail);
            // });

            // Set data-id cho form để sử dụng trong quá trình submit
            $("#formContainer").data("id", idToUpdate);
            // gán categoryCode vào selectedCategoryCode
            selectedCategoryCode = data.categoryCode;

            // Hiển thị form
            $("#formContainer").show();
            $("#overlay").show();

            // Load thể loại vào thẻ select
            loadCategoriesSelect(selectedCategoryCode);
            // Hiển thị nội dung vào trong CKEditor
            if (window.editor) {
                window.editor.setData(data.content);
            } else {
                console.error("CKEditor chưa được khởi tạo hoặc gán cho biến editor.");
            }
        },
        error: function (error) {
            console.error("Lỗi khi lấy dữ liệu bài viết:", error);
        }
    });
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

            // Hide the alert after 3 seconds
            setTimeout(function() {
                $(".alert-danger").hide();
            }, 3000);
        }
    });
}

// Hàm để lấy danh sách thể loại và cập nhật thẻ select
/*function loadCategoriesSelect(selectedCategoryCode) {
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
}*/

// Hàm để lấy danh sách thể loại và cập nhật thẻ select
function loadCategoriesSelect(selectedCategoryCode) {
    $.ajax({
        url: "http://localhost:8081/categories",
        type: "GET",
        dataType: "json",
        success: function(data) {
            // Lấy select element từ DOM
            let $selectCategory = $("#categorySelect");

            // Xóa các option cũ trong select
            $selectCategory.empty();

            // Thêm option mặc định
            let defaultOption = $("<option>").val("").text("Chọn thể loại").prop("selected", true).prop("selected", true);
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

// Hàm hiển thị ảnh đã chọn ngay trên form
function displaySelectedThumbnail(imageUrl) {
    // Ẩn thẻ hiển thị ảnh nếu nó đang ẩn
    $("#displayThumbnail").show();

    // Cập nhật src của thẻ img
    $("#displayThumbnail").attr("src", imageUrl);
}

// Hàm chung để submit form
function submitForm(idToUpdate, newData) {
    var form = $("#formContainer");
    var overlay = $("#overlay");

    if (form.data('isSubmitted')) {
        // Form đã được gửi, không thực hiện thêm lần nữa
        return;
    }

    // Lấy nội dung từ CKEditor thay vì trường textarea thông thường
    var content = window.editor.getData();
    newData.content = content;
    console.log("Content: ", content)

    // Kiểm tra xem đã có URL từ Cloudinary chưa
    var thumbnailUrl = $("#thumbnailUrl").val();

    // Kiểm tra xem đang thực hiện thêm mới hoặc cập nhật
    /*var isUpdating = !!$("#formContainer").data("id");

    if (!isUpdating) {
        // Nếu đang thực hiện thêm mới, kiểm tra xem đã có URL từ Cloudinary chưa
        var thumbnailUrl = $("#thumbnailUrl").val();

        if (!thumbnailUrl) {
            console.error("Vui lòng tải ảnh lên Cloudinary trước khi submit form.");
            return;
        }
    }*/

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

document.querySelector('.textarea').addEventListener('input', function(event) {
    var rowLength = 138; // Độ dài tối đa của mỗi hàng
    var content = this.value;

    // Kiểm tra xem phím được nhấn là Enter hay không
    if (event.inputType === 'insertLineBreak') {
        // Nếu là Enter, cập nhật số lượng hàng
        var rowCount = this.rows + 1;
        this.rows = rowCount;
        return; // Ngắt và không tiếp tục xử lý
    }

    // Tính toán số lượng hàng dựa trên độ dài của nội dung
    var rowCount = Math.ceil(content.length / rowLength);

    // Tính toán số lượng ký tự trên hàng cuối cùng
    var lastRowLength = content.length % rowLength || rowLength;

    // Nếu nội dung vượt quá độ dài mỗi hàng, cập nhật số lượng hàng của textarea
    if (lastRowLength !== 0 && content.length > rowLength) {
        this.rows = rowCount;
    } else {
        // Nếu không, giữ số lượng hàng ở mức mặc định
        this.rows = 1;
    }
})

/*// Thêm image vào Cloudinary và chèn URL vào CKEditor
function uploadAndInsertImageToEditor(file) {
    const cloudinaryUrl = "https://api.cloudinary.com/v1_1/dd1grolgr/image/upload";
    const uploadPreset = "auto-tag";
    const formData = new FormData();

    formData.append("file", file);
    formData.append("upload_preset", uploadPreset);

    return fetch(cloudinaryUrl, {
        method: "POST",
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            console.log("Cloudinary response:", data);

            // Lấy URL của ảnh từ Cloudinary
            const imageUrl = data.secure_url;

            // Tạo một phần tử hình ảnh trong CKEditor với URL từ Cloudinary
            const imageElement = window.editor.document.createElement("img");
            imageElement.setAttribute("src", imageUrl);

            // Chèn phần tử hình ảnh vào CKEditor
            window.editor.model.insertContent(imageElement);

            // Trả về URL của ảnh đã tải lên
            return imageUrl;
        })
        .catch(error => {
            console.error("Error uploading image to Cloudinary:", error);
            return null;
        });
}*/

// Thêm image vào Cloudinary và chèn URL vào CKEditor
function uploadAndInsertImageToEditor(file) {
    const cloudinaryUrl = "https://api.cloudinary.com/v1_1/dd1grolgr/image/upload";
    const uploadPreset = "auto-tag";
    const formData = new FormData();

    formData.append("file", file);
    formData.append("upload_preset", uploadPreset);

    return fetch(cloudinaryUrl, {
        method: "POST",
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            console.log("Cloudinary response:", data);

            // Lấy URL của ảnh từ Cloudinary
            const imageUrl = data.secure_url;

            // Trả về URL của ảnh đã tải lên
            return imageUrl;
        })
        .catch(error => {
            console.error("Error uploading image to Cloudinary:", error);
            return null;
        });
}

// Lắng nghe sự kiện 'fileUploadRequest' khi có yêu cầu tải lên ảnh
document.addEventListener('fileUploadRequest', function (event) {
    var fileLoader = event.detail.loader;

    // Gọi hàm uploadAndInsertImageToEditor với file ảnh
    uploadAndInsertImageToEditor(fileLoader.file)
        .then(imageUrl => {
            // Xử lý sau khi ảnh được tải lên và chèn vào CKEditor
        })
        .catch(error => {
            console.error("Lỗi khi tải lên ảnh và chèn vào CKEditor:", error);
        });
});

CKEDITOR.ClassicEditor
    .create(document.getElementById("editor"), {
        // https://ckeditor.com/docs/ckeditor5/latest/features/toolbar/toolbar.html#extended-toolbar-configuration-format
        toolbar: {
            items: [
                'exportPDF','exportWord', '|',
                'findAndReplace', 'selectAll', '|',
                'heading', '|',
                'bold', 'italic', 'strikethrough', 'underline', 'code', 'subscript', 'superscript', 'removeFormat', '|',
                'bulletedList', 'numberedList', 'todoList', '|',
                'outdent', 'indent', '|',
                'undo', 'redo',
                '-',
                'fontSize', 'fontFamily', 'fontColor', 'fontBackgroundColor', 'highlight', '|',
                'alignment', '|',
                'link', 'uploadImage', 'blockQuote', 'insertTable', 'mediaEmbed', 'codeBlock', 'htmlEmbed', '|',
                'specialCharacters', 'horizontalLine', 'pageBreak', '|',
                'textPartLanguage', '|',
                'sourceEditing'
            ],
            shouldNotGroupWhenFull: true
        },
        // Changing the language of the interface requires loading the language file using the <script> tag.
        // language: 'es',
        list: {
            properties: {
                styles: true,
                startIndex: true,
                reversed: true
            }
        },
        // https://ckeditor.com/docs/ckeditor5/latest/features/headings.html#configuration
        heading: {
            options: [
                { model: 'paragraph', title: 'Paragraph', class: 'ck-heading_paragraph' },
                { model: 'heading1', view: 'h1', title: 'Heading 1', class: 'ck-heading_heading1' },
                { model: 'heading2', view: 'h2', title: 'Heading 2', class: 'ck-heading_heading2' },
                { model: 'heading3', view: 'h3', title: 'Heading 3', class: 'ck-heading_heading3' },
                { model: 'heading4', view: 'h4', title: 'Heading 4', class: 'ck-heading_heading4' },
                { model: 'heading5', view: 'h5', title: 'Heading 5', class: 'ck-heading_heading5' },
                { model: 'heading6', view: 'h6', title: 'Heading 6', class: 'ck-heading_heading6' }
            ]
        },
        // https://ckeditor.com/docs/ckeditor5/latest/features/editor-placeholder.html#using-the-editor-configuration
        placeholder: 'Welcome to content New',
        // https://ckeditor.com/docs/ckeditor5/latest/features/font.html#configuring-the-font-family-feature
        fontFamily: {
            options: [
                'default',
                'Arial, Helvetica, sans-serif',
                'Courier New, Courier, monospace',
                'Georgia, serif',
                'Lucida Sans Unicode, Lucida Grande, sans-serif',
                'Tahoma, Geneva, sans-serif',
                'Times New Roman, Times, serif',
                'Trebuchet MS, Helvetica, sans-serif',
                'Verdana, Geneva, sans-serif'
            ],
            supportAllValues: true
        },
        // https://ckeditor.com/docs/ckeditor5/latest/features/font.html#configuring-the-font-size-feature
        fontSize: {
            options: [ 10, 12, 14, 'default', 18, 20, 22 ],
            supportAllValues: true
        },
        // Be careful with the setting below. It instructs CKEditor to accept ALL HTML markup.
        // https://ckeditor.com/docs/ckeditor5/latest/features/general-html-support.html#enabling-all-html-features
        htmlSupport: {
            allow: [
                {
                    name: /.*/,
                    attributes: true,
                    classes: true,
                    styles: true
                }
            ]
        },
        // Be careful with enabling previews
        // https://ckeditor.com/docs/ckeditor5/latest/features/html-embed.html#content-previews
        htmlEmbed: {
            showPreviews: true
        },
        // https://ckeditor.com/docs/ckeditor5/latest/features/link.html#custom-link-attributes-decorators
        link: {
            decorators: {
                addTargetToExternalLinks: true,
                defaultProtocol: 'https://',
                toggleDownloadable: {
                    mode: 'manual',
                    label: 'Downloadable',
                    attributes: {
                        download: 'file'
                    }
                }
            }
        },
        // https://ckeditor.com/docs/ckeditor5/latest/features/mentions.html#configuration
        mention: {
            feeds: [
                {
                    marker: '@',
                    feed: [
                        '@apple', '@bears', '@brownie', '@cake', '@cake', '@candy', '@canes', '@chocolate', '@cookie', '@cotton', '@cream',
                        '@cupcake', '@danish', '@donut', '@dragée', '@fruitcake', '@gingerbread', '@gummi', '@ice', '@jelly-o',
                        '@liquorice', '@macaroon', '@marzipan', '@oat', '@pie', '@plum', '@pudding', '@sesame', '@snaps', '@soufflé',
                        '@sugar', '@sweet', '@topping', '@wafer'
                    ],
                    minimumCharacters: 1
                }
            ]
        },
        // The "superbuild" contains more premium features that require additional configuration, disable them below.
        // Do not turn them on unless you read the documentation and know how to configure them and setup the editor.
        removePlugins: [
            // These two are commercial, but you can try them out without registering to a trial.
            // 'ExportPdf',
            // 'ExportWord',
            'AIAssistant',
            'CKBox',
            'CKFinder',
            'EasyImage',
            // This sample uses the Base64UploadAdapter to handle image uploads as it requires no configuration.
            // https://ckeditor.com/docs/ckeditor5/latest/features/images/image-upload/base64-upload-adapter.html
            // Storing images as Base64 is usually a very bad idea.
            // Replace it on production website with other solutions:
            // https://ckeditor.com/docs/ckeditor5/latest/features/images/image-upload/image-upload.html
            // 'Base64UploadAdapter',
            'RealTimeCollaborativeComments',
            'RealTimeCollaborativeTrackChanges',
            'RealTimeCollaborativeRevisionHistory',
            'PresenceList',
            'Comments',
            'TrackChanges',
            'TrackChangesData',
            'RevisionHistory',
            'Pagination',
            'WProofreader',
            // Careful, with the Mathtype plugin CKEditor will not load when loading this sample
            // from a local file system (file://) - load this site via HTTP server if you enable MathType.
            'MathType',
            // The following features are part of the Productivity Pack and require additional license.
            'SlashCommand',
            'Template',
            'DocumentOutline',
            'FormatPainter',
            'TableOfContents',
            'PasteFromOfficeEnhanced',
            'CaseChange'
        ]
    })
    .then(editor => {
        // Đoạn mã trong then được thực thi khi CKEditor đã được tạo thành công
        window.editor = editor;

        class MyUploadAdapter {
            constructor(loader) {
                this.loader = loader;
            }

            upload() {
                return this.loader.file.then(file => {
                    return new Promise((resolve, reject) => {
                        uploadAndInsertImageToEditor(file)
                            .then(imageUrl => {
                                resolve({
                                    default: imageUrl
                                });
                            })
                            .catch(error => {
                                reject(error);
                            });
                    });
                });
            }
        }

        // Lắng nghe sự kiện 'fileUploadRequest' khi editor đã sẵn sàng
        editor.plugins.get('FileRepository').createUploadAdapter = function (loader) {
            return new MyUploadAdapter(loader);
        };
    })
    .catch(error => {
        console.error('Đã xảy ra lỗi khi tạo CKEditor:', error);
    });

