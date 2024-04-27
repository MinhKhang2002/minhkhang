
// Sử dụng sự kiện click cho thẻ a
$(document).on("click", "#approveLink", function () {
    loadPageApproveContent();
})

// Hàm để tải nội dung trang "Duyệt tin tức"
function loadPageApproveContent() {
    $.get("/teststatus", function(data) {
        // Thay đổi nội dung của thẻ <main>
        $("#main-content").html(data);
    });
}

$(document).on("click", "#ds-user", function () {
    loadPageListUser()
})

function loadPageListUser() {
    $.get("/listUser", function (data) {
        $("#main-content").html(data);
    })
}

$(document).on("click", "#listNew", function () {
    loadPageListNew()
})

function loadPageListNew() {
    $.get("/ds-bai-viet", function (data) {
        // Thay đổi nội dung của thẻ <main>
        $("#main-content").html(data);
    });
}

function retrieveCategories() {
    const categories = sessionStorage.getItem('categories');
    if (categories) {
        console.log("Categories:", categories);
        // Sử dụng dữ liệu categories đã truy xuất trong logic ứng dụng của bạn, ví dụ: hiển thị chúng trên trang
    } else {
        console.warn("Categories not found in session storage.");
        // Xử lý trường hợp categories không khả dụng
    }
}

// Gọi hàm sau khi quá trình đăng nhập hoàn tất thành công:
retrieveCategories();

$(document).on("click", "#phong-vien", function () {
    loadPageContent()
})

function loadPageContent() {
    $.get("/phong-vien", function(data) {
        // Thay đổi nội dung của thẻ <main>
        $("#main-content").html(data);
    });
}

$(document).on("click", "#ds-category", function () {
    showPageCategoryList()
})

function showPageCategoryList() {
    $.get("/categoryList", function (data) {
        $("#main-content").html(data)
    })
}

$(document).on("click", "#ds-role", function () {
    showPageRoleList()
})

function showPageRoleList() {
    $.get("/roleList", function (data){
        $("#main-content").html(data)
    })
}