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

import webproject.filmreview.Models.Genres;
import webproject.filmreview.Models.Movie;
import webproject.filmreview.Utilities.Database;

import java.io.StringReader;
import java.sql.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;


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

            System.out.println("Found some movie");
            //Long mLong = entry.getKey();
            Movie mMovie = entry.getValue();
            allMoviesString = allMoviesString + "Name:";
            allMoviesString = allMoviesString + mMovie.getName();
            allMoviesString = allMoviesString + "\n";
            allMoviesString = allMoviesString + "Main actors: ";
            allMoviesString = allMoviesString + mMovie.getMainActors();
            allMoviesString = allMoviesString + "\n";
            allMoviesString = allMoviesString + "Rating: ";
            allMoviesString = allMoviesString + Float.toString(mMovie.getRating());
            allMoviesString = allMoviesString + "\n";
            allMoviesString = allMoviesString + "Description: ";
            allMoviesString = allMoviesString + mMovie.getDescription();      
            allMoviesString = allMoviesString + "\n";
            System.out.println(mMovie.getName());
       
        }

        return allMoviesString;
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
                allMoviesString = allMoviesString + "Name:";
                allMoviesString = allMoviesString + mMovie.getName();
                allMoviesString = allMoviesString + "\n";
                allMoviesString = allMoviesString + "Main actors: ";
                allMoviesString = allMoviesString + mMovie.getMainActors();
                allMoviesString = allMoviesString + "\n";
                allMoviesString = allMoviesString + "Rating: ";
                allMoviesString = allMoviesString + Float.toString(mMovie.getRating());
                allMoviesString = allMoviesString + "\n";
                allMoviesString = allMoviesString + "Description: ";
                allMoviesString = allMoviesString + mMovie.getDescription();      
                allMoviesString = allMoviesString + "\n";
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
            allMoviesString = allMoviesString + "Name:";
            allMoviesString = allMoviesString + mMovie.getName();
            allMoviesString = allMoviesString + "\n";
            allMoviesString = allMoviesString + "Main actors: ";
            allMoviesString = allMoviesString + mMovie.getMainActors();
            allMoviesString = allMoviesString + "\n";
            allMoviesString = allMoviesString + "Rating: ";
            allMoviesString = allMoviesString + Float.toString(mMovie.getRating());
            allMoviesString = allMoviesString + "\n";
            allMoviesString = allMoviesString + "Description: ";
            allMoviesString = allMoviesString + mMovie.getDescription();      
            allMoviesString = allMoviesString + "\n";
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


        Document doc = convertStringToXMLDocument(allMoviesString);

        System.out.println(doc.getFirstChild().getNodeName());

        return  allMoviesString;
    }

    @WebMethod
    public String addMovieToDatabase(Long movieId, List<Genres> movieGenre, String movieName, String movieActors, Float rating, String movieDesc)
    {
        Movie movieToAdd = new Movie();
        String infoString = "";
        if (movieGenre.size() > 0)
        {
            movieToAdd.setGenres(movieGenre);
        }else {infoString = infoString + "No Genres set \n";}
        if (!movieName.equals(""))
        {
            movieToAdd.setName(movieName);
        }else {infoString = infoString + "No name given! \n";return infoString;}
        if (!movieActors.equals(""))
        {
            movieToAdd.setMainActors(movieActors);
        }else {infoString = infoString + "No main actors added \n";} 
        if (rating != null)
        {
            movieToAdd.setRating(rating);
        }else {infoString = infoString + "No rating set \n";}
        if (!movieDesc.equals(""))
        {
            movieToAdd.setDescription(movieDesc);
        }else {infoString = infoString + "no description added v";}
        try {
            Database.addMovie(movieId, movieToAdd);
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
            return e.getLocalizedMessage();
        }
        if (infoString.equals(""))
        {
            return "movie added to database";
        }else {
            return "movie added, but with problems: " + infoString;
        }   
    }



    private static Document convertStringToXMLDocument(String xmlString) 
    {
        //Parser that produces DOM object trees from XML content
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         
        //API to obtain DOM Document instance
        DocumentBuilder builder = null;
        try
        {
            //Create DocumentBuilder with default configuration
            builder = factory.newDocumentBuilder();
             
            //Parse the content to Document object
            Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
            return doc;
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return null;
    }



}
