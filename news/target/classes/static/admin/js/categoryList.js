$(document).ready(function () {

    function updateTable(categoriesList) {
        $("table tbody").empty(); // Xóa nội dung của tbody trước khi thêm hàng mới

        // Sử dụng Thymeleaf để tạo dòng HTML cho mỗi tin tức
        $.each(categoriesList, function (index, category) {
            // Tạo dòng HTML
            var row = "<tr>" +
                "<td><input type='checkbox' class='checkbox-del' data-id="+ category.id +" id='checkbox_" + category.id + "' value='" + category.id + "'></td>" +
                "<td>" + category.name + "</td>" +
                "<td>" + category.code + "</td>" +
                "<td><a class=\"updateCategory\" href=\"#\" id=\"update\" title='Cập nhật' data-id="+ category.id +" data-name='"+ category.name +"' data-code="+category.code+" >\n" +
                "       <i class=\"fa-regular fa-pen-to-square\"></i>\n" +
                "    </a></td>" +
                "</tr>";
            $("table").append(row);
        });
    }

    // Hàm để thực hiện yêu cầu AJAX và cập nhật bảng khi thành công
    function fetchAndDisplayData() {
        $.ajax({
            type: "GET",
            url: "http://localhost:8081/categories",
            data: {},
            dataType: "json",
            success: function (data) {

                console.table(data)
                // updatePagination(data.totalPage);
                updateTable(data);
            },
            error: function (error) {
                console.error("Lỗi khi lấy dữ liệu từ API:", error);
            }
        });
    }

    fetchAndDisplayData();

    // Khi nhấn submit trong form
    $("#formContainer").submit( function (event) {
        submitFrom(event)

    })

    $(document).on("click", ".updateCategory", function () {
        $("#btn-submit").text("Sửa")
        var id = $(this).data("id");
        var name = $(this).data("name");
        console.log("Name", name)
        var code = $(this).data("code");
        $("#category-id").val(id);
        $("#category-name").val(name);
        $("#category-code").val(code);
        $("#formContainer").show();
        $("#overlay").show();
    });

    function submitFrom(event) {
        event.preventDefault(); // Ngăn chặn việc submit form
        var id = $("#category-id").val();
        var name = $("#category-name").val();
        var code = $("#category-code").val();
        if (id) {
            updateCategory(id, name, code);
        } else {
            addCategory(name, code);
        }
    }

    /*$("#deleteCategory").on("click", function (e) {
        e.preventDefault();
        var ids = $('tbody input[type=checkbox]:checked').map(function () {
        return $(this).val() }).get();
        var numbericIds = ids.map(Number);
        if (numbericIds.length > 0) {
            deleteCategory(numbericIds);
        } else {
            $(".alert-danger").text("Vui lòng chọn bài viết cần xoá!").show()

            setTimeout(function () {
                $(".alert-danger").hide()
            }, 3000)
        }
    });*/
    // Khai báo biến cờ
    var isDeleteCategoryAjaxCalled = false;
    $("#deleteCategory").on("click", function (e) {
        e.preventDefault();
        var ids = $('tbody input[type=checkbox]:checked').map(function () {
            return $(this).val()
        }).get();
        var numbericIds = ids.map(Number);
        if (numbericIds.length > 0) {
            if (!isDeleteCategoryAjaxCalled) {
                deleteCategory(numbericIds);
                isDeleteCategoryAjaxCalled = true;
            } else {
                console.log("Hàm deleteCategoryAjax đã được gọi trước đó.");
            }
        } else {
            $(".alert-danger").text("Vui lòng chọn bài viết cần xoá!").show()

            setTimeout(function () {
                $(".alert-danger").hide()
            }, 3000)
        }
    });

    // Click vào thêm thể loại mới
    $("#btnAddCategory").on("click", function () {
        showFormAddCategory()
    })
})

function showFormAddCategory() {
    $("#btn-submit").text("Thêm")
    $("#formContainer").toggle()
    $("#overlay").toggle()
}

// $(document).on("click", "#fromAddCategory", function () {
//     showFormCategory()
// })

$(document).on("click", "#overlay", function () {
    cancelForm()
})

$(document).on("click", ".cancel", function () {
    cancelForm()
})

function showFormCategory() {
    $("#formContainer").toggle()
    $("#overlay").toggle()
}

function cancelForm() {
    $("#formContainer").hide()
    $("#overlay").hide()
}

function addCategory(name, code) {
    $.ajax({
        type: "POST",
        url: "http://localhost:8081/add-Category",
        data: JSON.stringify({
            name: name,
            code: code
        }),
        contentType: "application/json",
        dataType: "json",
        success: function (response) {
            cancelForm()
            showCategoryList()
            // success()
        },
        error: function (error) {
            $(".alert-danger").text("Lỗi! Thêm không thành công.").show();
            setTimeout(function() {
                $(".alert-danger").hide();
            }, 3000);
        }
    })
}

function success() {
    // Hiển thị thông báo xoá thành công
    $(".alert-success").text("Thành công!").show();

    // Hide the alert after 3 seconds
    setTimeout(function() {
        $(".alert-success").hide();
    }, 3000);
}

function updateCategory(id, name, code) {
    $.ajax({
        type: "PUT",
        url: "http://localhost:8081/categories/" + id,
        data: JSON.stringify({
            name: name,
            code: code
        }),
        contentType: "application/json",
        success: function (response) {
            showCategoryList();

        },
        error: function (error) {
            // Hiển thị thông báo lỗi cập nhật
            alert("Lỗi")
        }
    });
}



function showCategoryList() {
    $.get("/categoryList", function (data) {
        $("#main-content").html(data)

        success()
    })
}

function deleteCategory(ids) {
    if (ids.length > 0) {
        console.log(ids);
        $.ajax({
            type: "DELETE",
            url: "http://localhost:8081/categories",
            contentType: "application/json",
            data: JSON.stringify(ids),
            success: function (response) {
                showCategoryList();
            },
            error: function (xhr, status, errorThrown) {
                if (errorThrown === 'timeout') {
                    $(".alert-danger").text("Lỗi mạng: Không thể kết nối đến máy chủ").show();
                } else {
                    $(".alert-danger").text(xhr.responseText).show(); // Hiển thị nội dung lỗi từ phản hồi của máy chủ
                }
                alert(errorThrown); // In ra lỗi nếu có

            }
        });


    }
}
