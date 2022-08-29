package com.zoolatech.webtask.servlet;

import com.zoolatech.webtask.file_management.FileManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(value = "/table/*")
public class TableServlet extends HttpServlet {

    private FileManager manager;
    private String dbPath;
    private String tableName;

    @Override
    public void init() {
        manager = new FileManager();
        dbPath = this.getClass().getClassLoader().getResource("dbs").getPath();
    }

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

        if (new File(dbPath + tableName).createNewFile()) {
            System.out.println("File created: " + tableName);
        } else {
            System.out.println("File already exists.");
        }
        response.sendRedirect("/tables");
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        tableName = request.getPathInfo();
        File file = new File(dbPath + tableName);

        if (!file.exists()) {
            System.out.println("User trying to update non-existent file");
        } else {
            try (FileWriter fileWriter = new FileWriter(dbPath + tableName)) {
                fileWriter.write(manager.convertJson(request));
            }
            response.sendRedirect("/table/" + tableName);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        tableName = request.getPathInfo();
        File file = new File(dbPath + tableName);

        if (file.delete()) {
            System.out.println("File '" + file.getName() + "' deleted");
            response.sendRedirect("/tables");
        } else {
            System.out.println("File '" + file.getName() + "' doesn't exist");
        }
    }
}
