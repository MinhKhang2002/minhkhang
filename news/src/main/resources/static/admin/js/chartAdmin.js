$(document).ready(function () {

    var selectElement = document.getElementById("timePeriod");
    selectElement.addEventListener("change", fetchDataAndDrawChart);

// Mở đầu vẽ biểu đồ khi trang được tải lần đầu
    fetchDataAndDrawChart();
});

/*function fetchDataAndDrawChart() {
    const selectedPeriod = document.getElementById('timePeriod').value;
    let endDate = new Date();
    let startDate;

    const currentDate = new Date();
    const oneDay = 24 * 60 * 60 * 1000; // milliseconds in a day

    switch (selectedPeriod) {
        case 'week':
            startDate = new Date(currentDate.getTime() - (7 * oneDay));
            endDate = currentDate;
            fetchChartData(startDate, endDate);
            break;
        case 'month':
            startDate = new Date(currentDate.getFullYear(), currentDate.getMonth() - 1, 1);
            endDate = new Date(currentDate.getFullYear(), currentDate.getMonth(), 0);
            fetchChartData(startDate, endDate);
            break;
        case 'quarter':
            startDate = new Date(currentDate.getFullYear(), currentDate.getMonth() - 3, 1);
            endDate = currentDate;
            fetchChartData(startDate, endDate);
            break;
        default:
            startDate = new Date(currentDate.getTime() - (7 * oneDay));
            endDate = currentDate;
            fetchChartData(startDate, endDate);
            break;
    }
}*/

function fetchDataAndDrawChart() {
    const selectedPeriod = document.getElementById('timePeriod').value;
    let endDate = new Date();
    let startDate;

    const currentDate = new Date();
    const oneDay = 24 * 60 * 60 * 1000; // milliseconds in a day

    switch (selectedPeriod) {
        /*case 'week':
            startDate = new Date(currentDate.getTime() - (7 * oneDay));
            endDate = currentDate;
            fetchChartData(startDate, endDate);
            fetchLast7DaysData();
            break;
        case 'month':
            startDate = new Date(currentDate.getFullYear(), currentDate.getMonth() - 1, 1);
            endDate = new Date(currentDate.getFullYear(), currentDate.getMonth(), 0);
            fetchChartData(startDate, endDate);
            fetchLastMonthData();
            break;
        case 'quarter':
            startDate = new Date(currentDate.getFullYear(), currentDate.getMonth() - 3, 1);
            endDate = currentDate;
            fetchChartData(startDate, endDate);
            fetchLastQuarterData();
            break;
        case 'last7Days':
            fetchLast7DaysData();
            break;
        case 'lastMonth':
            fetchLastMonthData();
            break;
        case 'lastQuarter':
            fetchLastQuarterData();
            break;
        default:
            startDate = new Date(currentDate.getTime() - (7 * oneDay));
            endDate = currentDate;
            fetchChartData(startDate, endDate);
            break;*/
        case 'week':
            /*startDate = new Date(currentDate.getTime() - (7 * oneDay));*/
            startDate = new Date(endDate.getTime() - (6  * oneDay));
            // endDate = currentDate;
            fetchLast7DaysData();  // Gọi hàm này cho biểu đồ so sánh
            fetchChartData(startDate);  // Gọi hàm này cho biểu đồ thống kê
            break;
        case 'month':
            startDate = new Date(currentDate.getFullYear(), currentDate.getMonth(), 1, 11, 0 ,0); // First day of the current month
            console.log("Start Date: ", startDate)
            // endDate = new Date(currentDate.getFullYear(), currentDate.getMonth(), 0);
            fetchLastMonthData()
            fetchChartData(startDate);
            break;
        case 'quarter':
            startDate = new Date(currentDate.getFullYear(), currentDate.getMonth() - 3, 1);
            // endDate = currentDate;
            fetchLastQuarterData()
            fetchChartData(startDate);
            break;
        default:
            startDate = new Date(currentDate.getTime() - (6 * oneDay));
            // endDate = currentDate;
            fetchLast7DaysData()
            fetchChartData(startDate);
            break;
    }
}

function fetchLast7DaysData() {
    fetch(`/new/countByLast7Days`)
        .then(response => response.json())
        .then(data => {
            drawAreaChart(data);
        })
        .catch(error => {
            console.error("Error fetching data:", error);
        });
}

