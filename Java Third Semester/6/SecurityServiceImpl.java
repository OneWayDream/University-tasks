package ru.itis.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.itis.exceptions.CookiesRepositoryException;
import ru.itis.exceptions.NoSuchEntityException;
import ru.itis.exceptions.UsersRepositoryException;
import ru.itis.models.SessionCookie;
import ru.itis.models.User;
import ru.itis.repositories.SessionCookiesRepository;
import ru.itis.repositories.UsersRepository;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.UUID;

public class SecurityServiceImpl implements SecurityService {

    protected SessionCookiesRepository sessionCookiesRepository;
    protected UsersRepository usersRepository;
    protected PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(
            SecurityServiceImpl.class);

    public SecurityServiceImpl (SessionCookiesRepository sessionCookiesRepository, UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.sessionCookiesRepository = sessionCookiesRepository;
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public int verifyUserData(User user, String repeatedPassword, String userAccess) {
        int result;
        if     ((!user.getLogin().equals(""))&&
                (!user.getEmail().equals(""))&&
                (!user.getPassword().equals(""))&&
                (!repeatedPassword.equals(""))){
            if (user.getPassword().equals(repeatedPassword)){
                if (userAccess!=null){
                    boolean correctEmail = user.getEmail().matches("[\\w\\-]+@([\\w\\-]+\\.)+([\\w\\-]+)$");
                    if (correctEmail){
                        if (user.getLogin().length()>2){
                            if (user.getLogin().length()<=30){
                                if (user.getEmail().length()<=40){
                                    if (user.getPassword().length()>=8){
                                        if (user.getPassword().length()<=40){
                                            result = 10;
                                        } else {
                                            result = 9;
                                        }
                                    } else {
                                        result = 8;
                                    }
                                } else {
                                    result = 7;
                                }
                            } else {
                                result = 6;
                            }
                        } else {
                            result = 5;
                        }
                    } else {
                        result = 4;
                    }
                } else {
                    result = 3;
                }
            } else {
                result = 2;
            }
        } else {
            result = 1;
        }
        return result;
    }

    @Override
    public boolean isSigned(HttpServletRequest req) {
        boolean result = false;
        Cookie[] cookies = req.getCookies();
        if (cookies!=null){
            for (int i = 0; i < cookies.length; i++){
                if (cookies[i].getName().equals("sessionCookie")){
                    Optional<SessionCookie> data= (sessionCookiesRepository.findByCookie(cookies[i].getValue()));
                    result = data.isPresent();
                    break;
                }
            }
        }
        return result;
    }

    @Override
    public Cookie signIn(Long id) {
        try {
            String newUUID = UUID.randomUUID().toString();
            sessionCookiesRepository.update(SessionCookie.builder()
                    .userId(id)
                    .sessionId(newUUID)
                    .build());
            logger.info("User " + id + " successfully sign in.");
            return new Cookie("sessionCookie", newUUID);
        } catch (IllegalStateException ex){
            throw new CookiesRepositoryException(ex);
        }
    }

    @Override
    public void addRecord(Long user_id) {
        try {
            sessionCookiesRepository.save(SessionCookie.builder()
                    .userId(user_id)
                    .sessionId(UUID.randomUUID().toString())
                    .build());
            logger.info("Cookie for user " + user_id + " was successfully added");
        } catch (IllegalStateException ex){
            throw new CookiesRepositoryException(ex);
        }
    }

    @Override
    public Long findUserIdByCookie(String session_id){
        try{
            Optional<SessionCookie> data = sessionCookiesRepository.findByCookie(session_id);
            return data.map(SessionCookie::getUserId).orElse(null);
        } catch (IllegalStateException ex){
            throw new CookiesRepositoryException(ex);
        }
    }

    @Override
    public String hashUserPassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public boolean matches(String password, String hash) {
        return passwordEncoder.matches(password, hash);
    }

    @Override
    public void logOut(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie("sessionCookie", "");
        response.addCookie(cookie);
        logger.info("Session for user" + ((User) request.getSession().getAttribute("user")).getId() + " was successfully closed.");
        request.getSession().setAttribute("user", null);
    }

    @Override
    public int checkUserChangeData(User user, String gender) {
        int result;
        try{
            Optional<User> existUser = usersRepository.findByLogin(user.getLogin());
            if ((!existUser.isPresent())||(user.getId().equals(existUser.get().getId()))){
                existUser = usersRepository.findByEMail(user.getEmail());
                if ((!existUser.isPresent())||(user.getId().equals(existUser.get().getId()))){
                    if (user.getLogin().length()>=3){
                        if (user.getLogin().length()<=30){
                            if ((user.getFirstName().length()>=2)||(user.getFirstName().equals(""))){
                                if ((user.getFirstName().length()<=30)||(user.getFirstName().equals(""))){
                                    if ((user.getMinecraftNickname().length()>=4)||(user.getMinecraftNickname().equals(""))){
                                        if ((user.getMinecraftNickname().length()<=30)||(user.getMinecraftNickname().equals(""))){
                                            boolean correctEmail = user.getEmail().matches("[\\w\\-]+@([\\w\\-]+\\.)+([\\w\\-]+)$");
                                            if (correctEmail){
                                                if(user.getEmail().length()<=30){
                                                    if (gender.equals("Male")){
                                                        user.setGender(1);
                                                    } else if (gender.equals("Female")){
                                                        user.setGender(2);
                                                    } else {
                                                        user.setGender(0);
                                                    }
                                                    if ((user.getCountry().length()<=30)||(user.getCountry().equals(""))){
                                                        boolean correctVk = user.getVk().matches("vk\\.com/[\\w]+");
                                                        if ((correctVk)||(user.getVk().equals(""))){
                                                            if ((user.getVk().length()<=30)||(user.getVk().equals(""))){
                                                                boolean correctFacebook = user.getFacebook().matches("facebook\\.com/[\\w/]+");
                                                                if ((correctFacebook)||(user.getFacebook().equals(""))){
                                                                    if ((user.getFacebook().length()<=30)||(user.getFacebook().equals(""))){
                                                                        result = 16;
                                                                    } else {
                                                                        result = 15;
                                                                    }
                                                                } else {
                                                                    result = 14;
                                                                }
                                                            } else {
                                                                result = 13;
                                                            }
                                                        } else {
                                                            result = 12;
                                                        }
                                                    } else {
                                                        result = 11;
                                                    }
                                                } else {
                                                    result = 10;
                                                }
                                            } else {
                                                result = 9;
                                            }
                                        } else {
                                            result = 8;
                                        }
                                    } else {
                                        result = 7;
                                    }
                                } else {
                                    result = 6;
                                }
                            } else {
                                result = 5;
                            }
                        } else {
                            result = 4;
                        }
                    } else {
                        result = 3;
                    }
                } else {
                    result = 2;
                }
            } else {
                result = 1;
            }

            return result;
        } catch (IllegalStateException ex){
            throw new UsersRepositoryException(ex);
        }
    }

    @Override
    public void deleteCookieForUser(User user, HttpServletRequest request, HttpServletResponse response) {
        sessionCookiesRepository.deleteCookieForUser(user);
        request.getSession().setAttribute("user", null);
        Cookie cookie = new Cookie("sessionCookie", "");
        response.addCookie(cookie);
        logger.info("Cookie for user " + user.getId() + " was successfully deleted.");
    }
}
