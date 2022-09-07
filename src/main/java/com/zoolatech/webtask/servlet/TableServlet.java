package com.zoolatech.webtask.servlet;

import com.zoolatech.webtask.file.FileManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(value = "/table/*")
public class TableServlet extends HttpServlet {

    private final FileManager manager = new FileManager();
    private String tableName;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();

        try {
            writer.println("<html><body><pre><h1>");
            writer.println(manager.readFile(request.getRequestURI()));
            writer.println("</h1></pre></body></html>");
        } catch (Exception e) {
            writer.print("<h1>File doesn't exist</h1>");
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        tableName = request.getPathInfo();

        if (manager.createFile(tableName)) {
            System.out.println("File created: " + tableName);
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.sendRedirect("/tables");
        } else {
            System.out.println("File already exists.");
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (manager.updateFile(request.getPathInfo(), request)) {
            response.sendRedirect("/table/" + tableName);
        } else {
            System.out.println("User trying to update non-existent file");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        tableName = request.getPathInfo();

        if (manager.deleteFile(tableName)) {
            System.out.println("File '" + tableName + "' deleted");
            response.sendRedirect("/tables");
        } else {
            System.out.println("File '" + tableName + "' doesn't exist");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
