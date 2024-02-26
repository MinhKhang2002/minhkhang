package com.laptrinhjavaweb.controller.web.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laptrinhjavaweb.model.CommentModel;
import com.laptrinhjavaweb.model.UserModel;
import com.laptrinhjavaweb.service.ICommentService;
import com.laptrinhjavaweb.utils.FormUtil;
import com.laptrinhjavaweb.utils.HttpUtil;
import com.laptrinhjavaweb.utils.SessionUtil;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/api-web-comment"})
public class CommentAPI extends HttpServlet {

    @Inject
    private ICommentService commentService;

    private static final long serialVersionUID = -915988021506484384L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        CommentModel commentModel = HttpUtil.of(request.getReader()).toModel(CommentModel.class);
//        commentModel.setCreatedBy(((UserModel) SessionUtil.getInstance().getValue(request, "USERMODEL")).getUserName());
        commentModel = commentService.addComment(commentModel);
        mapper.writeValue(response.getOutputStream(), commentModel);
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
