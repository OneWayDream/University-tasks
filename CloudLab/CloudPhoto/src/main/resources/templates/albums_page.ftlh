<!doctype html>
<html>
    <head>
        <meta charset=utf-8>
        <title>Фотоархив</title>
    </head>
    <body>
        <h1>Фотоархив</h1>
        <ul>
            <#list 0..albums?size-1 as i>
            <li><a href="album${i}.html">${albums[i]}</a></li>
            </#list>
        </ul>
    </body>
</html>