function fetchLastMonthData() {
    fetch(`/new/countByLastMonth`)
        .then(response => response.json())
        .then(data => {
            drawAreaChart(data);
        })
        .catch(error => {
            console.error("Error fetching data:", error);
        });
}

function fetchLastQuarterData() {
    fetch(`/new/countByLastQuarter`)
        .then(response => response.json())
        .then(data => {
            drawAreaChart(data);
        })
        .catch(error => {
            console.error("Error fetching data:", error);
        });
}

function groupDataByWeek(data) {
    const weeklyData = {};
    data.forEach(item => {
        const date = new Date(item.date);
        const weekNumber = getWeekNumber(date);
        weeklyData[weekNumber] = (weeklyData[weekNumber] || 0) + item.count;
    });
    return Object.values(weeklyData);
}

function groupDataByMonth(data) {
    const monthlyData = {};
    data.forEach(item => {
        const date = new Date(item.date);
        const month = date.getMonth();
        monthlyData[month] = (monthlyData[month] || 0) + item.count;
    });
    return Object.values(monthlyData);
}

function getWeekNumber(date) {
    const firstDayOfYear = new Date(date.getFullYear(), 0, 1);
    const pastDays = (date - firstDayOfYear) / 86400000;
    return Math.ceil((pastDays + firstDayOfYear.getDay() + 1) / 7);
}

function fetchChartData(startDate) {
    const startDateStr = startDate.toISOString().split('T')[0];
    // const endDateStr = endDate.toISOString().split('T')[0];
// &endDate=${endDateStr}

    fetch(`/new/countByDateRange?startDate=${startDateStr}`)
        .then(response => response.json())
        .then(data => {
            drawBarChart(data);
        })
        .catch(error => {
            console.error("Error fetching data:", error);
        });
}

let myBarChart = null; // Biến để lưu trữ biểu đồ cột
let myAreaChart = null; // Biến để lưu trữ biểu đồ diện tích

