package com.laptrinhjavaweb.controller.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laptrinhjavaweb.model.ChartModel;
import com.laptrinhjavaweb.service.IChartService;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/admin-chart"})
public class ChartController extends HttpServlet {
	@Inject
	private IChartService chartService;
	
	private static final long serialVersionUID = 2686801510274002166L;
	
	/*protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Lấy dữ liệu từ cơ sở dữ liệu
		List<ChartModel> chartDataComment = chartService.countCommentOfDay();

		// Chuyển dữ liệu thành chuỗi JSON
		String jsonData = new ObjectMapper().writeValueAsString(chartDataComment);

		// Chuyển dữ liệu vào request để truyền cho JSP
		request.setAttribute("chartDataComment", jsonData);

		RequestDispatcher rd = request.getRequestDispatcher("/views/admin/chart.jsp");
		rd.forward(request, response);
	}*/
	/*protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Lấy dữ liệu từ cơ sở dữ liệu cho bình luận
		List<ChartModel> chartDataComment = chartService.countCommentOfDay();

		// Lấy dữ liệu từ cơ sở dữ liệu cho bài viết
//		List<ChartModel> chartDataNews = chartService.countNewsOfDay();

		// Chuyển dữ liệu thành chuỗi JSON
		String jsonDataComment = new ObjectMapper().writeValueAsString(chartDataComment);
//		String jsonDataNews = new ObjectMapper().writeValueAsString(chartDataNews);

		// Chuyển dữ liệu vào request để truyền cho JSP
		request.setAttribute("chartDataComment", jsonDataComment);
//		request.setAttribute("chartDataNews", jsonDataNews);

		RequestDispatcher rd = request.getRequestDispatcher("/views/admin/chart.jsp");
		rd.forward(request, response);
	}*/
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<ChartModel> chartList = chartService.countNewsAndCommentOfDay();

		// Chuyển dữ liệu thành chuỗi JSON
		String jsonData = new ObjectMapper().writeValueAsString(chartList);

		// Chuyển dữ liệu vào request để truyền cho JSP
		request.setAttribute("chartList", jsonData);

		RequestDispatcher rd = request.getRequestDispatcher("/views/admin/chart.jsp");
		rd.forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
