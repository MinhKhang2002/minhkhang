function showLoading() {
    // Hiển thị biểu tượng loading
    $("#loadingIndicator").show();
    $("#overlay").show();
    /*setTimeout(function () {
        hideLoading()
    }, 2000)*/
}

var alert = $(".alert-success")

function hideLoading() {
    // Ẩn biểu tượng loading
    $("#loadingIndicator").hide();
    $("#overlay").hide();

    // Hide the alert after 2 seconds
    setTimeout(function() {
        // alert.hide();
    }, 4000);
}