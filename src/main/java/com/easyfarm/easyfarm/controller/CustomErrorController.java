package com.easyfarm.easyfarm.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String errorPage = "error/error";
        String errorMsg = "An unexpected error occurred";
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                errorPage = "error/404";
                errorMsg = "Page not found";
                log.warn("404 Error occurred. Path: {}", request.getRequestURI());
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                errorPage = "error/403";
                errorMsg = "Access denied";
                log.warn("403 Error occurred. Path: {}", request.getRequestURI());
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                errorPage = "error/500";
                errorMsg = "Internal server error";
                log.error("500 Error occurred. Path: {}", request.getRequestURI());
            }

            model.addAttribute("errorCode", statusCode);
        }

        model.addAttribute("errorMessage", errorMsg);
        return errorPage;
    }
}
