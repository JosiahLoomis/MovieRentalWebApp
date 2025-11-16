<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="header.jsp" %>
<section>
    <h1>Add New Movie</h1>
    
    <form action="${pageContext.request.contextPath}/edit/add" method="post">
        
        <label>Title:</label>
        <input type="text" name="title" required><br>
        
        <label>Genre:</label>
        <input type="text" name="genre" required><br>
        
        <label>Release Year:</label>
        <input type="number" name="releaseYear" required><br>
        
        <label>Duration In Minutes:</label>
        <input type="number" name="duration" required><br>
        
        <label>Rating:</label>
        <select name="rating" required>
            <option value="">-- Select Rating --</option>
            <option value="G">G</option>
            <option value="PG">PG</option>
            <option value="PG-13">PG-13</option>
            <option value="R">R</option>
            <option value="NA">NA</option>
        </select><br>
        
        <label>Number of Copies:</label>
        <input type="number" name="copies" min="1" required><br>
        
        <button type="submit">Add Movie</button>
        <a href="${pageContext.request.contextPath}/edit">Cancel</a>
    </form>
</section>
<%@include file="footer.jsp" %>