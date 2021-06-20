<#import "macro.ftlh" as m/>
<#import "spring.ftl" as spring/>


<html lang="en">
    <head>
        <@m.htmlHeader title="${login}"/>
        <link rel="stylesheet" type="text/css" href="<@spring.url '/static/css/profile/profile.css' />">
        <script type ="text/javascript" src="<@spring.url '/static/js/profile/profile-main.js' />"></script>
    </head>
    <body>
        <div id="select_values" <#if user_background??>user_background="${user_background}"</#if>></div>
        <#if login??>
            <@m.pageHeader isSigned="${is_signed}" login="${login}"/>
        <#else>
            <@m.pageHeader isSigned="${is_signed}" />
        </#if>
        <div class="container emp-profile bg-dark">
            <div class="row">
                <div class="col-md-4">
                    <div class="profile-img">
                        <img src="<@spring.url '/static/images/users-images/${image}.png' />" alt=""/>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="profile-head">
                        <h5 class="text-light">
                            ${login}
                        </h5>
                        <h6 class="${text_color}">
                            ${role}
                        </h6>
                        <ul class="nav nav-tabs" id="myTab" role="tablist">
                            <li class="nav-item">
                                <a class="nav-link active" id="home-tab" data-toggle="tab" href="#home" role="tab" aria-controls="home" aria-selected="true">Main info</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" id="profile-tab" data-toggle="tab" href="#profile" role="tab" aria-controls="profile" aria-selected="false">About</a>
                            </li>
                        </ul>
                    </div>
                </div>
                <div class="col-md-2">
                    <a class="btn btn-secondary" href="<@spring.url '/profile-edit' />" role="button">Edit profile</a>
                    <#if message??>
                        <p class="text-success">${message}</p>
                    </#if>
                </div>
            </div>
            <div class="row">
                <div class="col-md-4"></div>
                <div class="col-md-8">
                    <div class="tab-content profile-tab" id="myTabContent">
                        <div class="tab-pane fade show active" id="home" role="tabpanel" aria-labelledby="home-tab">
                            <div class="row">
                                <div class="col-md-6 text-light">
                                    <label>Firstname</label>
                                </div>
                                <div class="col-md-6 text-light">
                                    <p>${first_name}</p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6 text-light">
                                    <label>Minecraft nickname</label>
                                </div>
                                <div class="col-md-6 text-light">
                                    <p>${nickname}</p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6 text-light">
                                    <label>Email</label>
                                </div>
                                <div class="col-md-6 text-light">
                                    <p>${email}</p>
                                </div>
                            </div>
                        </div>
                        <div class="tab-pane fade" id="profile" role="tabpanel" aria-labelledby="profile-tab">
                            <div class="row">
                                <div class="col-md-6 text-light">
                                    <label>Gender</label>
                                </div>
                                <div class="col-md-6 text-light">
                                    <p>${gender}</p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6 text-light">
                                    <label>Country</label>
                                </div>
                                <div class="col-md-6 text-light">
                                    <p>${country}</p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6 text-light">
                                    <label>VK</label>
                                </div>
                                <div class="col-md-6 text-light">
                                    <p>${vk}</p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6 text-light">
                                    <label>Facebook</label>
                                </div>
                                <div class="col-md-6 text-light">
                                    <p>${facebook}</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <@m.htmlFooter/>
    </body>
</html>