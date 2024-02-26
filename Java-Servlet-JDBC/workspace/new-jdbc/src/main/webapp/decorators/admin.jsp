<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<title><dec:title default = "Trang chủ" /></title>

	<link rel="stylesheet" href="<c:url value='/template/admin/assets/css/bootstrap.min.css' />" />
    <link rel="stylesheet" href="<c:url value='/template/admin/font-awesome/4.5.0/css/font-awesome.min.css' />" />
    <link rel="stylesheet" href="<c:url value='/template/admin/assets/css/ace.min.css' />" class="ace-main-stylesheet" id="main-ace-style" />
    <script src="<c:url value='/template/admin/assets/js/ace-extra.min.js' />"></script>
    <link rel="stylesheet" href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/themes/smoothness/jquery-ui.css">
    <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script type='text/javascript' src='<c:url value="/template/admin/js/jquery-2.2.3.min.js" />'></script>
    <script src="<c:url value='/template/admin/assets/js/jquery.2.1.1.min.js' />"></script>
    <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
    <script src="<c:url value='/template/paging/jquery.twbsPagination.js' />"></script>
    
    <script src="<c:url value='/ckeditor/ckeditor.js' />"></script>

	<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
	<script>
		google.charts.load('current', {'packages':['corechart']});
		google.charts.setOnLoadCallback(function () {
			var chartData = JSON.parse('${chartList}');
			console.log(chartData);

			drawCombinedChart(chartData);
		});

		function drawCombinedChart(chartData) {
			var currentDate = new Date();
			var combinedData = generateCombinedChartData(currentDate, chartData);
			console.log(combinedData);

			var options = {
				title: 'Daily Comments and News',
				vAxis: { title: 'Count', minValue: 0 },
				hAxis: { title: 'Day' },
				series: {
					0: { type: 'bars', color: '#3366cc' },  // Màu đỏ cho News dc3912
					1: { type: 'line', color: '#dc3912' }   // Màu xanh cho Comments 3366cc
				}
			};

			var chart = new google.visualization.ComboChart(document.getElementById('chart_div_comments_and_news'));
			chart.draw(google.visualization.arrayToDataTable(combinedData), options);
		}

		function generateCombinedChartData(currentDate, data) {
			var result = [['Day', 'Number of Comments', 'Number of News']];

			for (var i = 4; i >= 0; i--) {
				var date = formatDate(new Date(currentDate));
				var comments = 0;
				var news = 0;

				var dataItem = findDataByDate(currentDate, data);
				if (dataItem) {
					comments = dataItem.numberOfComments || 0;
					news = dataItem.numberOfPosts || 0;
				}

				result.push([date, comments, news]);

				currentDate.setDate(currentDate.getDate() - 1);
			}

			// return result.reverse(); // Đảo ngược mảng để hiển thị theo thứ tự ngày tăng dần
			return result;
		}

		function findDataByDate(currentDate, dataArray) {
			var formattedDate = formatDate(currentDate);

			// Kiểm tra xem ngày có trong khoảng thời gian 4 ngày trước đó hay không
			var isInDesiredRange = dataArray.some(item => formatDate(new Date(item.date)) === formattedDate);

			if (isInDesiredRange) {
				return dataArray.find(item => formatDate(new Date(item.date)) === formattedDate);
			} else {
				return null; // Trả về null nếu ngày không nằm trong khoảng thời gian mong muốn
			}
		}

		function formatDate(date) {
			var year = date.getFullYear();
			var month = (date.getMonth() + 1).toString().padStart(2, '0');
			var day = date.getDate().toString().padStart(2, '0');
			return year + '/' + month + '/' + day;
		}
	</script>
</head>
<body class="no-skin">
<%--	header--%>
    <%@ include file="/common/admin/header.jsp" %>
    <!-- header -->
	
	<div class="main-container" id="main-container">
		<script type="text/javascript">
				try{ace.settings.check('main-container' , 'fixed')}catch(e){}
		</script>
		<!-- header -->
    	<%@ include file="/common/admin/menu.jsp" %>
    	<!-- header -->
		
		<dec:body/>
		
		<!-- footer -->
    	<%@ include file="/common/admin/footer.jsp" %>
    	<!-- footer -->
    	
    	<a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse display">
				<i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
		</a>
	</div>

	<script src="<c:url value='/template/admin/assets/js/bootstrap.min.js' />"></script>
	<script src="<c:url value='/template/admin/assets/js/jquery-ui.custom.min.js' />"></script>
	<script src="<c:url value='/template/admin/assets/js/jquery.ui.touch-punch.min.js' />"></script>
	<script src="<c:url value='/template/admin/assets/js/jquery.easypiechart.min.js' />"></script>
	<script src="<c:url value='/template/admin/assets/js/jquery.sparkline.min.js' />"></script>
	<script src="<c:url value='/template/admin/assets/js/jquery.flot.min.js' />"></script>
	<script src="<c:url value='/template/admin/assets/js/jquery.flot.pie.min.js' />"></script>
	<script src="<c:url value='/template/admin/assets/js/jquery.flot.resize.min.js' />"></script>
	<script src="<c:url value='/template/admin/assets/js/ace-elements.min.js' />"></script>
	<script src="<c:url value='/template/admin/assets/js/ace.min.js' />"></script>
	<script src="<c:url value='/template/admin/assets/js/bootstrap.min.js'/>"></script>
	
	<!-- page specific plugin scripts -->
	<script src="<c:url value='/template/admin/assets/js/jquery-ui.min.js'/>"></script>
</body>
</html>