<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="header.jsp" %>
<section class="movie-section">    
    <div style="grid-column: 1 / -1; display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px;">
        <h3 style="margin: 0;">Movie Catalog</h3>
        <form action="${pageContext.request.contextPath}/edit" method="post" style="margin: 0;">
            <input type="hidden" name="action" value="add-movie">
            <button type="submit">Add Movie</button>
        </form>
    </div>
    
    <c:forEach var="movie" items="${movies}">
        <div class="movie-card">
            <h5>${movie.title}</h5>
            
            <p>Genre: ${movie.genre}</p>
            <p>Year: ${movie.year}</p>
            <p>Duration: ${movie.duration}</p>
            <p>Rating: ${movie.rating}</p>
            <p>Available copies: ${movie.availableCopies}</p>
            
            <form action="${pageContext.request.contextPath}/edit" method="post">
                <input type="hidden" name="action" value="remove-coppie">
                <input type="hidden" name="movieId" value="${movie.movieId}">
                <button type="submit"> - </button>
            </form>
            <form action="${pageContext.request.contextPath}/edit" method="post">
                <input type="hidden" name="action" value="add-coppie">
                <input type="hidden" name="movieId" value="${movie.movieId}">
                <button type="submit"> + </button>
            </form>
            <form action="${pageContext.request.contextPath}/edit" method="post">
                <input type="hidden" name="action" value="delete-movie">
                <input type="hidden" name="movieId" value="${movie.movieId}">
                <button type="submit">Delete movie</button>
            </form>
                
        </div>
    </c:forEach>
</section>
<%@include file="footer.jsp" %>