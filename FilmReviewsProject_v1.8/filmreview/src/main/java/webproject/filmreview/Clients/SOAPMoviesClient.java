package webproject.filmreview.Clients;

import java.util.Map;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.ws.rs.client.Client;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.internal.util.Base64;
import java.util.List;

import org.glassfish.jersey.client.ClientConfig;

import webproject.filmreview.Models.Movie;

import webproject.filmreview.Utilities.Database;

@WebService
public class SOAPMoviesClient {
    
    private static final String serviceURL = "http://localhost:8080/ws/moviesSOAP";
    public static final String tokence = "RBHfBIWq9ZLzVTVxVso730RhyfXIO3pjZ5JY0HG+HT8=";

    @WebMethod
    public String doGetAllMoviesSOAP()
    {
        
        Map<Long , Movie> moviesDictionary = Database.getAllMovies();
        
        String allMoviesString = "";

        if (moviesDictionary.size() == 0)
        {
            return "Database is empty!";
        }

        for (Map.Entry<Long, Movie> entry : moviesDictionary.entrySet()) {
            //Long mLong = entry.getKey();
            Movie mMovie = entry.getValue();
            allMoviesString.concat("Name: ");
            allMoviesString.concat(mMovie.getName());
            allMoviesString.concat("\n");
            allMoviesString.concat("Main actors: ");
            allMoviesString.concat(mMovie.getMainActors());
            allMoviesString.concat("\n");
            allMoviesString.concat("Rating: ");
            allMoviesString.concat(Float.toString(mMovie.getRating()));
            allMoviesString.concat("\n");
            allMoviesString.concat("Description: ");
            allMoviesString.concat(mMovie.getDescription());      
            allMoviesString.concat("\n");
       
        }

        return "Movies on database: \n" + allMoviesString;
    }


    @WebMethod
    public String doGetAMovieByNameSOAP(String nameOfRequestedMovie)
    {
        
        Map<Long , Movie> moviesDictionary = Database.getAllMovies();
        
        String allMoviesString = "";

        if (moviesDictionary.size() == 0)
        {
            return "Database is empty!";
        }

        for (Map.Entry<Long, Movie> entry : moviesDictionary.entrySet()) {
            Movie mMovie = entry.getValue();
            if (mMovie.getName().contains(nameOfRequestedMovie))
            {
                allMoviesString.concat("Name: ");
                allMoviesString.concat(mMovie.getName());
                allMoviesString.concat("\n");
                allMoviesString.concat("Main actors: ");
                allMoviesString.concat(mMovie.getMainActors());
                allMoviesString.concat("\n");
                allMoviesString.concat("Rating: ");
                allMoviesString.concat(Float.toString(mMovie.getRating()));
                allMoviesString.concat("\n");
                allMoviesString.concat("Description: ");
                allMoviesString.concat(mMovie.getDescription());      
                allMoviesString.concat("\n");
            }
        }

        if (allMoviesString.equals(""))
        {
            return "No movie containing " + nameOfRequestedMovie + " in its title/name";
        }
        else {
        return "Movies that contain" + nameOfRequestedMovie + " in their title: \n";
        }
    }


    @WebMethod
    public String doGetAMovieByIdSOAP(Long movieId)
    {
        
        Map<Long , Movie> moviesDictionary = Database.getAllMovies();
        
        String allMoviesString = "";

        if (moviesDictionary.size() == 0)
        {
            return "database is empty!";
        }

        for (Map.Entry<Long, Movie> entry : moviesDictionary.entrySet()) {
            Movie mMovie = entry.getValue();
            if (mMovie.getId() == movieId)
            {
                allMoviesString.concat("Name: ");
                allMoviesString.concat(mMovie.getName());
                allMoviesString.concat("\n");
                allMoviesString.concat("Main actors: ");
                allMoviesString.concat(mMovie.getMainActors());
                allMoviesString.concat("\n");
                allMoviesString.concat("Rating: ");
                allMoviesString.concat(Float.toString(mMovie.getRating()));
                allMoviesString.concat("\n");
                allMoviesString.concat("Description: ");
                allMoviesString.concat(mMovie.getDescription());      
                allMoviesString.concat("\n");
            }
        }
        if (allMoviesString.equals(""))
        {
            return "No such movie found on database";
        }
        else {
            return "Basic information aboit the movie requested by Movieid: \n " + allMoviesString;
        }
    }
 
    public String doGetAllMoviesSOAPSecured(String userId, String token)
    {
        String credentials = userId + ":" + token;
        String authHeaderValue = "Basic " + Base64.encodeAsString(credentials.getBytes());
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);
        WebTarget service = client.target(serviceURL).path("secured/movies");
        Invocation.Builder invocationBuilder = service.request(MediaType.APPLICATION_XML).accept(MediaType.APPLICATION_XML)
        .header(HttpHeaders.AUTHORIZATION, authHeaderValue);
        List<Movie> movies = invocationBuilder.get(new GenericType<List<Movie>> () {});

        String allMoviesString = "";

        for (Movie entry : movies) {
            allMoviesString.concat("Name: ");
            allMoviesString.concat(entry.getName());
            allMoviesString.concat("\n");
            allMoviesString.concat("Main actors: ");
            allMoviesString.concat(entry.getMainActors());
            allMoviesString.concat("\n");
            allMoviesString.concat("Rating: ");
            allMoviesString.concat(Float.toString(entry.getRating()));
            allMoviesString.concat("\n");
            allMoviesString.concat("Description: ");
            allMoviesString.concat(entry.getDescription());      
            allMoviesString.concat("\n");
       
        }
        return "Movies on database: " + allMoviesString;
    }

}
