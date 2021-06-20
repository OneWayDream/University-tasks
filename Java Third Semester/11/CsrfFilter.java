package ru.itis.filters;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

public class CsrfFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {

        boolean isCorrect = true;
        String uri = req.getRequestURI().substring(req.getContextPath().length());
        if (req.getMethod().equals("GET")){
            if (uri.equals("/profile-edit")){
                String csrf = UUID.randomUUID().toString();
                req.getSession().setAttribute("_csrf_token_profile_edit", csrf);
                req.setAttribute("_csrf_token_profile_edit", csrf);
            }
        } else if (req.getMethod().equals("POST")){
            if (uri.equals("/profile-edit")){
                Object reqUuid = req.getParameter("userdata__csrf_token_profile_edit");
                reqUuid = (reqUuid==null) ? req.getParameter("_password_csrf_token_profile_edit1") : reqUuid;
                reqUuid = (reqUuid==null) ? req.getParameter("_delete_csrf_token_profile_edit") : reqUuid;
                if (!req.getSession().getAttribute("_csrf_token_profile_edit").equals(reqUuid)){
                    isCorrect = false;
                }
            }
        }

        if (isCorrect){
            chain.doFilter(req, res);
        } else {
            res.sendRedirect(req.getContextPath() + "/main");
        }
    }
}
