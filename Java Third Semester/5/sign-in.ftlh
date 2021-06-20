<#import "macro.ftlh" as m />
<#import "spring.ftl" as spring/>

<html lang="en">
    <head>
        <@m.htmlHeader title="Sign In"/>
        <!-- Font Icon -->
        <link rel="stylesheet" type="text/css" href="<@spring.url '/static/css/sign/material-design-iconic-font.min.css' />">
        <!-- Main css -->
        <link rel="stylesheet" type="text/css" href="<@spring.url '/static/css/sign/style.css' />">

        <link rel="stylesheet" type="text/css" href="<@spring.url '/static/css/sign/sign-forms.css' />">
        <script type ="text/javascript" src="<@spring.url '/static/js/sign/sign-main.js' />"></script>
    </head>
    <body>
        <div id="select_values"
             <#if user_background??>user_background="${user_background}"</#if>></div>
        <#if login??>
            <@m.pageHeader isSigned="${is_signed}" login="${login}"/>
        <#else>
            <@m.pageHeader isSigned="${is_signed}"/>
        </#if>
        <div>
            <section>
                <div class="container bg-dark">
                    <div class="signin-content">
                        <div class="signin-form">
                            <h3 class="form-title text-light">Sign in</h3>
                            <#if message??>
                                <h4 class="text-danger">${message}</h4>
                            </#if>
                            <form method="POST" class="register-form" id="login-form">
                                <div class="form-group">
                                    <label for="login"><i class="zmdi zmdi-account material-icons-name"></i></label>
                                    <input type="text" name="login" id="login" placeholder="Your Login"
                                           <#if user_login??>value="${user_login}"</#if> required minlength="3" maxlength="30"/>
                                </div>
                                <div class="form-group">
                                    <label for="your_pass"><i class="zmdi zmdi-lock"></i></label>
                                    <input type="password" name="your_pass" id="your_pass" placeholder="Password" required minlength="8" maxlength="40"/>
                                </div>
                                <div class="form-group">
                                    <input type="checkbox" name="remember-me" id="remember-me" class="agree-term" />
                                    <label for="remember-me" class="label-agree-term text-light"><span><span></span></span>Remember me</label>
                                </div>
                                <input type="hidden" name="user_background" id="user_background"
                                       <#if user_background??>value="${user_background}"</#if>>
                                <div class="form-group form-button">
                                    <input type="submit" name="signin" id="signin" class="form-submit" value="Log in"/>
                                </div>
                            </form>
                            <a href="<@spring.url '/sign-up' />" class="signup-image-link text-light">Create an account</a>
                        </div>
                    </div>
                </div>
            </section>
        </div>

        <!-- JS -->
        <script type ="text/javascript" src="<@spring.url '/static/js/sign/jquery-ui.min.js' />"></script>
        <@m.htmlFooter/>
    </body>
</html>