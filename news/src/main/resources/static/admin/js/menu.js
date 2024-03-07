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