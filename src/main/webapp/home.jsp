<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="header.jsp" %>
<section class="movie-section">
    <h3>Currently Rented</h3>
    <c:forEach var="rental" items="${curRentals}">
        <div class="movie-card">
            <h5>${rental.movie.title}</h5>
            <h5>Due date: ${rental.dueDate}</h5>
            <p>Date rented: ${rental.rentalDate}</p>
            <p>Genre: ${rental.movie.genre}</p>
            <p>Year: ${rental.movie.year}</p>
            <p>Duration: ${rental.movie.duration}</p>
            <p>Rating: ${rental.movie.rating}</p>
            <p>Available copies: ${rental.movie.availableCopies}</p>
            <button type="button">Return Movie</button>
        </div>
    </c:forEach>
</section> 
<section class="movie-section">
    <h3>Rental History</h3>
    <c:forEach var="rental" items="${pastRentals}">
        <div class="movie-card">
            <h5>${rental.movie.title}</h5>
            <h5>Date: ${rental.rentalDate} - ${rental.dateReturned}</h5>
            <p>Genre: ${rental.movie.genre}</p>
            <p>Year: ${rental.movie.year}</p>
            <p>Duration: ${rental.movie.duration}</p>
            <p>Rating: ${rental.movie.rating}</p>
            <p>Available copies: ${rental.movie.availableCopies}</p>
            
            <c:if test="${rental.movie.availableCopies > 0}">
                <button type="button">Rent</button>
            </c:if>
        </div>
    </c:forEach>
</section> 
<section class="movie-section">    
    <h3>Recommendations</h3>
    <c:forEach var="recommendation" items="${recommendations}">
        <div class="movie-card">
            <h5>${recommendation.movie.title}</h5>
            <h5>${recommendation.reason}</h5>
            <p>Genre: ${recommendation.movie.genre}</p>
            <p>Year: ${recommendation.movie.year}</p>
            <p>Duration: ${recommendation.movie.duration}</p>
            <p>Rating: ${recommendation.movie.rating}</p>
            <p>Available copies: ${recommendation.movie.availableCopies}</p>
            <c:if test="${recommendation.movie.availableCopies > 0}">
                <button type="button">Rent</button>
            </c:if>
        </div>
    </c:forEach>
</section> 
<section>    
    <h3>Movie Catalog</h3>
    <c:forEach var="movie" items="${movies}">
        <div class="movie-card">
            <h5>${movie.title}</h5>
            <p>Genre: ${movie.genre}</p>
            <p>Year: ${movie.year}</p>
            <p>Duration: ${movie.duration}</p>
            <p>Rating: ${movie.rating}</p>
            <p>Available copies: ${movie.availableCopies}</p>
            <c:if test="${movie.availableCopies > 0}">
                <button type="button">Rent</button>
            </c:if>
        </div>
    </c:forEach>
</section>
<%@include file="footer.jsp" %>