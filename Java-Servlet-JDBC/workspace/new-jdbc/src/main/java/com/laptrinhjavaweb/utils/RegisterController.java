package com.laptrinhjavaweb.utils;

import com.laptrinhjavaweb.constant.SystemConstant;
import com.laptrinhjavaweb.model.NewModel;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ResourceBundle;

public class RegisterController {
    static ResourceBundle resourceBundle = ResourceBundle.getBundle("message");

    public static void handleRegister(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String message = request.getParameter("message");
        String alert = request.getParameter("alert");

        if (message != null && alert != null) {
            request.setAttribute("message", resourceBundle.getString(message));
            request.setAttribute("alert", alert);
        }

        NewModel model = FormUtil.toModel(NewModel.class, request);
        request.setAttribute(SystemConstant.MODEL, model);

        RequestDispatcher rd = request.getRequestDispatcher("/views/register.jsp");
        rd.forward(request, response);
    }
}
