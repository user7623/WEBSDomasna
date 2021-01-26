package webproject.filmreview.Utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import webproject.filmreview.Models.Movie;

public class MovieService 
{
    private Map<Long, Movie> movies = Database.getAllMovies();

    public MovieService()
    {

    }

    public List<Movie> getAllMovies()
    {
        return new ArrayList<Movie>(movies.values());
    }

    public Movie getMovieById(long movieId)
    {
        return movies.get(movieId);
    }

    public Movie addMovie(Movie movie)
    {
        movie.setId(movies.size()+1);
        movies.put(movie.getId(), movie);
        return movie;
    }

    public Movie updateMovie(Movie movie)
    {
        if(movie.getId()<=0)
        {
            return null;
        }
        movies.put(movie.getId(), movie);
        return movie;
    }

    public void deleteMovie(long movieId)
    {
        movies.remove(movieId);
    }

}