
    document.getElementById("supportLink").addEventListener("click", function(event) {
    event.preventDefault(); // Ngăn chặn hành động mặc định của liên kết
    document.getElementById("contactSection").scrollIntoView({ behavior: 'smooth' }); // Chuyển hướng đến phần body mục tiêu với hiệu ứng smooth scroll
});
