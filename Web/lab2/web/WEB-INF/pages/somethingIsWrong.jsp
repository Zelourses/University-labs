<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="ru">
<head>
    <title>Что-то пошло не так!</title>
    <style type="text/css">
        * {
            font-size: 20px;
        }

        H1 {
            font-family: Tahoma,Arial,sans-serif;
            color: white;
            background-color: #525D76;
            font-size: 22px;
        }

        H2 {
            font-family: Tahoma,Arial,sans-serif;
            color: white;
            background-color: #525D76;
            font-size: 16px;
        }

        H3 {
            font-family: Tahoma,Arial,sans-serif;
            color: white;
            background-color: #525D76;
            font-size: 14px;
        }

        BODY {
            font-family: Tahoma,Arial,sans-serif;
            color: black;
            background-color: white;
        }

        B {
            font-family: Tahoma,Arial,sans-serif;
            color: white;
            background-color: #525D76;
        }

        P {
            font-family: Tahoma,Arial,sans-serif;
            background: white;
            color: black;
            font-size: 12px;
        }

        A {
            color : black;
        }

        HR {
            color : #525D76;
        }

        .error{
            color: #ff0000;
        }
        .gifs {
            max-height: 150px;
        }
    </style>
</head>
<body>
    <h1>Тут произошла небольшая техничненькая неполадочка</h1>
    <hr/>
    <p>
        <b>тип</b>
        <span>отчёт о статусе</span>
    </p>
    <p>
        <b>Сообщение</b>
    <span>Не найдены ЛК, либо же их значение меньше 2.</span>
    </p>
    <p class="error">
        <b>Описание</b>
        <br>Ошибка вызвана в Google Docs(сеть интернект):НеВыставленыЛКИсключение:
        <br>&nbsp;&nbsp;&nbsp;&nbsp;в Google sheets (https://docs.google.com/spreadsheets/d/1hGx52Q6omjFo7D_0L_FXXhb5FyVR2I6m2bmbOKYdEVA)
        <br>&nbsp;&nbsp;&nbsp;&nbsp;в листе (P3201)
        <br>&nbsp;&nbsp;&nbsp;&nbsp;в ячейке (R4)
    </p>
        <br>Рекомендуемые действия:
        <br>Перейдите по ссылке - (<a target="_blank" href="https://docs.google.com/spreadsheets/d/1hGx52Q6omjFo7D_0L_FXXhb5FyVR2I6m2bmbOKYdEVA" >https://docs.google.com/spreadsheets/d/1hGx52Q6omjFo7D_0L_FXXhb5FyVR2I6m2bmbOKYdEVA</a>)
        <br>Либо же перейдите с помощью QR кода: <img src="${pageContext.request.contextPath}/Files/misc/untitled.png" alt="Код">
        <br>Далее, перейдите в лист (P3201):<img class="gifs" src="${pageContext.request.contextPath}/Files/misc/first.gif" alt="Первое действие"><br>
        <br>После того, как вы удачно перешли на лист (P3201), найдите ячейку (R4):<img class="gifs" src="${pageContext.request.contextPath}/Files/misc/second.gif" alt="Второе действие"><br>
        <br>И наконец, последний шаг-поставьте максимально возможное колличество баллов в эту ячейку, и подождите, пока изменения сохранятся:<img class="gifs" src="${pageContext.request.contextPath}/Files/misc/third.gif" alt="Третье действие">
        <br>Скорее всего, проблема будет решена. Типичные ошибки новичка:
        <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Ввести не максимально возможное колличество баллов.
        <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Ввести что-то, что не является числом.
        <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Переход не по той ссылке.
        <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Создание копии таблицы, в которую потом будут внесены данные
    <hr/>
    <h3>Лабораторная работа №2 лаба_2_(финальная v2.312) - копия </h3>
</body>
</html>
