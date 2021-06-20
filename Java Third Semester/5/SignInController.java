package ru.itis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import ru.itis.exceptions.CookiesRepositoryException;
import ru.itis.exceptions.UsersRepositoryException;
import ru.itis.models.User;
import ru.itis.services.SecurityService;
import ru.itis.services.UsersService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public class SignInController implements Controller {

    @Autowired
    private UsersService usersService;
    @Autowired
    private SecurityService securityService;

    @Override
    public ModelAndView handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        if (httpServletRequest.getMethod().equals("GET")){
            ModelAndView modelAndView = new ModelAndView();
            if (httpServletRequest.getSession().getAttribute("signInMessage")!=null){
                modelAndView.addObject("message", httpServletRequest.getSession().getAttribute("signInMessage"));
                httpServletRequest.getSession().setAttribute("signInMessage", null);
            }
            if (httpServletRequest.getSession().getAttribute("user")!=null){
                User user = (User) httpServletRequest.getSession().getAttribute("user");
                modelAndView.addObject("login", user.getLogin());
                modelAndView.addObject("is_signed", "true");
            } else {
                modelAndView.addObject("is_signed", "false");
            }
            String userBackground = (String) httpServletRequest.getSession().getAttribute("background");
            if (userBackground!=null){
                modelAndView.addObject("user_background", userBackground);
            }
            modelAndView.setViewName("sign-in");
            return modelAndView;
        } else if (httpServletRequest.getMethod().equals("POST")){
            ModelAndView modelAndView = new ModelAndView();
            String login = httpServletRequest.getParameter("login");
            String password = httpServletRequest.getParameter("your_pass");
            String userAccess = httpServletRequest.getParameter("remember-me");
            String userBackground = httpServletRequest.getParameter("user_background");
            if (httpServletRequest.getSession().getAttribute("user")!=null){
                User user = (User) httpServletRequest.getSession().getAttribute("user");
                modelAndView.addObject("login", user.getLogin());
                modelAndView.addObject("is_signed", "true");
            } else {
                modelAndView.addObject("is_signed", "false");
            }
            httpServletRequest.getSession().setAttribute("background", userBackground);
            try{
                Optional<User> user = usersService.findUserByLogin(login);
                boolean isUser = false;
                if (user.isPresent()){
                    isUser = securityService.matches(password, user.get().getPassword().trim());
                }
                if (isUser){
                    httpServletRequest.getSession().setAttribute("user", user.get());
                    Cookie cookie = securityService.signIn(user.get().getId());
                    if (userAccess!=null){
                        cookie.setMaxAge(60 * 60 * 12);
                    }
                    httpServletResponse.addCookie(cookie);
                    httpServletResponse.sendRedirect(httpServletRequest.getServletContext().getContextPath()+"/profile");
                } else {
                    modelAndView.addObject("user_background", userBackground);
                    modelAndView.addObject("user_login", login);
                    if (user.isPresent()){
                        modelAndView.addObject("message", "Wrong password entered");
                    } else {
                        modelAndView.addObject("message", "There is no user with this name.");
                    }
                    modelAndView.setViewName("sign-in");
                    return modelAndView;
                }
            } catch (UsersRepositoryException | CookiesRepositoryException ex) {
                modelAndView.addObject("user_background", userBackground);
                modelAndView.addObject("user_login", login);
                modelAndView.addObject("message", "Something went wrong.");
                modelAndView.setViewName("sign-in");
                return modelAndView;
            }
        }
        return null;
    }
}
