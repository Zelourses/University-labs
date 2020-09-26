<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <meta name="viewport" content="width=device-width,initial-scale=1">
    <meta charset="utf-8">
    <title>лаба_2_(финальная v2.312) - копия</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/Files/css/main.css">
</head>
<body>
<jsp:include page="./header.html"/>

<div class="content ">
    <div class="container">
    <div class="graph-container">
        <div class="panel">
            <canvas id="graph-canvas" class="graph-view retrowave" width="1000" height="1000"></canvas>
        </div>
    </div>

    <div class="">
        <form action="${pageContext.request.contextPath}/control" id="form" method="get">
            <div class="XInput panel-content button-container ">
                <label class="retrowave">Выберите число X </label>
                <p class="x retrowave">
                    <input type="radio" name="X[]" id="x-3" value="-3">-3
                    <input type="radio" name="X[]" id="x-2" value="-2">-2
                    <input type="radio" name="X[]" id="x-1" value="-1">-1
                    <input type="radio" name="X[]" id="x0" value="0">0
                    <input type="radio" name="X[]" id="x1" value="1">1
                    <input type="radio" name="X[]" id="x2" value="2">2
                    <input type="radio" name="X[]" id="x3" value="3">3
                    <input type="radio" name="X[]" id="x4" value="4">4
                    <input type="radio" name="X[]" id="x5" value="5">5

            </div>
            <div class="YInput panel-content button-container">
                <label class="retrowave">Выберите число Y </label>
                <p>
                    <input type="text" id="Y" name="Y" maxlength="7" placeholder="(-5; 5)">
            </div>
            <div class="RInput button-container panel-content">
                <label class="retrowave">
                    Выберите число R </label>
                <p>
                    <input type="text" id="R" name="R" maxlength="7" placeholder="(1;4)">
            </div>
            <div class="err-msg hidden panel-content button-container" id="err-msg"><label  id="text"></label></div>
            <div class="button-container">
                <button type="submit" class="submit" id="submit-button"><a class="retrowave">Проверить</a></button>
            </div>


        </form>
        <button onclick="alert('YOU FELL FOR IT FOOL! NEBESNAYA KARA (ПСЖ)');" class="SkyRage"></button>
    </div>
    </div>
</div>
<script src="${pageContext.request.contextPath}/Files/scripts/grapher.js"></script>
<script src="${pageContext.request.contextPath}/Files/scripts/script.js"></script>
</body>
</html>