package ru.itis.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import ru.itis.exceptions.CookiesRepositoryException;
import ru.itis.exceptions.UsersRepositoryException;
import ru.itis.models.SessionCookie;
import ru.itis.models.User;
import ru.itis.services.SecurityService;
import ru.itis.services.SecurityServiceImpl;
import ru.itis.services.UsersService;
import ru.itis.services.UsersServiceImpl;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;


public class AuthFilter extends HttpFilter {

    protected final String[] protectedPaths = {"/profile", "/profile-edit"};
    protected final String[] unprotectedPaths = {"/sign-up", "/sign-in"};
    private SecurityService securityService;
    private UsersService usersService;

    private static final Logger logger = LoggerFactory.getLogger(
            AuthFilter.class);

    @Override
    public void init(FilterConfig config) throws ServletException {
        ApplicationContext applicationContext = (ApplicationContext) config.getServletContext().getAttribute("applicationContext");
        this.securityService = applicationContext.getBean(SecurityServiceImpl.class);
        this.usersService = applicationContext.getBean(UsersServiceImpl.class);

        super.init(config);
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        boolean prot= false;
        for(String path : protectedPaths){
            if(path.equals(req.getRequestURI().substring(req.getContextPath().length()))){
                prot = true;
                break;
            }
        }
        try{
            boolean isSigned = securityService.isSigned(req);
            if(prot && ! isSigned){
                req.getSession().setAttribute("signInMessage", "Please, sign in.");
                res.sendRedirect(req.getContextPath() + "/sign-in");
            } else{
                if(isSigned){
                    boolean noProt = false;
                    for(String path : unprotectedPaths){
                        if(path.equals(req.getRequestURI().substring(req.getContextPath().length()))){
                            noProt = true;
                            break;
                        }
                    }
                    if (noProt){
                        res.sendRedirect(req.getContextPath() + "/main");
                    } else {
                        if (req.getSession().getAttribute("user")==null){
                            Cookie[] cookies = req.getCookies();
                            if (cookies!=null){
                                for (int i = 0; i < cookies.length; i++){
                                    if (cookies[i].getName().equals("sessionCookie")){
                                        Long user_id = securityService.findUserIdByCookie(cookies[i].getValue());
                                        if (user_id!=null){
                                            User user = usersService.findUserById(user_id).get();
                                            req.getSession().setAttribute("user", user);
                                        } else {
                                            logger.info("User " + req.getRemoteAddr() + " try to enter with incorrect cookie");
                                            req.getSession().setAttribute("/exception", true);
                                            req.getSession().setAttribute("cookieErrorMessage",
                                                                "You have the required cookie for our site, but it has the wrong ID. If you are trying to deceive us, you will fail! We killed your broken cookie anyway. So sign in again and be happy)");
                                            res.sendRedirect(req.getContextPath() + "/exception");
                                        }
                                    }
                                }
                            }
                        } else {
                            req.getSession().setAttribute("user", req.getSession().getAttribute("user"));
                        }
                        chain.doFilter(req, res);
                    }
                } else {
                    chain.doFilter(req, res);
                }
            }
        } catch (CookiesRepositoryException| UsersRepositoryException ex){
            logger.error("User " + req.getRemoteAddr() + " get unexpected exception.");
            req.getSession().setAttribute("/exception", true);
            req.getSession().setAttribute("unexpectedExceptionMessage",
                    "Congratulations, you unlocked a secret achievement and ended up on this page with an error that should never have occurred. Contact us, we will buy you a cake and fix this flaw, thank you.");
            res.sendRedirect(req.getContextPath() + "/exception");
        }
    }
}