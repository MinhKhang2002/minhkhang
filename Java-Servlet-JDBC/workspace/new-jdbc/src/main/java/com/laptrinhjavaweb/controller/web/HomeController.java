package com.laptrinhjavaweb.controller.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.laptrinhjavaweb.constant.SystemConstant;
import com.laptrinhjavaweb.dao.IUserDAO;
import com.laptrinhjavaweb.model.CommentModel;
import com.laptrinhjavaweb.model.NewModel;
import com.laptrinhjavaweb.model.UserModel;
import com.laptrinhjavaweb.paging.PageRequest;
import com.laptrinhjavaweb.paging.Pageble;
import com.laptrinhjavaweb.service.ICategoryService;
import com.laptrinhjavaweb.service.ICommentService;
import com.laptrinhjavaweb.service.INewService;
import com.laptrinhjavaweb.service.IUserService;
import com.laptrinhjavaweb.sort.Sorter;
import com.laptrinhjavaweb.utils.*;

@WebServlet(urlPatterns = {"/trang-chu", "/dang-nhap", "/thoat", "/dang-ky"})
public class HomeController extends HttpServlet {
	
	@Inject
	private ICategoryService categoryService;
	
	@Inject
	private IUserService userService;

	@Inject
	INewService newService;

	@Inject
	private IUserDAO userDao;

	@Inject
	private ICommentService commentService;

	
	private static final long serialVersionUID = 2686801510274002166L;

	ResourceBundle resourceBundle = ResourceBundle.getBundle("message");
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		NewModel model = FormUtil.toModel(NewModel.class, request);
		String action = request.getParameter("action");
		if (action != null && action.equals("login")) {
			LoginController.handleLogin(request, response);
		} else if (action != null && action.equals("register")) {
			RegisterController.handleRegister(request, response);
		} else if (action != null && action.equals("logout")) {
			LogoutController.handleLogout(request, response);
		} else {
			Pageble pageble = new PageRequest(model.getPage(), model.getMaxPageItem(),
					new Sorter(model.getSortName(), model.getSortBy()));
			model.setListResult(newService.findAll(pageble));

			// Đảo ngược danh sách ở đây trước khi truyền cho trang JSP
			List<NewModel> reversedList = new ArrayList<NewModel>(model.getListResult());
			Collections.reverse(reversedList);
			request.setAttribute("reversedList", reversedList);

			request.setAttribute("categories", categoryService.findAll());
			if (model != null && model.getType() != null && model.getType().equals(SystemConstant.ITEM)) {
				if (model.getId() != null) {
					model = newService.findOne(model.getId());
					request.setAttribute(SystemConstant.MODEL, model);
				}
				request.setAttribute("categories", categoryService.findAll());
				// Lấy newId từ yêu cầu (thường truyền qua URL hoặc form)
				String newIdParam = request.getParameter("id");
				// Sử dụng newId để lấy danh sách comment
				List<CommentModel> comments = commentService.getCommentsByNewId(Long.valueOf(newIdParam));// chuyển từ String sang Long
				// Đặt danh sách comment vào thuộc tính của request
				request.setAttribute("comments", comments);

				RequestDispatcher rd = request.getRequestDispatcher("/views/web/item.jsp");
				rd.forward(request, response);
			} else {
				request.setAttribute(SystemConstant.MODEL, model);
				RequestDispatcher rd = request.getRequestDispatcher("/views/web/home.jsp");
				rd.forward(request, response);
			}
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		UserModel model = FormUtil.toModel(UserModel.class, request);
		if (action != null && action.equals("login")) {
			model = userService.findByUserNameAndPasswordAndStatus(model.getUserName(), model.getPassword(), 1);
			if (model != null) {
				SessionUtil.getInstance().putValue(request, "USERMODEL", model);
				// Xác thực thành công, lưu ID người dùng vào phiên
				HttpSession session = request.getSession(true);
				session.setAttribute("user", model);
				if (model.getRole().getCode().equals("USER")) {
					response.sendRedirect(request.getContextPath()+"/trang-chu");
				} else if (model.getRole().getCode().equals("ADMIN")) {
					response.sendRedirect(request.getContextPath()+"/admin-home");
				}
			} else {
				response.sendRedirect(request.getContextPath()+"/dang-nhap?action=login&message=username_password_invalid&alert=danger");
			}
		} else if (action != null && action.equals("register")) {
			// Tiến hành thêm người dùng mới
			UserModel userId = userService.addUser(model);
			if (userId != null) {
				// Người dùng đã được thêm thành công
				model = userService.findOne(model.getId()); // Lấy thông tin người dùng vừa thêm
				request.setAttribute("model", model);
			}
		}
	}
}
