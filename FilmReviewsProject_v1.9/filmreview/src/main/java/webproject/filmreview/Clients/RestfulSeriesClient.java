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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.internal.util.Base64;

import webproject.filmreview.Models.Episode;
import webproject.filmreview.Models.ErrorMessage;
import webproject.filmreview.Models.HttpDateModel;
import webproject.filmreview.Models.RegisterModel;
import webproject.filmreview.Models.ResponseModel;
import webproject.filmreview.Models.Season;
import webproject.filmreview.Models.Series;

public class RestfulSeriesClient 
{
    private static final String serviceURL = "http://localhost:8080/filmreview/webapi";
    public static final String tokence = "RBHfBIWq9ZLzVTVxVso730RhyfXIO3pjZ5JY0HG+HT8=";

    public RestfulSeriesClient()
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

    public ResponseModel doGetSeriesById(long seriesId, String userId, String token)
    {
        String credentials = userId + ":" + token;
        String authHeaderValue = "Basic " + Base64.encodeAsString(credentials.getBytes());
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);
        WebTarget service = client.target(serviceURL).path("secured/series/" + String.valueOf(seriesId));
        Invocation.Builder invocationBuilder = service.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.AUTHORIZATION, authHeaderValue);
        Series response = invocationBuilder.get(Series.class);
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

    public ResponseModel doAddSeriesToWatchList(long seriesId, String userId, String token)
    {
        String credentials = userId + ":" + token;
        String authHeaderValue = "Basic " + Base64.encodeAsString(credentials.getBytes());
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);
        WebTarget service = client.target(serviceURL).path("secured/series/" + String.valueOf(seriesId));
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

    public ResponseModel doSetSeriesAsFinished(long seriesId, String userId, String token, HttpDateModel model)
    {
        String credentials = userId + ":" + token;
        String authHeaderValue = "Basic " + Base64.encodeAsString(credentials.getBytes());
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);
        WebTarget service = client.target(serviceURL).path("secured/series/" + String.valueOf(seriesId));
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

    public ResponseModel doDeleteSeries(long seriesId, String userId, String token)
    {
        String credentials = userId + ":" + token;
        String authHeaderValue = "Basic " + Base64.encodeAsString(credentials.getBytes());
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);
        WebTarget service = client.target(serviceURL).path("secured/series/" + String.valueOf(seriesId));
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

    public List<Series> doGetAllSeries(String userId, String token)
    {
        String credentials = userId + ":" + token;
        String authHeaderValue = "Basic " + Base64.encodeAsString(credentials.getBytes());
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);
        WebTarget service = client.target(serviceURL).path("secured/series");
        Invocation.Builder invocationBuilder = service.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.AUTHORIZATION, authHeaderValue);
        List<Series> series = invocationBuilder.get(new GenericType<List<Series>> () {});
        return series;
    }

    public List<Series> doGetAllFinishedSeries(String userId, String token)
    {
        String credentials = userId + ":" + token;
        String authHeaderValue = "Basic " + Base64.encodeAsString(credentials.getBytes());
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);
        WebTarget service = client.target(serviceURL).path("secured/series/finished");
        Invocation.Builder invocationBuilder = service.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.AUTHORIZATION, authHeaderValue);
        List<Series> series = invocationBuilder.get(new GenericType<List<Series>> () {});
        return series;
    }

    public ResponseModel doGetSeasonById(long seriesId, long seasonId, String userId, String token)
    {
        String credentials = userId + ":" + token;
        String authHeaderValue = "Basic " + Base64.encodeAsString(credentials.getBytes());
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);
        WebTarget service = client.target(serviceURL).path("secured/series/" + String.valueOf(seriesId) + "/" + 
        String.valueOf(seasonId));
        Invocation.Builder invocationBuilder = service.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.AUTHORIZATION, authHeaderValue);
        Season response = invocationBuilder.get(Season.class);
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

    public ResponseModel doAddSeasonToWatchList(long seriesId, long seasonId, String userId, String token)
    {
        String credentials = userId + ":" + token;
        String authHeaderValue = "Basic " + Base64.encodeAsString(credentials.getBytes());
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);
        WebTarget service = client.target(serviceURL).path("secured/series/" + String.valueOf(seriesId) + "/" + 
        String.valueOf(seasonId));
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

    public ResponseModel doSetSeasonAsFinished(long seriesId, long seasonId, String userId, String token, HttpDateModel model)
    {
        String credentials = userId + ":" + token;
        String authHeaderValue = "Basic " + Base64.encodeAsString(credentials.getBytes());
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);
        WebTarget service = client.target(serviceURL).path("secured/series/" + String.valueOf(seriesId) + "/" + 
        String.valueOf(seasonId));
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

    public ResponseModel doDeleteSeason(long seriesId, long seasonId, String userId, String token)
    {
        String credentials = userId + ":" + token;
        String authHeaderValue = "Basic " + Base64.encodeAsString(credentials.getBytes());
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);
        WebTarget service = client.target(serviceURL).path("secured/series/" + String.valueOf(seriesId) + "/" + 
        String.valueOf(seasonId));
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

    public List<Season> doGetAllFinishedSeasonsFromSeries(long seriesId, String userId, String token)
    {
        String credentials = userId + ":" + token;
        String authHeaderValue = "Basic " + Base64.encodeAsString(credentials.getBytes());
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);
        WebTarget service = client.target(serviceURL).path("secured/series/finished/" + String.valueOf(seriesId));
        Invocation.Builder invocationBuilder = service.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.AUTHORIZATION, authHeaderValue);
        List<Season> seasons = invocationBuilder.get(new GenericType<List<Season>> () {});
        return seasons;
    }

    public ResponseModel doAddEpisodeToWatchList(long seriesId, long seasonId, long episodeId, String userId, String token)
    {
        String credentials = userId + ":" + token;
        String authHeaderValue = "Basic " + Base64.encodeAsString(credentials.getBytes());
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);
        WebTarget service = client.target(serviceURL).path("secured/series/" + String.valueOf(seriesId) + "/" + 
        String.valueOf(seasonId) + "/" + String.valueOf(episodeId));
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

    public ResponseModel doGetEpisodeById(long seriesId, long seasonId, long episodeId, String userId, String token)
    {
        String credentials = userId + ":" + token;
        String authHeaderValue = "Basic " + Base64.encodeAsString(credentials.getBytes());
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);
        WebTarget service = client.target(serviceURL).path("secured/series/" + String.valueOf(seriesId) + "/" + 
        String.valueOf(seasonId) + "/" + String.valueOf(episodeId));
        Invocation.Builder invocationBuilder = service.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.AUTHORIZATION, authHeaderValue);
        Episode response = invocationBuilder.get(Episode.class);
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

    public ResponseModel doSetEpisodeAsFinished(long seriesId, long seasonId, long episodeId, String userId,
    String token, HttpDateModel model)
    {
        String credentials = userId + ":" + token;
        String authHeaderValue = "Basic " + Base64.encodeAsString(credentials.getBytes());
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);
        WebTarget service = client.target(serviceURL).path("secured/series/" + String.valueOf(seriesId) + "/" + 
        String.valueOf(seasonId) + "/" + String.valueOf(episodeId));
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

    public ResponseModel doDeleteEpisode(long seriesId, long seasonId, long episodeId, String userId, String token)
    {
        String credentials = userId + ":" + token;
        String authHeaderValue = "Basic " + Base64.encodeAsString(credentials.getBytes());
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);
        WebTarget service = client.target(serviceURL).path("secured/series/" + String.valueOf(seriesId) + "/" + 
        String.valueOf(seasonId) + "/" + String.valueOf(episodeId));
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

    public List<Episode> doGetAllFinishedEpisodesFromSeason(long seriesId, long seasonId, String userId, String token)
    {
        String credentials = userId + ":" + token;
        String authHeaderValue = "Basic " + Base64.encodeAsString(credentials.getBytes());
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);
        WebTarget service = client.target(serviceURL).path("secured/series/finished/" + String.valueOf(seriesId)
        + "/" + String.valueOf(seasonId));
        Invocation.Builder invocationBuilder = service.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.AUTHORIZATION, authHeaderValue);
        List<Episode> episodes = invocationBuilder.get(new GenericType<List<Episode>> () {});
        return episodes;
    }

    public ResponseModel doGetLastWatchedEpisode(long seriesId, String userId, String token)
    {
        String credentials = userId + ":" + token;
        String authHeaderValue = "Basic " + Base64.encodeAsString(credentials.getBytes());
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);
        WebTarget service = client.target(serviceURL).path("secured/series/" + String.valueOf(seriesId) + "/lastwatchedbyfinishdate");
        Invocation.Builder invocationBuilder = service.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.AUTHORIZATION, authHeaderValue);
        Episode response = invocationBuilder.get(Episode.class);
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

    public static void main(String[] args) 
    {
        boolean flag1 = true;
        boolean flag2 = true;
        RestfulSeriesClient c = new RestfulSeriesClient();
        ResponseModel mod1 = c.doAddSeriesToWatchList(Long.valueOf(1), "1", RestfulSeriesClient.tokence);
        if(mod1.getStatusCode() == 202)
        {
            System.out.println(mod1.getResponseMessage());
            ResponseModel mod2 = c.doAddSeasonToWatchList(Long.valueOf(1), Long.valueOf(1), "1", 
            RestfulSeriesClient.tokence);
            if(mod2.getStatusCode() == 202)
            {
                for(int i=1;i<=5;i++)
                {
                    ResponseModel mod = c.doAddEpisodeToWatchList(Long.valueOf(1), Long.valueOf(1), 
                    Long.valueOf(i), "1", RestfulSeriesClient.tokence);
                    if(mod.getStatusCode() != 202)
                    {
                        System.out.println(mod.getResponseMessage());
                        System.out.println("Stopping the client process.");
                        flag1 = false;
                        break;
                    }
                    System.out.println(mod.getResponseMessage());
                }
                if(flag1)
                {
                    for(int i=1;i<=5;i++)
                    {
                        String fDate = "0" + i + "/10/2019";
                        HttpDateModel date = new HttpDateModel(fDate);
                        ResponseModel modce = c.doSetEpisodeAsFinished(Long.valueOf(1), Long.valueOf(1), 
                        Long.valueOf(i), "1", RestfulSeriesClient.tokence, date);
                        if(modce.getStatusCode() != 202)
                        {
                            System.out.println(modce.getResponseMessage());
                            System.out.println("Stopping the client process.");
                            flag2 = false;
                            break;
                        }
                        System.out.println(modce.getResponseMessage());
                    }
                    if(flag2)
                    {
                        HttpDateModel dateS = new HttpDateModel("06/10/2019");
                        ResponseModel mod3 = c.doSetSeasonAsFinished(Long.valueOf(1), Long.valueOf(1), 
                        "1", RestfulSeriesClient.tokence, dateS);
                        if(mod3.getStatusCode() == 202)
                        {
                            System.out.println(mod3.getResponseMessage());
                            HttpDateModel dateS2 = new HttpDateModel("07/10/2019");
                            ResponseModel mod4 = c.doSetSeriesAsFinished(Long.valueOf(1), "1", RestfulSeriesClient.tokence,
                            dateS2);
                            if(mod4.getStatusCode() == 202)
                            {
                                System.out.println(mod4.getResponseMessage());
                                List<Series> sList = c.doGetAllFinishedSeries("1", RestfulSeriesClient.tokence);
                                for(Series s : sList)
                                {
                                    ObjectMapper mapper = new ObjectMapper();
                                    try
                                    {
                                        String json = mapper.writeValueAsString(s);
                                        System.out.println("Series " + s.getId() + ": " + json);
                                    }
                                    catch(JsonProcessingException e)
                                    {
                                        e.printStackTrace();
                                    }
                                }
                                ResponseModel mod5 = c.doGetLastWatchedEpisode(Long.valueOf(1), "1", 
                                RestfulSeriesClient.tokence);
                                System.out.println(mod5.getResponseMessage());
                                ResponseModel mod6 = c.doDeleteSeries(Long.valueOf(1), "1", RestfulSeriesClient.tokence);
                                System.out.println(mod6.getResponseMessage());
                            }
                            else
                            {
                                System.out.println(mod4.getResponseMessage());
                            }
                        }
                        else
                        {
                            System.out.println(mod3.getResponseMessage());
                        }
                    }
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