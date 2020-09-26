<%@ page import="ru.zelourses.web2.Result" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Результаты</title>
    <meta name="viewport" content="width=device-width,initial-scale=1">
    <meta charset="utf-8">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Files/css/main.css">
</head>
<body>
<%!
private String transform(LocalDateTime time){
    return time.toString().replace("T"," ");
}

%>
<%
    String resultStatus = (String)session.getAttribute("last_result_status");
    @SuppressWarnings("unchecked")
    List<Result> results = (List<Result>) session.getAttribute("results");
    if (results != null) {
        while (results.size() > 10)
            results.remove(results.size() - 1);
    }

%>

<jsp:include page="header.html"/>
<div class="content">
    <div class="container">
        <div class="graph-view-wr left">
            <div class="panel">
                <canvas id="graph-canvas" class="graph-view" width="1000" height="1000"></canvas>
            </div>
        </div>
        <div class="right">
            <% if (results != null && resultStatus != null) { %>
            <div class="panel">
                <div class="panel__title retrowave">Результат</div>
                <div class="panel__content">
                    <% if ("ok".equals(resultStatus)) { %>
                    <div class="result_hit retrowave"><%= results.get(0).isHit() ? "Есть пробитие" : "Наводчик контужен" %></div>
                    <a href="${pageContext.request.contextPath}/" class="result_again retrowave">Еще раз</a>
                    <% } else if ("wrong_data".equals(resultStatus)) { %>
                    <div class="result_error retrowave">Введены неверные данные</div>
                    <% } %>
                </div>
            </div>
            <% } %>
            <div class="panel">
                <div class="panel__title retrowave">Предыдущие 10 результатов</div>
                <div class="panel__content">
                    <table class="results retrowave">
                        <tr>
                            <th>X</th>
                            <th>Y</th>
                            <th>R</th>
                            <th>Попадание</th>
                            <th>Время</th>
                        </tr>
                        <% if (results != null) {%>
                        <% for (Result result : results) { %>
                        <tr>
                            <td><%= result.getX() %></td>
                            <td><%= result.getY() %></td>
                            <td><%= result.getR() %></td>
                            <td><%= result.isHit() ? "Есть пробитие" : "Наводчик контужен" %></td>
                            <td><%= transform(result.getTimestamp()) %></td>
                        </tr>
                        <% }} %>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="${pageContext.request.contextPath}/Files/scripts/grapher.js"></script>
<script src="${pageContext.request.contextPath}/Files/scripts/result.js"></script>
</body>
</html>