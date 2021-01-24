package webproject.filmreview.Resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.internal.util.Base64;
import webproject.filmreview.Models.ErrorMessage;
import webproject.filmreview.Models.Genres;
import webproject.filmreview.Models.HttpDateModel;
import webproject.filmreview.Models.Movie;
import webproject.filmreview.Models.WatchesMovie;
import webproject.filmreview.Utilities.WatchTrackerService;

@Path("/secured/movies")
public class WatchTrackerMovieResource {
    private WatchTrackerService watchService = new WatchTrackerService();
    private static final String AUTHORIZATION_HEADER_KEY = "Authorization";
    private static final String AUTHORIZATION_HEADER_PREFIX = "Basic ";

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Movie> getWatchingMovies(@QueryParam("contains") String contains, @QueryParam("rating") float rating, 
    @QueryParam("genre") String genre, @Context HttpHeaders headers) 
    {
        long userId = getUserIdFromHeader(headers);
        List<WatchesMovie> movies = watchService.getWatchingMovies(userId);
        if (movies.equals(null)) 
        {
            ErrorMessage msg = new ErrorMessage("Not found", 404, "Error finding the movies list.");
            Response response = Response.status(Status.NOT_FOUND).entity(msg).build();
            throw new NotFoundException(response);
        }
        List<Movie> moviesList = new ArrayList<>();
        if(movies.size() == 0)
        {
            return moviesList;
        }
        if((contains == null) && (rating == 0.0f) && (genre == null))
        {
            for(WatchesMovie m : movies)
            {
                Map<Long, Movie> map = watchService.getMoviesMap();
                Movie mov = map.get(m.getMovieId());
                moviesList.add(mov);
            }
            return moviesList;
        }
        else if(!(contains == null) && (rating == 0.0f) && (genre == null))
        {
            for(WatchesMovie m : movies)
            {
                Map<Long, Movie> map = watchService.getMoviesMap();
                Movie mov = map.get(m.getMovieId());
                if(mov.getName().contains(contains))
                {
                    moviesList.add(mov);
                }
            }
            return moviesList;
        }
        else if((contains == null) && (rating != 0.0f) && (genre == null))
        {
            for(WatchesMovie m : movies)
            {
                Map<Long, Movie> map = watchService.getMoviesMap();
                Movie mov = map.get(m.getMovieId());
                if(mov.getRating() >= rating)
                {
                    moviesList.add(mov);
                }
            }
            return moviesList;
        }
        else if((contains == null) && (rating == 0.0f) && !(genre == null))
        {
            for(WatchesMovie m : movies)
            {
                Map<Long, Movie> map = watchService.getMoviesMap();
                Movie mov = map.get(m.getMovieId());
                List<Genres> gnrs = mov.getGenres();
                for(Genres g : gnrs)
                {
                    if(genre.equalsIgnoreCase(g.name()))
                    {
                        moviesList.add(mov);
                        break;
                    }
                } 
            }
            return moviesList;
        }
        else if(!(contains == null) && (rating == 0.0f) && !(genre == null))
        {
            for(WatchesMovie m : movies)
            {
                Map<Long, Movie> map = watchService.getMoviesMap();
                Movie mov = map.get(m.getMovieId());
                if(mov.getName().contains(contains))
                {
                    List<Genres> gnrs = mov.getGenres();
                    for(Genres g : gnrs)
                    {
                        if(genre.equalsIgnoreCase(g.name()))
                        {
                            moviesList.add(mov);
                            break;
                        }
                    }
                }
            }
            return moviesList;
        }
        else if((contains == null) && (rating != 0.0f) && !(genre == null))
        {
            for(WatchesMovie m : movies)
            {
                Map<Long, Movie> map = watchService.getMoviesMap();
                Movie mov = map.get(m.getMovieId());
                if(mov.getRating() >= rating)
                {
                    List<Genres> gnrs = mov.getGenres();
                    for(Genres g : gnrs)
                    {
                        if(genre.equalsIgnoreCase(g.name()))
                        {
                            moviesList.add(mov);
                            break;
                        }
                    }
                }
            }
            return moviesList;
        }
        else if(!(contains == null) && (rating != 0.0f) && (genre == null))
        {
            for(WatchesMovie m : movies)
            {
                Map<Long, Movie> map = watchService.getMoviesMap();
                Movie mov = map.get(m.getMovieId());
                if((mov.getRating() >= rating) && (mov.getName().contains(contains)))
                {
                   moviesList.add(mov);
                }
            }
            return moviesList;
        }
        else
        {
            for(WatchesMovie m : movies)
            {
                Map<Long, Movie> map = watchService.getMoviesMap();
                Movie mov = map.get(m.getMovieId());
                if((mov.getRating() >= rating) && (mov.getName().contains(contains)))
                {
                    List<Genres> gnrs = mov.getGenres();
                    for(Genres g : gnrs)
                    {
                        if(genre.equalsIgnoreCase(g.name()))
                        {
                            moviesList.add(mov);
                            break;
                        }
                    }
                }
            }
            return moviesList;
        }
    }

