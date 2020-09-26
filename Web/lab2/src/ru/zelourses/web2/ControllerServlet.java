package ru.zelourses.web2;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = {"/control", "/results"})
public class ControllerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getServletPath().equals("/control")) {
            if (req.getParameter("R") != null && req.getParameter("X[]") != null && req.getParameter("Y") != null) {
                req.setAttribute("r", req.getParameter("R"));
                req.setAttribute("x", req.getParameter("X[]"));
                req.setAttribute("y", req.getParameter("Y"));
                req.getRequestDispatcher("check").forward(req, resp);

            } else {
                printer(req, resp,"doGet empty x,y,z");
            }
        }else if (req.getServletPath().equals("/results")){
            doPost(req,resp);
        }else {
            if (SheetTest.getResult()){
                req.getRequestDispatcher("/WEB-INF/pages/index.jsp").forward(req,resp);
            }else {
                req.getRequestDispatcher("/WEB-INF/pages/somethingIsWrong.jsp").forward(req,resp);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getServletPath().equals("/results")) {
            req.getRequestDispatcher("/WEB-INF/pages/result.jsp").forward(req, resp);

        }else {
            printer(req, resp, "doPost thing");
        }
    }

    private void printer(HttpServletRequest req, HttpServletResponse resp,String startMessage) throws IOException {
        PrintWriter out = resp.getWriter();
        out.println(startMessage);
        out.println();
        out.println("r-"+req.getParameter("R"));
        out.println("y-"+req.getParameter("Y"));
        out.println("x-"+req.getParameter("X[]"));
    }
}
