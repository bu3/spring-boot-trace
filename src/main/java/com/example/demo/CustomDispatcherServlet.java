package com.example.demo;

import static jakarta.servlet.http.HttpServletResponse.SC_METHOD_NOT_ALLOWED;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.web.servlet.DispatcherServlet;

public class CustomDispatcherServlet extends DispatcherServlet {

    @Override
    protected void doTrace(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("TRACE IS NOT ALLOWED");
        response.sendError(SC_METHOD_NOT_ALLOWED);
    }
}
