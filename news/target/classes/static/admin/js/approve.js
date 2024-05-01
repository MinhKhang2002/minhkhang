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
                "<td style='display: none'>" + item.shortDescription + "</td>" +
                "<td><a class=\"detailApprove\" href=\"#\" title='Chi tiết' data-id=" + item.id + ">\n" +
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

    // Xử lý sự kiện click vào nút cập nhật form
    $(document).on("click", ".detailApprove", function () {
        $("#formContainer").toggle();
        $("#overlay").toggle();

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
})

// Xử lý sự kiện submit form
$("#formContainer").submit(function (e) {
    e.preventDefault();
    var idToUpdate = $(this).data("id");
    // Gọi hàm submitForm để thực hiện thêm mới hoặc cập nhật
    updateStatus(idToUpdate);
});

// Hàm hiển thị ảnh đã chọn ngay trên form
function displaySelectedThumbnail(imageUrl) {
    // Ẩn thẻ hiển thị ảnh nếu nó đang ẩn
    $("#displayThumbnail").show();

    // Cập nhật src của thẻ img
    $("#displayThumbnail").attr("src", imageUrl);
}

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
    showLoading()
    // Sử dụng jQuery để tải nội dung từ index.html
    $.get("/teststatus", function (data) {

        // Thay đổi nội dung của thẻ <main>
        $("#main-content").html(data);
        hideLoading()

        // Hiển thị thông báo xoá thành công
        $(".alert-success").text("Thành công!").show();

        // Hide the alert after 3 seconds
        setTimeout(function() {
            $(".alert-success").hide();
        }, 3000);
    });
}

function error(text) {

    // Hiển thị thông báo xoá thành công
    $(".alert-danger").text(text).show();

    // Hide the alert after 3 seconds
    setTimeout(function() {
        $(".alert-danger").hide();
    }, 3000);
}

$(document).on("click", "#Duyet", function (e) {
    e.preventDefault();

    // Lấy giá trị id từ thuộc tính data-id
    var id = $(this).data("id");
    // Chuyển đổi các chuỗi số thành số nguyên
    // var numericIds = id.map(Number);
    updateStatus(id)
})

// Thêm hàm xử lý sự kiện cho checkbox, sử dụng delegated event
$(document).on("change", ".checkbox-del", function () {
    if ($(this).prop("checked")) {
        var id = $(this).val();
    }
});

$("#notApprove").click(function () {
    // Lấy ID của checkbox đầu tiên được chọn
    var id = $(".checkbox-del:checked").first().val();
    if (id) {
        notApprove(id);
    } else {
        error("Vui lòng chọn tối thiếu 1 bài viết!")
    }
})

function notApprove(id) {
    $.ajax({
        type: "PUT",
        url: "http://localhost:8081/new/" + id + "/notApprove",
        success: function (result) {
            // alert("Duyệt thành công")
            loadIndexContent()
            console.log("Đã từ trối duyệt bài viết có ID:", id);
        },
        error: function (error) {
            console.error("Lỗi:", error);
        }
    });
}


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

