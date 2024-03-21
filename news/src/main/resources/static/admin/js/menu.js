
// Sử dụng sự kiện click cho thẻ a
$(document).on("click", "#approveLink", function () {
    loadApproveContent();
})

// Hàm để tải nội dung trang "Duyệt tin tức"
function loadApproveContent() {
    $.get("/teststatus", function(data) {
        // Thay đổi nội dung của thẻ <main>
        $("#main-content").html(data);
    });
}

$(document).on("click", "#listNew", function () {
    loadListNew()
})

function loadListNew() {
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
    loadContent()
})

function loadContent() {
    $.get("/phong-vien", function(data) {
        // Thay đổi nội dung của thẻ <main>
        $("#main-content").html(data);
    });
}