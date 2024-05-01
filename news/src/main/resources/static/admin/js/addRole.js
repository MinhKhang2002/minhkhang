$(document).ready(function (){
    loadCategoriesSelect()

    loadRoleCodes()

    /*// Disable text input when select is changed
    $('#RoleCodeSelect').change(function() {
        $('#codeRole').prop('disabled', true).val('');
    });

    // Disable select when text input is changed
    $('#codeRole').on('input', function() {
        $('#RoleCodeSelect').prop('disabled', true).val('');
    });

    // Enable text input when select is deselected
    $('#RoleCodeSelect').focusout(function() {
        if ($(this).val() === '') {
            $('#codeRole').prop('disabled', false);
        }
    });

    // Enable select when text input is empty
    $('#codeRole').focusout(function() {
        if ($(this).val() === '') {
            $('#RoleCodeSelect').prop('disabled', false);
        }
    });*/
})


$("#addRoleForm").submit(function(event) {
    event.preventDefault(); // Ngăn chặn việc submit form
    submitForm(event)
});

function submitForm(event) {
    event.preventDefault()

    var nameRole = $("#nameRole").val()
    var roleCode = $("#RoleCodeSelect").val()
    var category = $("#categorySelect").val()

    if(!nameRole || !roleCode || !category) {
        alert("Vui lòng nhập thông tin cần thêm!")
    } else {
        addRole(nameRole, roleCode, category)
    }
}

function addRole(nameRole, roleCode, category) {
    var role = {
        name : nameRole,
        code: roleCode,
        categories: category
    }

    $.ajax({
        type: "POST",
        url: "http://localhost:8081/addRole",
        data: JSON.stringify(role),
        contentType: "application/json",
        dataType: "json",
        success: function (response) {
            showRoleList()
        },
        error: function (error) {
            alert("Lỗi khi thêm vai trò")
        }
    })
}

// Hàm để lấy danh sách thể loại và cập nhật thẻ select
function loadCategoriesSelect() {
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

function loadRoleCodes() {
    $.ajax({
        url: "http://localhost:8081/allRole",
        type: "GET",
        dataType: "json",
        success: function(data) {
            loadRoleName(data)
            /*let uniqueCodes = new Set();

            // Lặp qua dữ liệu từ API và thêm các code duy nhất vào Set
            $.each(data, function(index, role) {
                uniqueCodes.add(role);
            });
            // Lấy select element từ DOM
            let $selectElement = $("#RoleCodeSelect");
            // Xóa các option cũ trong select
            $selectElement.empty();

            // Thêm option mặc định
            let defaultOption = $("<option>").val("").text("Chọn vai trò").prop("selected", true).prop("selected", true);
            $selectElement.append(defaultOption);

            // Thêm các option từ Set vào select
            uniqueCodes.forEach(function(role) {
                let option = $("<option>").val(role.code).text(role.name);
                $selectElement.append(option);
            });*/
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.error("Error loading role codes:", textStatus, errorThrown);
        }
    });
}

function loadRoleName(data){
    let roleMap = {};

    // Lọc các role có mã khác 'USER'
    var filteredRoles = data.filter(function(role) {
        return role.code !== 'USER';
    });

    // Lặp qua dữ liệu từ API và thêm các role vào roleMap
    $.each(filteredRoles, function(index, role) {
        if (role.code === 'ADMIN' || role.code === 'phong-vien') {
            roleMap[role.code] = role.name;
        } else if (role.code.startsWith('ADMIN_MANAGE')) {
            roleMap['ADMIN_MANAGE'] = 'Quản lý thể loại';
            // return false; // Dừng vòng lặp sau khi tìm thấy ADMIN_MANAGE
        }
    });

    // Lấy select element từ DOM
    let $selectElement = $("#RoleCodeSelect");

    // Xóa các option cũ trong select
    $selectElement.empty();

    // Thêm option mặc định
    let defaultOption = $("<option>").val("").text("Chọn vai trò").prop("selected", true);
    $selectElement.append(defaultOption);

    // Thêm các option từ đối tượng roleMap vào select
    $.each(roleMap, function(code, name) {
        let option = $("<option>").val(code).text(name);
        $selectElement.append(option);
    });
}

$(document).on("click", "#cancel", function () {
    showLoading()
    $.get("/roleList", function (data){
        $("#main-content").html(data)

        hideLoading()
    })
})



function showRoleList() {
    showLoading()
    $.get("/roleList", function (data){
        $("#main-content").html(data)

        hideLoading()

        $(".alert-success").text("Thành công").show()

        setTimeout(function () {
            $(".alert-success").hide()
        }, 3000)
    })
}