    @GET
    @Path("/finished")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Movie> getfinishedMovies(@Context HttpHeaders headers)
    {
        long userId = getUserIdFromHeader(headers);
        List<WatchesMovie> list = watchService.getFinishedWatchingMovies(userId);
        List<Movie> finishedMovies = new ArrayList<>();
        if(list.size() == 0)
        {
            return finishedMovies;
        }
        Map<Long, Movie> map = watchService.getMoviesMap();
        for(WatchesMovie m : list)
        {
            Movie mov = map.get(m.getMovieId());
            finishedMovies.add(mov);
        }
        return finishedMovies;
    }

    @GET
    @Path("/finished/{movieId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Movie getfinishedMovieById(@PathParam("movieId") long movieId, @Context HttpHeaders headers)
    {
        long userId = getUserIdFromHeader(headers);
        WatchesMovie m = watchService.getFinishedMovieById(userId, movieId);
        if(m == null)
        {
            ErrorMessage msg = new ErrorMessage("Not found", 404, "You have either provided a wrong movie ID or" 
            + " the movie with ID=" + movieId + " is not on your watch list OR it's not set as finished.");
            Response response = Response.status(Status.NOT_FOUND).entity(msg).build();
            throw new NotFoundException(response);
        }
        else
        {
            Map<Long, Movie> map = watchService.getMoviesMap();
            Movie mov = map.get(movieId);
            return mov;
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{movieId}")
    public Movie getWatchingMovieById(@PathParam("movieId") long movieId, @Context HttpHeaders headers) {
        long userId = getUserIdFromHeader(headers);
        WatchesMovie movie = watchService.getWatchingMovieById(userId, movieId);
        if(movie == null) 
        {
            ErrorMessage msg = new ErrorMessage("Not found", 404, "You have either provided a wrong movie ID or" 
            + " the movie with ID=" + movieId + " is not on your watch list");
            Response response = Response.status(Status.NOT_FOUND).entity(msg).build();
            throw new NotFoundException(response);
        }
        else 
        {
            Map<Long, Movie> map = watchService.getMoviesMap();
            Movie realMovie = map.get(movie.getMovieId());
            return realMovie;
        }
    }

    @POST
    @Path("/{movieId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addMovieToWatch(@PathParam("movieId") long movieId, @Context HttpHeaders headers) {
        long userId = getUserIdFromHeader(headers);
        boolean resp = watchService.addMovieToWatchList(userId, movieId);
        if (resp == false) {
            ErrorMessage msg = new ErrorMessage("Not acceptable", 406, "Error adding the movie to your watch list."
            + " Either the movie with the provided ID=" + movieId + " does not exist or this movie is already on your watch list.");
            Response response = Response.status(Status.NOT_ACCEPTABLE).entity(msg).build();
            throw new NotAcceptableException(response);
        } 
        else 
        {
            return Response.accepted().entity("Movie with id=" + movieId + " has been added to your watch list.")
                    .build();
        }
    }

    @PUT
    @Path("/{movieId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setMovieAsFinished(HttpDateModel model, @PathParam("movieId") long movieId, @Context HttpHeaders headers)
    {
        long userId = getUserIdFromHeader(headers);
        if (model.equals(null)) {
            ErrorMessage msg = new ErrorMessage("Not acceptable", 406, "Please provide a finish date.");
            Response response = Response.status(Status.NOT_ACCEPTABLE).entity(msg).build();
            throw new NotAcceptableException(response);
        }
        String inputDate = model.getFinishDate();
        boolean valid = checkDateFormat(inputDate);
        if(valid)
        {
            Boolean resp = watchService.setMovieAsFinished(userId, movieId, model.getFinishDate());
            if(resp)
            {
                WatchesMovie mov = watchService.getWatchingMovieById(userId, movieId);
                return Response.accepted().entity(mov).build();
            }
            else
            {
                ErrorMessage msg = new ErrorMessage("Not found", 404, "Error while setting the movie as finished, either the"
                + " movie with the provided ID=" + movieId + " does not exist, or it's not on your watch list, or the date"
                + " you have provided is before the movie's release date.");
                Response response = Response.status(Status.NOT_FOUND).entity(msg).build();
                throw new NotFoundException(response);
            }
        }
        else
        {
           throw new DateFormatException("Bad Request - Error with date input");
        }
    }

    @DELETE
    @Path("/{movieId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response removeMovieFromWatchList(@PathParam("movieId") long movieId, @Context HttpHeaders headers)
    {
        long userId = getUserIdFromHeader(headers);
        boolean resp = watchService.deleteMovieFromWatchList(userId, movieId);
        if(resp)
        {
            return Response.accepted().entity("Movie with id=" + movieId + " has been removed from your watch list.").build();
        }
        else
        {
            ErrorMessage msg = new ErrorMessage("Not found", 404, "Movie with ID=" + movieId + " has not been found.");
            Response response = Response.status(Status.NOT_FOUND).entity(msg).build();
            throw new NotFoundException(response);
        }
    }

    public long getUserIdFromHeader(HttpHeaders headers)
    {
        List<String> authHeader = headers.getRequestHeaders().get(AUTHORIZATION_HEADER_KEY);
        String authToken = authHeader.get(0);
        authToken = authToken.replace(AUTHORIZATION_HEADER_PREFIX, "");
        String decoded = Base64.decodeAsString(authToken);
        StringTokenizer tokenizer = new StringTokenizer(decoded, ":");
        String userId = tokenizer.nextToken();
        long id = Long.valueOf(userId);
        return id;
    }

    public boolean checkDateFormat(String date)
    {
        String[] comps = date.split("/");
        if(comps.length != 3)
        {
            return false;
        }
        if((comps[0].isEmpty()) || (comps[1].isEmpty()) || (comps[2].isEmpty()))
        {
            return false;
        }
        if((comps[0].length() != 2) || (comps[1].length() != 2) || (comps[2].length() != 4))
        {
            return false;
        }
        String day = comps[0];
        char firstOfDay = day.charAt(0);
        if(firstOfDay == '0')
        {
            char secondOfDay = day.charAt(1);
            int secondDay = Character.getNumericValue(secondOfDay);
            if(secondDay<=0)
            {
                return false;
            }
            if(secondDay > 9)
            {
                return false;
            }
        }
        else
        {
            int dayInt = Integer.parseInt(day);
            if(dayInt < 10)
            {
                return false;
            }
            if(dayInt > 31)
            {
                return false;
            }
        }
        String month = comps[1];
        char firstOfMonth = month.charAt(0);
        if(firstOfMonth == '0')
        {
            char secondOfMonth = month.charAt(1);
            int secondMonth = Character.getNumericValue(secondOfMonth);
            if(secondMonth <=0)
            {
                return false;
            }
            if(secondMonth > 9)
            {
                return false;
            }
        }
        else
        {
            int monthInt = Integer.parseInt(month);
            if(monthInt < 0)
            {
                return false;
            }
            if(monthInt > 12)
            {
                return false;
            }
        }
        String year = comps[2];
        int yearInt = Integer.parseInt(year);
        if(yearInt < 1900)
        {
            return false;
        }
        if(yearInt > 2021)
        {
            return false;
        }
        return true;
    }

}