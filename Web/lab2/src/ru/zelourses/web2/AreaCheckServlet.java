package ru.zelourses.web2;

import javax.servlet.*;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@WebServlet("/check")
public class AreaCheckServlet  extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();//Данные хранятся в сессии
        List<Result> results = (List)session.getAttribute("results");
        if (results == null) {
            results = new LinkedList<>();
            session.setAttribute("results", results);
        }
        String inputX = (String)req.getAttribute("x");
        String inputY = (String)req.getAttribute("y");
        String inputR = (String)req.getAttribute("r");

        double x,y,r;
        try {
            x = Double.parseDouble(inputX);
            y = Double.parseDouble(inputY);
            r = Double.parseDouble(inputR);
            results.add(0,new Result(x, y, r, isThisShooted(x, y, r), LocalDateTime.now()));
            session.setAttribute("last_result_status","ok");
        }catch (NumberFormatException e){
            session.setAttribute("last_result_status", "bad_data");
        }
        resp.sendRedirect("results");

    }
    private boolean isThisShooted(double x, double y, double r){
        return ((y <= 0 && x <= 0 && y >= -(x + r)/2 ||
                (y >= 0 && x <= 0 && x*x + y*y <= r*r) ||
                (x >= 0 && y <= 0 && x <= r/2 && y >= -r)));
    }
}
