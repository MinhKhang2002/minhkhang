$(document).ready(function (){
    loadCategoriesSelect()

    loadRoleCodes()
})
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

function loadRoleCodes() {
    $.ajax({
        url: "http://localhost:8081/allRole",
        type: "GET",
        dataType: "json",
        success: function(data) {
            let uniqueCodes = new Set();

            // Lặp qua dữ liệu từ API và thêm các code duy nhất vào Set
            $.each(data, function(index, role) {
                uniqueCodes.add(role.code);
            });

            // Lấy select element từ DOM
            let $selectElement = $("#RoleCodeSelect");

            // Xóa các option cũ trong select
            $selectElement.empty();

            // Thêm option mặc định
            let defaultOption = $("<option>").val("").text("Chọn vai trò").prop("selected", true).prop("selected", true);
            $selectElement.append(defaultOption);

            // Thêm các option từ Set vào select
            uniqueCodes.forEach(function(code) {
                let option = $("<option>").val(code).text(code);
                $selectElement.append(option);
            });
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.error("Error loading role codes:", textStatus, errorThrown);
        }
    });
}