function drawBarChart(dataFromApi) {
    // Tiêu diệt biểu đồ cột hiện tại nếu tồn tại
    if (myBarChart) {
        myBarChart.destroy();
    }

    // Xử lý dữ liệu để trích xuất ngày và số lượng
    const dates = dataFromApi.map(item => new Date(item.date).toLocaleDateString());
    const counts = dataFromApi.map(item => item.count);

    // Vẽ biểu đồ cột
    var ctxBar = document.getElementById('myBarChart').getContext('2d');
    myBarChart = new Chart(ctxBar, {
        type: 'bar',
        data: {
            labels: dates,
            datasets: [{
                label: 'Thống kê bài viết',
                data: counts,
                backgroundColor: 'rgba(75, 192, 192, 0.2)',
                borderColor: 'rgba(75, 192, 192, 1)',
                borderWidth: 1
            }]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
}

function drawAreaChart(dataFromApi) {
    // Tiêu diệt biểu đồ diện tích hiện tại nếu tồn tại
    if (myAreaChart) {
        myAreaChart.destroy();
    }

    // Xử lý dữ liệu để trích xuất ngày và số lượng
    const dates = dataFromApi.map(item => new Date(item.date).toLocaleDateString());
    const counts = dataFromApi.map(item => item.count);

    // Vẽ biểu đồ diện tích
    const areaCounts = counts.map(count => count * 1.5); // Ví dụ tăng giá trị lên 1.5 lần
    var ctxArea = document.getElementById('myAreaChart').getContext('2d');
    myAreaChart = new Chart(ctxArea, {
        type: 'line',
        data: {
            labels: dates,
            datasets: [{
                label: 'Biểu đồ so sánh bài viết',
                data: areaCounts,
                backgroundColor: 'rgba(255, 99, 132, 0.2)',
                borderColor: 'rgba(255, 99, 132, 1)',
                borderWidth: 1
            }]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
}

// Hàm để lấy thứ của ngày
function getDayOfWeek(dateStr) {
    const date = new Date(dateStr);
    const days = ['SUNDAY', 'MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY'];
    return days[date.getDay()];
}

/*function drawChart(dataFromApi) {
    // Tiêu diệt biểu đồ hiện tại nếu tồn tại
    if (myBarChart) {
        myBarChart.destroy();
    }

    if (myAreaChart) {
        myAreaChart.destroy();
    }

    // Xử lý dữ liệu để trích xuất ngày và số lượng
    const dates = dataFromApi.map(item => new Date(item.date).toLocaleDateString());
    const counts = dataFromApi.map(item => item.count);

    // Vẽ biểu đồ cột
    var ctxBar = document.getElementById('myBarChart').getContext('2d');
    myBarChart = new Chart(ctxBar, {
        type: 'bar',
        data: {
            labels: dates,
            datasets: [{
                label: 'Thống kê bài viết',
                data: counts,
                backgroundColor: 'rgba(75, 192, 192, 0.2)',
                borderColor: 'rgba(75, 192, 192, 1)',
                borderWidth: 1
            }]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });

    // Vẽ biểu đồ diện tích
    const areaCounts = counts.map(count => count * 1.5); // Ví dụ tăng giá trị lên 1.5 lần
    var ctxArea = document.getElementById('myAreaChart').getContext('2d');
    myAreaChart = new Chart(ctxArea, {
        type: 'line',
        data: {
            labels: dates,
            datasets: [{
                label: 'Biểu đồ so sánh bài viết',
                data: areaCounts,
                backgroundColor: 'rgba(255, 99, 132, 0.2)',
                borderColor: 'rgba(255, 99, 132, 1)',
                borderWidth: 1
            }]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
}*/


/*function fetchDataAndDrawChart() {
    const selectedPeriod = document.getElementById('timePeriod').value;
    let endDate = new Date();
    let startDate;

    /!*if (selectedPeriod === "week") {
        startDate = new Date(endDate);
        startDate.setDate(endDate.getDate() - 7);
    } else if (selectedPeriod === "month") {
        startDate = new Date(endDate);
        startDate.setMonth(endDate.getMonth() - 1);
    } else if (selectedPeriod === "quarter") {
        startDate = new Date(endDate);
        startDate.setDate(endDate.getDate() - 90);
    }*!/
    const currentDate = new Date();
    const oneDay = 24 * 60 * 60 * 1000; // milliseconds in a day

    switch (selectedPeriod) {
        case 'week':
            startDate = new Date(currentDate.getTime() - (7 * oneDay));
            endDate = currentDate;
            break;
        case 'month':
            startDate = new Date(currentDate.getFullYear(), currentDate.getMonth() - 1, 1);
            endDate = new Date(currentDate.getFullYear(), currentDate.getMonth(), 0);
            break;
        case 'quarter':
            startDate = new Date(currentDate.getFullYear(), currentDate.getMonth() - 3, 1);
            endDate = currentDate;
            break;
        default:
            startDate = new Date(currentDate.getTime() - (7 * oneDay));
            endDate = currentDate;
            break;
    }

    // Convert dates to string format 'yyyy-MM-dd'
    const startDateStr = startDate.toISOString().split('T')[0];
    const endDateStr = endDate.toISOString().split('T')[0];

    fetch(`/new/countByDateRange?startDate=${startDateStr}&endDate=${endDateStr}`)
        .then(response => response.json())
        .then(data => {
            drawChart(data);
        })
        .catch(error => {
            console.error("Error fetching data:", error);
        });
}

let myBarChart = null; // Biến để lưu trữ biểu đồ

function drawChart(dataFromApi) {
    // Tiêu diệt biểu đồ hiện tại nếu tồn tại
    if (myBarChart) {
        myBarChart.destroy();
    }

    // Xử lý dữ liệu để trích xuất ngày và số lượng
    const dates = dataFromApi.map(item => new Date(item.date).toLocaleDateString());
    const counts = dataFromApi.map(item => item.count);

    // Vẽ biểu đồ
    var ctx = document.getElementById('myBarChart').getContext('2d');
    myBarChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: dates,
            datasets: [{
                label: 'Thống kê bài viết',
                data: counts,
                backgroundColor: 'rgba(75, 192, 192, 0.2)',
                borderColor: 'rgba(75, 192, 192, 1)',
                borderWidth: 1
            }]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
}*/

/*
function drawChart(dataFromApi) {
    // Xử lý dữ liệu để trích xuất ngày và số lượng
    const dates = dataFromApi.map(item => new Date(item.date).toLocaleDateString());
    const counts = dataFromApi.map(item => item.count);

    // Vẽ biểu đồ
    var ctx = document.getElementById('myBarChart').getContext('2d');
    var myBarChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: dates,
            datasets: [{
                label: 'Thống kê bài viết',
                data: counts,
                backgroundColor: 'rgba(75, 192, 192, 0.2)',
                borderColor: 'rgba(75, 192, 192, 1)',
                borderWidth: 1
            }]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
}*/
