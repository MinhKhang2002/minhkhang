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

                console.log("Data: ", data)
                // updatePagination(data.totalPage);
                updateTable(data);
            },
            error: function (error) {
                console.error("Lỗi khi lấy dữ liệu từ API:", error);
            }
        });
    }

    /*function updatePagination(totalPages) {
        window.pagObj = $('#pagination').twbsPagination({
            totalPages: totalPages, // Số trang tổng cộng
            visiblePages: 10, // Số trang hiển thị
            onPageClick: function (event, page) {
                fetchAndDisplayData(page, 5); // Gọi hàm để lấy và hiển thị dữ liệu cho trang mới
            }
        });
    }

    fetchAndDisplayData(1, 5);*/

    fetchAndDisplayData();

    // Khi nhấn submit trong form
    $("#formContainer").submit( function (event) {
        submitFrom(event)

    })
    $(document).on("click", ".updateCategory", function () {
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

    $("#deleteCategory").on("click", function () {
      //  e.preventDefault();
        var ids = $('tbody input[type=checkbox]:checked').map(function () {
        return $(this).val() }).get();
        var numbericIds = ids.map(Number);
        if (numbericIds.length > 0) {
            deleteCategory(numbericIds);
        } else {
            alert("Bạn hãy chọn thể loại muốn xóa");
        }
    });
    $("#btnAddCategory").on("click",function () {
        showFormCategory();
    });
})

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
            success()
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
    $(".alert-success").text("Thêm thành công!").show();
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
            alert("Đã sửa thành công");
            showCategoryList();

        },
        error: function (error) {
            // Hiển thị thông báo lỗi cập nhật
            alert(error)
        }
    });
}



function showCategoryList() {
    $.get("/categoryList", function (data) {
        $("#main-content").html(data)
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
                alert(response);
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
