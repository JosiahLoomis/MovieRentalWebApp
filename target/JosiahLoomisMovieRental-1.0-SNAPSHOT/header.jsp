<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Movie Rental</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/main.css" type="text/css">
    </head>
    <body>
        <header>
            <section>
                <h1><a href="${pageContext.request.contextPath}/">Movie Rental App</a></h1>
                <c:if test="${pageContext.request.userPrincipal != null}">
                    <div class="auth-buttons">
                        <a href="${pageContext.request.contextPath}/logout">
                            <button type="button">Sign Out</button>
                        </a>
                    </div>
                </c:if>
            </section>
        </header>
