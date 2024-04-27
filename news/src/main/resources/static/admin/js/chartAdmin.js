$(document).ready(function () {

    var selectElement = document.getElementById("timePeriod");
    selectElement.addEventListener("change", fetchDataAndDrawChart);

// Mở đầu vẽ biểu đồ khi trang được tải lần đầu
    fetchDataAndDrawChart();
});

function fetchDataAndDrawChart() {
    const selectedPeriod = document.getElementById('timePeriod').value;
    let endDate = new Date();
    let startDate;

    const currentDate = new Date();
    const oneDay = 24 * 60 * 60 * 1000; // milliseconds in a day
    const startMonth = currentDate.getMonth();
    const startYear = currentDate.getFullYear();
    let newMonth, newYear;

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
            newMonth = startMonth - 1 < 0 ? 12 + (startMonth - 1) : startMonth - 1;
            newYear = startMonth - 1 < 0 ? startYear - 1 : startYear;
            startDate = new Date(newYear, newMonth, currentDate.getDate(), currentDate.getHours(), currentDate.getMinutes(), currentDate.getSeconds());
            console.log("Start Date: ", startDate)
            // endDate = new Date(currentDate.getFullYear(), currentDate.getMonth(), 0);
            fetchLastMonthData()
            fetchChartData(startDate);
            break;
        case 'quarter':
            newMonth = startMonth - 3 < 0 ? 12 + (startMonth - 3) : startMonth - 3;
            newYear = startMonth - 3 < 0 ? startYear - 1 : startYear;
            startDate = new Date(newYear, newMonth, currentDate.getDate(), currentDate.getHours(), currentDate.getMinutes(), currentDate.getSeconds());
            console.log("Quý: ", startDate);
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
    var ctxArea = document.getElementById('myAreaChart').getContext('2d');
    myAreaChart = new Chart(ctxArea, {
        type: 'line',
        data: {
            labels: dates,
            datasets: [{
                label: 'Biểu đồ so sánh bài viết',
                data: counts,  // Sử dụng giá trị count gốc
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

