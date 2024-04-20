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
                "<td><a class=\"updateCategory\" href=\"#\" id=\"update\" title='Cập nhật' data-id="+ category.id +" data-name="+category.name+" data-code="+category.code+" >\n" +
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

    function submitFrom(event) {
        event.preventDefault(); // Ngăn chặn việc submit form
        /*var name = $("#category-name").val()
        var code = $("#category-code").val()

        addCategory(name, code)*/

        var id = $("#category-id").val();
        var name = $("#category-name").val();
        var code = $("#category-code").val();

        if (id) {
            updateCategory(id, name, code);
        } else {
            addCategory(name, code);
        }
    }
})

$(document).on("click", "#fromAddCategory", function () {
    showFormCategory()
})

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

            // Hiển thị lại danh sách thể loại
            showCategoryList()

            //Thông báo thành công
            success()
        },
        error: function (error) {

            // Hiển thị thông báo xoá thành công
            $(".alert-danger").text("Lỗi! Thêm không thành công.").show();

            // Hide the alert after 3 seconds
            setTimeout(function() {
                $(".alert-danger").hide();
            }, 3000);
        }
    })
}

function success() {

    // Hiển thị thông báo xoá thành công
    $(".alert-success").text("Thêm thành công!").show();

    // Hide the alert after 3 seconds
    setTimeout(function() {
        $(".alert-success").hide();
    }, 3000);
}

// update ở đây nè

function updateCategory(id, name, code) {
    $.ajax({
        type: "PUT",
        url: "http://localhost:8081/categories/" + id,
        data: JSON.stringify({
            name: name,
            code: code
        }),
        contentType: "application/json",
        dataType: "json",
        success: function (response) {
            cancelForm();

            // Cập nhật lại bảng sau khi cập nhật thành công
            showCategoryList()

            // Hiển thị thông báo cập nhật thành công
            success()
        },
        error: function (error) {
            // Hiển thị thông báo lỗi cập nhật
            $(".alert-danger").text("Lỗi! Cập nhật không thành công.").show();

            // Hide the alert after 3 seconds
            setTimeout(function() {
                $(".alert-danger").hide();
            }, 3000);
        }
    });
}

$(document).on("click", ".updateCategory", function () {
    var id = $(this).data("id");
    var name = $(this).data("name");
    var code = $(this).data("code");

    $("#category-id").val(id);
    $("#category-name").val(name);
    $("#category-code").val(code);

    $("#formContainer").show();
    $("#overlay").show();
});

/*function updateCategory(id, categoryData){
    $.ajax({
        type: "PUT",
        url: "http://localhost:8081/categories/" + id,
        data:JSON.stringify( {
            name: categoryData.name,
            code: categoryData.code
        }),
        contentType: "application/json",
        dataType: "json",
        success: function(result) {
            alert("Đã sửa danh mục có ID " + id);
            showCategoryList()
        },
        error: function(error) {
            $(".alert-success").text("Thêm thành công!").show();

            // Hide the alert after 3 seconds
            setTimeout(function() {
                $(".alert-success").hide();
            }, 3000);
            showCategoryList()
        }
    });
}
$(document).on("click", "#update", function(e) {
    e.preventDefault();
    $("#formContainer-update").show()
    var categoryId = $(this).data("id");
    var name = $(this).data("name");
    var code =$(this).data("code");
    $("#category-id-update").val(categoryId);
    $("#category-name-update").val(name);
    $("#category-code-update").val(code);
});

$(document).on("click", ".cancel", function () {
    $("#formContainer-update").hide()
 })
$("#formContainer-update").submit( function (event) {
    submitFromUpdate(event)
})

function submitFromUpdate(event) {
    event.preventDefault(); // Ngăn chặn việc submit form
    var name = $("#category-name-update").val()
    var code = $("#category-code-update").val()
    var categoryId = $("#category-id-update").val();

    var categoryData = {
        name: name,
        code: code
    }
    updateCategory(categoryId, categoryData)
}*/

function showCategoryList() {
    $.get("/categoryList", function (data) {
        $("#main-content").html(data)
    })
}


