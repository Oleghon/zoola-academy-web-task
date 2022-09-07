package com.zoolatech.webtask.servlet;

import com.zoolatech.webtask.file.FileManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "TablesServlet", value = "/tables")
public class TablesServlet extends HttpServlet {
    String[] directoryContent;

    FileManager manager = new FileManager();

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        directoryContent = manager.readAllFiles();
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();

        request.getRequestDispatcher(request.getContextPath() + "fragments/link.html")
                .include(request, response);

        writer.println("<html><body>");
        for (String file : directoryContent) {
            writer.println("<a href=\"/table/" + file + "\">- " + file + "</a>");
            writer.println("<br>");
        }
        writer.println("</body></html>");
    }

}
