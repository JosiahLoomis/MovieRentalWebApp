<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="header.jsp" %>
<section>
    <h1>Login</h1>
    <form action="${pageContext.request.contextPath}/j_security_check" method="post">
        <label>Username:</label>
        <input type="text" name="j_username" required><br>
            
        <label>Password:</label>
        <input type="password" name="j_password" required><br>
            
        <button type="submit">Login</button>
    </form>
</section>
<%@include file="footer.jsp" %>