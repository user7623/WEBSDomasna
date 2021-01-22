package webproject.filmreview.Clients;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.internal.util.Base64;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.glassfish.jersey.client.ClientConfig;

import webproject.filmreview.Models.ErrorMessage;
import webproject.filmreview.Models.HttpDateModel;
import webproject.filmreview.Models.Movie;
import webproject.filmreview.Models.RegisterModel;
import webproject.filmreview.Models.ResponseModel;

public class RestfulMovieClient 
{
    private static final String serviceURL = "http://localhost:8080/filmreview/webapi";
    public static final String tokence = "RBHfBIWq9ZLzVTVxVso730RhyfXIO3pjZ5JY0HG+HT8=";

    public RestfulMovieClient()
    { }

    public boolean doRegisterUser(String username, String password)
    {
        RegisterModel model = new RegisterModel(username, password);
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);
        WebTarget service = client.target(serviceURL).path("register");
        Invocation.Builder invocationBuilder = service.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.entity(model, MediaType.APPLICATION_JSON));
        if(response.getStatus() == 201)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public String doLoginUser(String username, String password)
    {
        RegisterModel model = new RegisterModel(username, password);
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);
        WebTarget service = client.target(serviceURL).path("login");
        Invocation.Builder invocationBuilder = service.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.entity(model, MediaType.APPLICATION_JSON));
        if(response.getStatus() == 202)
        {
            String result = response.readEntity(String.class);
            return result;
        }
        else
        {
            return null;
        }
    }

    public String doLogoutUser(String userId, String token, String username, String password)
    {
        String credentials = userId + ":" + token;
        String authHeaderValue = "Basic " + Base64.encodeAsString(credentials.getBytes());
        RegisterModel model = new RegisterModel(username, password);
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);
        WebTarget service = client.target(serviceURL).path("secured/logout");
        Invocation.Builder invocationBuilder = service.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.AUTHORIZATION, authHeaderValue);
        Response response = invocationBuilder.post(Entity.entity(model, MediaType.APPLICATION_JSON));
        if(response.getStatus() == 200)
        {
            String result = response.readEntity(String.class);
            return result;
        }
        else
        {
            return null;
        }
    }

    public ResponseModel doAddMovieToWatchList(long movieId, String userId, String token)
    {
        String credentials = userId + ":" + token;
        String authHeaderValue = "Basic " + Base64.encodeAsString(credentials.getBytes());
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);
        WebTarget service = client.target(serviceURL).path("secured/movies/" + String.valueOf(movieId));
        Invocation.Builder invocationBuilder = service.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.AUTHORIZATION, authHeaderValue);
        RegisterModel model = new RegisterModel("zoran", "123");
        Response response = invocationBuilder.post(Entity.entity(model, MediaType.APPLICATION_JSON));
        if(response.getStatus() != 202)
        {
            ErrorMessage msg = response.readEntity(ErrorMessage.class);  
            ResponseModel mod = new ResponseModel(response.getStatus(), msg.getDocumentation());
            return mod;
        }
        else
        {
            ResponseModel modelce = new ResponseModel(response.getStatus(), response.readEntity(String.class));
            return modelce;
        }
    }

    public ResponseModel doSetMovieAsFinished(long movieId, String userId, String token, HttpDateModel model)
    {
        String credentials = userId + ":" + token;
        String authHeaderValue = "Basic " + Base64.encodeAsString(credentials.getBytes());
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);
        WebTarget service = client.target(serviceURL).path("secured/movies/" + String.valueOf(movieId));
        Invocation.Builder invocationBuilder = service.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.AUTHORIZATION, authHeaderValue);
        Response response = invocationBuilder.put(Entity.entity(model, MediaType.APPLICATION_JSON));
        if(response.getStatus() == 202)
        {
            ResponseModel modelce = new ResponseModel(response.getStatus(), response.readEntity(String.class));
            return modelce;
        }
        else
        {
            ErrorMessage msg = response.readEntity(ErrorMessage.class);  
            ResponseModel mod = new ResponseModel(response.getStatus(), msg.getDocumentation());
            return mod;
        }
    }

    public ResponseModel doDeleteMovie(long movieId, String userId, String token)
    {
        String credentials = userId + ":" + token;
        String authHeaderValue = "Basic " + Base64.encodeAsString(credentials.getBytes());
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);
        WebTarget service = client.target(serviceURL).path("secured/movies/" + String.valueOf(movieId));
        Invocation.Builder invocationBuilder = service.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.AUTHORIZATION, authHeaderValue);
        Response response = invocationBuilder.delete();
        if(response.getStatus() == 202)
        {
            ResponseModel modelce = new ResponseModel(response.getStatus(), response.readEntity(String.class));
            return modelce;
        }
        else
        {
            ErrorMessage msg = response.readEntity(ErrorMessage.class);  
            ResponseModel mod = new ResponseModel(response.getStatus(), msg.getDocumentation());
            return mod;
        }
    }

    public List<Movie> doGetAllMovies(String userId, String token)
    {
        String credentials = userId + ":" + token;
        String authHeaderValue = "Basic " + Base64.encodeAsString(credentials.getBytes());
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);
        WebTarget service = client.target(serviceURL).path("secured/movies");
        Invocation.Builder invocationBuilder = service.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.AUTHORIZATION, authHeaderValue);
        List<Movie> movies = invocationBuilder.get(new GenericType<List<Movie>> () {});
        return movies;
    }

    public ResponseModel doGetMovieById(long movieId, String userId, String token)
    {
        String credentials = userId + ":" + token;
        String authHeaderValue = "Basic " + Base64.encodeAsString(credentials.getBytes());
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);
        WebTarget service = client.target(serviceURL).path("secured/movies/" + String.valueOf(movieId));
        Invocation.Builder invocationBuilder = service.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.AUTHORIZATION, authHeaderValue);
        Movie response = invocationBuilder.get(Movie.class);
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            String json = mapper.writeValueAsString(response);
            ResponseModel mod = new ResponseModel(200, json);
            return mod;
        }
        catch (JsonProcessingException e) 
        {
            e.printStackTrace();
        }
        return null; 
    }

    public ResponseModel doGetFinishedMovieById(long movieId, String userId, String token)
    {
        String credentials = userId + ":" + token;
        String authHeaderValue = "Basic " + Base64.encodeAsString(credentials.getBytes());
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);
        WebTarget service = client.target(serviceURL).path("secured/movies/finished/" + String.valueOf(movieId));
        Invocation.Builder invocationBuilder = service.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.AUTHORIZATION, authHeaderValue);
        Movie response = invocationBuilder.get(Movie.class);
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            String json = mapper.writeValueAsString(response);
            ResponseModel mod = new ResponseModel(200, json);
            return mod;
        }
        catch (JsonProcessingException e) 
        {
            e.printStackTrace();
        }
        return null; 
    }

    public List<Movie> doGetAllFinishedMovies(String userId, String token)
    {
        String credentials = userId + ":" + token;
        String authHeaderValue = "Basic " + Base64.encodeAsString(credentials.getBytes());
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);
        WebTarget service = client.target(serviceURL).path("secured/movies/finished");
        Invocation.Builder invocationBuilder = service.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.AUTHORIZATION, authHeaderValue);
        List<Movie> movies = invocationBuilder.get(new GenericType<List<Movie>> () {});
        return movies;
    }

    public static void main(String[] args) 
    {
        RestfulMovieClient c = new RestfulMovieClient();
        ResponseModel mod1 = c.doAddMovieToWatchList(Long.valueOf(1), "1", RestfulMovieClient.tokence);
        if(mod1.getStatusCode() == 202)
        {
            System.out.println(mod1.getResponseMessage());
            ResponseModel mod2 = c.doGetMovieById(Long.valueOf(1), "1", RestfulMovieClient.tokence);
            if(mod2.getStatusCode() == 200)
            {
                System.out.println(mod2.getResponseMessage());
                List<Movie> moviesList = c.doGetAllMovies("1", RestfulMovieClient.tokence);
                for(Movie m : moviesList)
                {
                    ObjectMapper mapper = new ObjectMapper();
                    try
                    {
                        String json = mapper.writeValueAsString(m);
                        System.out.println("Movie " + m.getId() + ": " + json);
                    }
                    catch(JsonProcessingException e)
                    {
                        e.printStackTrace();
                    }
                }
                HttpDateModel model = new HttpDateModel("15/12/2019");
                ResponseModel mod3 = c.doSetMovieAsFinished(Long.valueOf(1), "1", RestfulMovieClient.tokence, model);
                if(mod3.getStatusCode() == 202)
                {
                    System.out.println(mod3.getResponseMessage());
                    ResponseModel mod5 = c.doGetFinishedMovieById(Long.valueOf(1), "1", RestfulMovieClient.tokence);
                    System.out.println(mod5.getResponseMessage());
                    ResponseModel mod6 = c.doDeleteMovie(Long.valueOf(1), "1", RestfulMovieClient.tokence);
                    if(mod6.getStatusCode() == 202)
                    {
                        System.out.println(mod6.getResponseMessage());
                    }
                    else
                    {
                        System.out.println(mod6.getResponseMessage());
                    }
                }
                else
                {
                    System.out.println(mod3.getResponseMessage());
                }
            }
            else
            {
                System.out.println(mod2.getResponseMessage());
            }
        }
        else
        {
            System.out.println(mod1.getResponseMessage());
        }
    }

}