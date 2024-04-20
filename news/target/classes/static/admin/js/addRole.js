$(document).ready(function (){
    loadCategoriesSelect()

    loadRoleCodes()
})
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
            /*loadRoleName(data)*/
            let uniqueCodes = new Set();

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
            });
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.error("Error loading role codes:", textStatus, errorThrown);
        }
    });
}

/*
function loadRoleName(data){
    // Lấy select element từ DOM
    let $selectRoleName = $("#RoleNameSelect");

    // Xóa các option cũ trong select
    $selectRoleName.empty();

    // Thêm option mặc định
    let defaultOption = $("<option>").val("").text("Chọn vai trò").prop("selected", true).prop("selected", true);
    $selectRoleName.append(defaultOption);

    // Thêm các option từ Set vào select
    $.each(data, function(index, role) {
        let option = $("<option>").val(role.code).text(role.name);
        $selectRoleName.append(option);
    });
}*/
