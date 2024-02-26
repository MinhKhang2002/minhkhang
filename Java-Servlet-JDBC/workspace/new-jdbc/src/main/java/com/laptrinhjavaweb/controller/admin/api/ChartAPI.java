package com.laptrinhjavaweb.controller.admin.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laptrinhjavaweb.model.ChartModel;
import com.laptrinhjavaweb.model.CommentModel;
import com.laptrinhjavaweb.service.IChartService;
import com.laptrinhjavaweb.utils.HttpUtil;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/api-admin-chart"})
public class ChartAPI extends HttpServlet {
	
	@Inject
	private IChartService chartService;

	private static final long serialVersionUID = -915988021506484384L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		ChartModel chartModel = HttpUtil.of(request.getReader()).toModel(ChartModel.class);

		List<ChartModel> commentOfDay = chartService.countCommentOfDay();

		// Chuyển đổi đối tượng kết quả thành chuỗi JSON và gửi về client
		String jsonResponse = mapper.writeValueAsString(commentOfDay);
		response.getWriter().write(jsonResponse);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}
	
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}
}
