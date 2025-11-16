<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="header.jsp" %>
<section>
    <h1>Login Failed</h1>
    <p class="error">Invalid username or password. Please try again.</p>
    <a href="${pageContext.request.contextPath}/login.jsp">Back to Login</a>
</section>
<%@include file="footer.jsp" %>
