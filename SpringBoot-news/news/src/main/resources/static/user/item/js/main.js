$(document).ready(function () {
    // Lấy giá trị ID từ query parameters
    var urlParams = new URLSearchParams(window.location.search);
    var idNewsItem = urlParams.get('id');

    $.ajax({
        type: "GET",
        url: "http://localhost:8081/new/" + idNewsItem,
        dataType: "json",
        success: function (data) {
            showItemNews(data);
            console.log("Data:", data)
            /*$("#content").val(data.content);
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
            loadCategoriesSelect(selectedCategoryCode);*/
        },
        error: function (error) {
            console.error("Lỗi khi lấy dữ liệu bài viết:", error);
        }
    });

    function showItemNews (data) {
        $("#title-item").text(data.title)
        $(".content-item").text(data.content)
        $(".thumbnail-item").attr("src", data.thumbnail)
        console.log("img: ", data.thumbnail)
    }
});