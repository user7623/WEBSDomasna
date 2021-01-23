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

import webproject.filmreview.Models.Episode;
import webproject.filmreview.Models.ErrorMessage;
import webproject.filmreview.Models.Genres;
import webproject.filmreview.Models.HttpDateModel;
import webproject.filmreview.Models.Season;
import webproject.filmreview.Models.Series;
import webproject.filmreview.Models.WatchesEpisode;
import webproject.filmreview.Models.WatchesSeason;
import webproject.filmreview.Models.WatchesSeries;
import webproject.filmreview.Utilities.WatchTrackerService;

@Path("/secured/series")
public class WatchTrackerSeriesResource 
{
    private WatchTrackerService watchService = new WatchTrackerService();
    private static final String AUTHORIZATION_HEADER_KEY = "Authorization";
    private static final String AUTHORIZATION_HEADER_PREFIX = "Basic ";

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Series> getAllWatchingSeries(@QueryParam("contains") String contains, @QueryParam("rating") float rating, 
    @QueryParam("genre") String genre, @Context HttpHeaders headers)
    {
        long userId = getUserIdFromHeader(headers);
        List<WatchesSeries> theSeries = watchService.getWatchingSeries(userId);
        if(theSeries == null)
        {
            ErrorMessage msg = new ErrorMessage("Not found", 404, "Error, you have no series currently added to your watch list.");
            Response response = Response.status(Status.NOT_FOUND).entity(msg).build();
            throw new NotFoundException(response);
        }
        else if(theSeries.size() == 0)
        {
            List<Series> emptyList = new ArrayList<>();
            return emptyList;
        }
        else
        {
            if((contains == null) && (rating == 0.0f) && (genre == null))
            {
                List<Series> theList = new ArrayList<>();
                for(WatchesSeries s : theSeries)
                {
                    Map<Long, Series> theMap = watchService.getSeriesMap();
                    Series ss = theMap.get(Long.valueOf(s.getSeriesId()));
                    if(ss == null)
                    {
                        ErrorMessage msg = new ErrorMessage("Not found", 404, "Error series not found.");
                        Response response = Response.status(Status.NOT_FOUND).entity(msg).build();
                        throw new NotFoundException(response);
                    }
                    Series toAdd = fillSeriesWithWatchingItems(ss, s);
                    theList.add(toAdd);
                }
                return theList;
            }
            else if(!(contains == null) && (rating == 0.0f) && (genre == null))
            {
                List<Series> theList = new ArrayList<>();
                for(WatchesSeries s : theSeries)
                {
                    Map<Long, Series> theMap = watchService.getSeriesMap();
                    Series ss = theMap.get(Long.valueOf(s.getSeriesId()));
                    if(ss == null)
                    {
                        ErrorMessage msg = new ErrorMessage("Not found", 404, "Error series not found.");
                        Response response = Response.status(Status.NOT_FOUND).entity(msg).build();
                        throw new NotFoundException(response);
                    }
                    if(ss.getName().contains(contains))
                    {
                        Series toAdd = fillSeriesWithWatchingItems(ss, s);
                        theList.add(toAdd);
                    }
                }
                return theList;
            }
            else if((contains == null) && (rating != 0.0f) && (genre == null))
            {
                List<Series> theList = new ArrayList<>();
                for(WatchesSeries s : theSeries)
                {
                    Map<Long, Series> theMap = watchService.getSeriesMap();
                    Series ss = theMap.get(Long.valueOf(s.getSeriesId()));
                    if(ss == null)
                    {
                        ErrorMessage msg = new ErrorMessage("Not found", 404, "Error series not found.");
                        Response response = Response.status(Status.NOT_FOUND).entity(msg).build();
                        throw new NotFoundException(response);
                    }
                    if(ss.getRating() >= rating)
                    {
                        Series toAdd = fillSeriesWithWatchingItems(ss, s);
                        theList.add(toAdd);
                    }
                }
                return theList;
            }
            else if((contains == null) && (rating == 0.0f) && !(genre == null))
            {
                List<Series> theList = new ArrayList<>();
                for(WatchesSeries s : theSeries)
                {
                    Map<Long, Series> theMap = watchService.getSeriesMap();
                    Series ss = theMap.get(Long.valueOf(s.getSeriesId()));
                    if(ss == null)
                    {
                        ErrorMessage msg = new ErrorMessage("Not found", 404, "Error series not found.");
                        Response response = Response.status(Status.NOT_FOUND).entity(msg).build();
                        throw new NotFoundException(response);
                    }
                    List<Genres> gnrs = ss.getGenres();
                    for(Genres g : gnrs)
                    {
                        if(genre.equalsIgnoreCase(g.name()))
                        {
                            Series toAdd = fillSeriesWithWatchingItems(ss, s);
                            theList.add(toAdd);
                            break;
                        }
                    }
                }
                return theList;
            }
            else if((contains == null) && (rating != 0.0f) && !(genre == null))
            {
                List<Series> theList = new ArrayList<>();
                for(WatchesSeries s : theSeries)
                {
                    Map<Long, Series> theMap = watchService.getSeriesMap();
                    Series ss = theMap.get(Long.valueOf(s.getSeriesId()));
                    if(ss == null)
                    {
                        ErrorMessage msg = new ErrorMessage("Not found", 404, "Error series not found.");
                        Response response = Response.status(Status.NOT_FOUND).entity(msg).build();
                        throw new NotFoundException(response);
                    }
                    List<Genres> gnrs = ss.getGenres();
                    for(Genres g : gnrs)
                    {
                        if(genre.equalsIgnoreCase(g.name()))
                        {
                            if(ss.getRating() >= rating)
                            {
                                Series toAdd = fillSeriesWithWatchingItems(ss, s);
                                theList.add(toAdd);
                                break;
                            }
                        }
                    }
                }
                return theList;
            }
            else if(!(contains == null) && (rating == 0.0f) && !(genre == null))
            {
                List<Series> theList = new ArrayList<>();
                for(WatchesSeries s : theSeries)
                {
                    Map<Long, Series> theMap = watchService.getSeriesMap();
                    Series ss = theMap.get(Long.valueOf(s.getSeriesId()));
                    if(ss == null)
                    {
                        ErrorMessage msg = new ErrorMessage("Not found", 404, "Error series not found.");
                        Response response = Response.status(Status.NOT_FOUND).entity(msg).build();
                        throw new NotFoundException(response);
                    }
                    List<Genres> gnrs = ss.getGenres();
                    for(Genres g : gnrs)
                    {
                        if(genre.equalsIgnoreCase(g.name()))
                        {
                            if(ss.getName().contains(contains))
                            {
                                Series toAdd = fillSeriesWithWatchingItems(ss, s);
                                theList.add(toAdd);
                                break;
                            }
                        }
                    }
                }
                return theList;
            }
            else if(!(contains == null) && (rating != 0.0f) && (genre == null))
            {
                List<Series> theList = new ArrayList<>();
                for(WatchesSeries s : theSeries)
                {
                    Map<Long, Series> theMap = watchService.getSeriesMap();
                    Series ss = theMap.get(Long.valueOf(s.getSeriesId()));
                    if(ss == null)
                    {
                        ErrorMessage msg = new ErrorMessage("Not found", 404, "Error series not found.");
                        Response response = Response.status(Status.NOT_FOUND).entity(msg).build();
                        throw new NotFoundException(response);
                    }
                    if(ss.getRating() >= rating && ss.getName().contains(contains))
                    {
                        Series toAdd = fillSeriesWithWatchingItems(ss, s);
                        theList.add(toAdd);
                    }
                }
                return theList;
            }
            else
            {
                List<Series> theList = new ArrayList<>();
                for(WatchesSeries s : theSeries)
                {
                    Map<Long, Series> theMap = watchService.getSeriesMap();
                    Series ss = theMap.get(Long.valueOf(s.getSeriesId()));
                    if(ss == null)
                    {
                        ErrorMessage msg = new ErrorMessage("Not found", 404, "Error series not found.");
                        Response response = Response.status(Status.NOT_FOUND).entity(msg).build();
                        throw new NotFoundException(response);
                    }
                    List<Genres> gnrs = ss.getGenres();
                    for(Genres g : gnrs)
                    {
                        if(genre.equalsIgnoreCase(g.name()))
                        {
                            if(ss.getName().contains(contains) && ss.getRating() >= rating)
                            {
                                Series toAdd = fillSeriesWithWatchingItems(ss, s);
                                theList.add(toAdd);
                                break;
                            }
                        }
                    }
                }
                return theList;
            }
        }
    }

    @GET
    @Path("/finished")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Series> getAllFinishedSeries(@Context HttpHeaders headers)
    {
        long userId = getUserIdFromHeader(headers);
        List<WatchesSeries> sList = watchService.getFinishedSeries(userId);
        List<Series> returnList = new ArrayList<>();
        if(sList.size() == 0)
        {
            return returnList;
        }
        Map<Long, Series> theMap = watchService.getSeriesMap();
        for(WatchesSeries s : sList)
        {
            Series ss = theMap.get(s.getSeriesId());
            if(ss != null)
            {
                returnList.add(ss);
            }
        }
        return returnList;
    }

    @GET
    @Path("/finished/{seriesId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Season> getAllFinishedSeasons(@PathParam("seriesId") long seriesId, @Context HttpHeaders headers)
    {
        long userId = getUserIdFromHeader(headers);
        List<WatchesSeason> seasonsList = watchService.getFinishedSeasonsFromSeries(userId, seriesId);
        List<Season> retList = new ArrayList<>();
        if(seasonsList.size() == 0)
        {
            return retList;
        }
        Map<Long, Series> theMap = watchService.getSeriesMap();
        Series s = theMap.get(seriesId);
        if(s == null)
        {
            return retList;
        }
        List<Season> sList = s.getSeasons();
        for(WatchesSeason ss : seasonsList)
        {
            for(Season ses : sList)
            {
                if(ss.getSeasonId() == ses.getId())
                {
                    retList.add(ses);
                }
            }
        }
        return retList;
    }

    @GET
    @Path("/finished/{seriesId}/{seasonId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Episode> getAllFinishedEpisodes(@PathParam("seriesId") long seriesId, @PathParam("seasonId") long seasonId, 
    @Context HttpHeaders headers)
    {
        long userId = getUserIdFromHeader(headers);
        List<WatchesEpisode> watchedEpisodesList = watchService.getFinishedEpisodesFromSeasonOfSeries(userId, seriesId, seasonId);
        List<Episode> epsList = new ArrayList<>();
        if(watchedEpisodesList.size() == 0)
        {
            return epsList;
        }
        Map<Long, Series> theMap = watchService.getSeriesMap();
        Series s = theMap.get(seriesId);
        Season ss = null;
        List<Season> sesList = s.getSeasons();
        for(Season sea : sesList)
        {
            if(sea.getId() == seasonId)
            {
                ss = sea;
            }
        }
        if(ss == null)
        {
            return epsList;
        }
        List<Episode> epaList = ss.getEpisodes();
        for(WatchesEpisode ep : watchedEpisodesList)
        {
            for(Episode e : epaList)
            {
                if(ep.getEpisodeId() == e.getId())
                {
                    epsList.add(e);
                }
            }
        }
        return epsList;
    }

    @GET
    @Path("/finished/{seriesId}/{seasonId}/{episodeId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Episode getFinishedEpisodeById(@PathParam("seriesId") long seriesId, @PathParam("seasonId") long seasonId, 
    @PathParam("episodeId") long episodeId, @Context HttpHeaders headers)
    {
        long userId = getUserIdFromHeader(headers);
        WatchesEpisode fep = watchService.getFinishedEpisode(userId, seriesId, seasonId, episodeId);
        if(fep == null)
        {
            ErrorMessage msg = new ErrorMessage("Not found", 404, "Error, you have provided either a wrong series ID" 
            + " and/or season ID, and/or episode ID, OR the episode with ID=" + episodeId + " is not set as finished.");
            Response response = Response.status(Status.NOT_FOUND).entity(msg).build();
            throw new NotFoundException(response);
        }
        Map<Long, Series> theMap = watchService.getSeriesMap();
        Series s = theMap.get(seriesId);
        Season ss = null;
        List<Season> sesList = s.getSeasons();
        for(Season sea : sesList)
        {
            if(sea.getId() == seasonId)
            {
                ss = sea;
            }
        }
        if(ss == null)
        {
            ErrorMessage msg = new ErrorMessage("Not found", 404, "Error, you have provided either a wrong series ID" 
            + " and/or season ID, and/or episode ID, OR the episode with ID=" + episodeId + " is not set as finished.");
            Response response = Response.status(Status.NOT_FOUND).entity(msg).build();
            throw new NotFoundException(response);
        }
        List<Episode> epaList = ss.getEpisodes();
        for(Episode p : epaList)
        {
            if(p.getId() == episodeId)
            {
                return p;
            }
        }
        ErrorMessage msg = new ErrorMessage("Not found", 404, "Error, you have provided either a wrong series ID" 
        + " and/or season ID, and/or episode ID, OR the episode with ID=" + episodeId + " is not set as finished.");
        Response response = Response.status(Status.NOT_FOUND).entity(msg).build();
        throw new NotFoundException(response);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{seriesId}")
    public Series getWatchingSeriesById(@PathParam("seriesId") long seriesId, @Context HttpHeaders headers)
    {
        long userId = getUserIdFromHeader(headers);
        WatchesSeries s = watchService.getWatchingSeriesById(userId, seriesId);
        if(s == null) 
        {
            ErrorMessage msg = new ErrorMessage("Not found", 404, "Error, you have provided either a wrong series ID" 
            + "or the series with ID=" + seriesId + " is not on your watch list.");
            Response response = Response.status(Status.NOT_FOUND).entity(msg).build();
            throw new NotFoundException(response);
        }
        else 
        {
            Map<Long, Series> theMap = watchService.getSeriesMap();
            Series ss = theMap.get(Long.valueOf(s.getSeriesId()));
            if(ss == null)
            {
                ErrorMessage msg = new ErrorMessage("Not found", 404, "Error series not");
                Response response = Response.status(Status.NOT_FOUND).entity(msg).build();
                throw new NotFoundException(response);
            }
            Series toReturn = fillSeriesWithWatchingItems(ss, s);
            return toReturn;
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{seriesId}/{seasonId}")
    public Season getWachingSeasonById(@PathParam("seriesId") long seriesId, @PathParam("seasonId") long seasonId, 
    @Context HttpHeaders headers)
    {
        long userId = getUserIdFromHeader(headers);
        WatchesSeason season = watchService.getWatchingSeasonById(userId, seriesId, seasonId);
        if(season == null)
        {
            ErrorMessage msg = new ErrorMessage("Not found", 404, "You are either not watching the season and/or series with" +
            " the provided Id or they do not exist.");
            Response response = Response.status(Status.NOT_FOUND).entity(msg).build();
            throw new NotFoundException(response);
        }
        else
        {
            Map<Long, Series> theMap = watchService.getSeriesMap();
            Series ss = theMap.get(Long.valueOf(seriesId));
            if(ss == null)
            {
                ErrorMessage msg = new ErrorMessage("Not found", 404, "You are either not watching the season and/or series with" +
                " the provided Id or they do not exist.");
                Response response = Response.status(Status.NOT_FOUND).entity(msg).build();
                throw new NotFoundException(response);
            }
            List<Season> seasonsList = ss.getSeasons();
            if(seasonsList.size() == 0)
            {
                ErrorMessage msg = new ErrorMessage("Not found", 404, "You are either not watching the season and/or series with" +
                " the provided Id or they do not exist.");
                Response response = Response.status(Status.NOT_FOUND).entity(msg).build();
                throw new NotFoundException(response);
            }
            for(Season ses : seasonsList)
            {
                if(ses.getId() == seasonId)
                {
                    Season toReturn = fillSeasonWithWatchingItems(ses, season);
                    return toReturn;
                }
            }
            ErrorMessage msg = new ErrorMessage("Not found", 404, "You are either not watching the season and/or series with" +
            " the provided Id or they do not exist.");
            Response response = Response.status(Status.NOT_FOUND).entity(msg).build();
            throw new NotFoundException(response);
        }
    }

    @POST
    @Path("/{seriesId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addSeriesToWatchList(@PathParam("seriesId") long seriesId, @Context HttpHeaders headers)
    {
        long userId = getUserIdFromHeader(headers);
        boolean resp = watchService.addSeriesToWatchList(userId, seriesId);
        if(resp)
        {
            return Response.accepted().entity("Series with id=" + seriesId + " has been added to your watch list.")
                    .build();
        }
        else
        {
            ErrorMessage msg = new ErrorMessage("Not acceptable", 406, "The series with ID=" + seriesId + " is either not found or" + 
            " you have already added this series to your watch list.");
            Response response = Response.status(Status.NOT_ACCEPTABLE).entity(msg).build();
            throw new NotAcceptableException(response);
        }
    }

    @PUT
    @Path("/{seriesId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setSeriesAsFinished(HttpDateModel model, @PathParam("seriesId") long seriesId, @Context HttpHeaders headers)
    {
        long userId = getUserIdFromHeader(headers);
        boolean valid = checkDateFormat(model.getFinishDate());
        if(valid)
        {
            boolean resp = watchService.setSeriesAsFinished(userId, seriesId, model.getFinishDate());
            if(resp)
            {
                WatchesSeries s = watchService.getWatchingSeriesById(userId, seriesId);
                return Response.accepted().entity(s).build();
            }
            else
            {
                ErrorMessage msg = new ErrorMessage("Not found", 404, "You have provided a wrong series ID," +
                " OR this series currently isn't on your watch list, OR the date you have provided is earlier than the series"
                +" release date");
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
    @Path("/{seriesId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response removeSeriesFromWatchList(@PathParam("seriesId") long seriesId, @Context HttpHeaders headers)
    {
        long userId = getUserIdFromHeader(headers);
        boolean resp = watchService.removeSeriesFromWatchList(userId, seriesId);
        if(resp)
        {
            return Response.accepted().entity("Series with id=" + seriesId + " has been removed from your watch list.")
                    .build();
        }
        else
        {
            ErrorMessage msg = new ErrorMessage("Not found", 404, "You have provided a wrong series ID," +
            " OR this season currently isn't on your watch list.");
            Response response = Response.status(Status.NOT_FOUND).entity(msg).build();
            throw new NotFoundException(response);
        }
    }

    @POST
    @Path("/{seriesId}/{seasonId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addSeasonToWatchList(@PathParam("seriesId") long seriesId, @PathParam("seasonId") long seasonId, 
    @Context HttpHeaders headers)
    {
        long userId = getUserIdFromHeader(headers);
        boolean resp = watchService.addSeasonToWatchList(userId, seriesId, seasonId);
        if(resp)
        {
            return Response.accepted().entity("Season with id=" + seasonId + " has been added to your watch list.")
                    .build();
        }
        else
        {
            ErrorMessage msg = new ErrorMessage("Not acceptable", 406, "The season with ID=" + seasonId + " is either not found or" + 
            " you have already added this season to your watch list. Or the series with ID=" + seriesId + " is not found or you" +
            " have tried to add the season to your watch list without first adding the series.");
            Response response = Response.status(Status.NOT_ACCEPTABLE).entity(msg).build();
            throw new NotAcceptableException(response);
        }
    }

    @PUT
    @Path("/{seriesId}/{seasonId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setSeasonAsFinished(HttpDateModel model, @PathParam("seriesId") long seriesId, @PathParam("seasonId") long seasonId, 
    @Context HttpHeaders headers)
    {
        long userId = getUserIdFromHeader(headers);
        boolean valid = checkDateFormat(model.getFinishDate());
        if(valid)
        {
            boolean resp = watchService.setSeasonAsFinished(userId, seriesId, seasonId, model.getFinishDate());
            if(resp)
            {
                return Response.accepted().entity("Season with Id=" + seasonId + " has been set as finished.").build();
            }
            else
            {
                ErrorMessage msg = new ErrorMessage("Not found", 404, "You have provided a wrong series ID and/or season ID," +
                " OR they currently aren't on your watch list, OR the date you have provided is earlier than the season's"
                + " release date");
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
    @Path("/{seriesId}/{seasonId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response removeSeasonFromWatchList(@PathParam("seriesId") long seriesId, @PathParam("seasonId") long seasonId, 
    @Context HttpHeaders headers)
    {
        long userId = getUserIdFromHeader(headers);
        boolean resp = watchService.removeSeasonFromWatchList(userId, seriesId, seasonId);
        if(resp)
        {
            return Response.accepted().entity("Season with id=" + seasonId + " has been deleted.").build();
        }
        else
        {
            ErrorMessage msg = new ErrorMessage("Not found", 404, "You have either provided a wrong series ID and/or" +
            " season ID, OR the series/season are not currently on your watch list.");
            Response response = Response.status(Status.NOT_FOUND).entity(msg).build();
            throw new NotFoundException(response);
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{seriesId}/{seasonId}/{episodeId}")
    public Episode getWatchingEpisodeById(@PathParam("seriesId") long seriesId, @PathParam("seasonId") long seasonId, 
    long episodeId, @Context HttpHeaders headers)
    {
        long userId = getUserIdFromHeader(headers);
        WatchesEpisode epis = watchService.getWatchingEpisodeById(userId, seriesId, seasonId, episodeId);
        if(epis == null)
        {
            ErrorMessage msg = new ErrorMessage("Not found", 404, "You are either not watching the episode and/or season and/or series with" +
            " the provided Id or they do not exist.");
            Response response = Response.status(Status.NOT_FOUND).entity(msg).build();
            throw new NotFoundException(response);
        }
        else
        {
            Map<Long, Series> theMap = watchService.getSeriesMap();
            Series s = theMap.get(Long.valueOf(seriesId));
            if(s == null)
            {
                ErrorMessage msg = new ErrorMessage("Not found", 404, "Error series null.");
                Response response = Response.status(Status.NOT_FOUND).entity(msg).build();
                throw new NotFoundException(response);
            }
            List<Season> seasons = s.getSeasons();
            if(seasons.size() == 0)
            {
                ErrorMessage msg = new ErrorMessage("Not found", 404, "Error seasons list is empty.");
                Response response = Response.status(Status.NOT_FOUND).entity(msg).build();
                throw new NotFoundException(response);
            }
            for(Season ss : seasons)
            {
                if(ss.getId() == seasonId)
                {
                    List<Episode> episodes = ss.getEpisodes();
                    if(episodes.size() == 0)
                    {
                        ErrorMessage msg = new ErrorMessage("Not found", 404, "Error episodes list is empty.");
                        Response response = Response.status(Status.NOT_FOUND).entity(msg).build();
                        throw new NotFoundException(response);
                    }
                    for(Episode eps : episodes)
                    {
                        if(eps.getId() == episodeId)
                        {
                            return eps;
                        }
                    }
                }
            }
            ErrorMessage msg = new ErrorMessage("Not found", 404, "You are either not watching the episode and/or season and/or series with" +
            " the provided Id or they do not exist.");
            Response response = Response.status(Status.NOT_FOUND).entity(msg).build();
            throw new NotFoundException(response);
        }
    }

    @GET
    @Path("/{seriesId}/lastwatchedinorder")
    @Produces(MediaType.APPLICATION_JSON)
    public Episode getLastWatchedEpisodeFromSeries(@PathParam("seriesId") long seriesId, @Context HttpHeaders headers)
    {
        long userId = getUserIdFromHeader(headers);
        WatchesEpisode episode = watchService.getLastWatchedEpisodeFromSeries(userId, seriesId);
        if(episode == null)
        {
            ErrorMessage msg = new ErrorMessage("Not found", 404, "You have no last watched episode.");
            Response response = Response.status(Status.NOT_FOUND).entity(msg).build();
            throw new NotFoundException(response);
        }
        else
        {
            Map<Long, Series> theMap = watchService.getSeriesMap();
            Series s = theMap.get(Long.valueOf(seriesId));
            List<Season> seasons = s.getSeasons();
            for(Season ss : seasons)
            {
                List<Episode> eps = ss.getEpisodes();
                for(Episode p : eps)
                {
                    if(p.getId() == episode.getEpisodeId())
                    {
                        return p;
                    }
                }
            }
            ErrorMessage msg = new ErrorMessage("Not found", 404, "You have no last watched episode.");
            Response response = Response.status(Status.NOT_FOUND).entity(msg).build();
            throw new NotFoundException(response);
        }
    }

    @GET
    @Path("/{seriesId}/lastwatchedbyfinishdate")
    @Produces(MediaType.APPLICATION_JSON)
    public Episode getLastWatchedEpisodeFromSeriesByDate(@PathParam("seriesId") long seriesId, @Context HttpHeaders headers)
    {
        long userId = getUserIdFromHeader(headers);
        WatchesEpisode episode = watchService.getLastWatchedEpisodeFromSeriesUsingDate(userId, seriesId);
        if(episode == null)
        {
            ErrorMessage msg = new ErrorMessage("Not found", 404, "You have no last watched episode.");
            Response response = Response.status(Status.NOT_FOUND).entity(msg).build();
            throw new NotFoundException(response);
        }
        else
        {
            Map<Long, Series> theMap = watchService.getSeriesMap();
            Series s = theMap.get(Long.valueOf(seriesId));
            List<Season> seasons = s.getSeasons();
            for(Season ss : seasons)
            {
                List<Episode> eps = ss.getEpisodes();
                for(Episode p : eps)
                {
                    if(p.getId() == episode.getEpisodeId())
                    {
                        return p;
                    }
                }
            }
            ErrorMessage msg = new ErrorMessage("Not found", 404, "You have no last watched episode.");
            Response response = Response.status(Status.NOT_FOUND).entity(msg).build();
            throw new NotFoundException(response);
        }
    }

    @POST
    @Path("/{seriesId}/{seasonId}/{episodeId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addEpisodeToWatchList(@PathParam("seriesId") long seriesId, @PathParam("seasonId") long seasonId, 
    @PathParam("episodeId") long episodeId, @Context HttpHeaders headers)
    {
        long userId = getUserIdFromHeader(headers);
        boolean resp = watchService.addEpisodeToWatchList(userId, seriesId, seasonId, episodeId);
        if(resp)
        {
            return Response.accepted().entity("Episode with id=" + episodeId + " has been added to your watch list.")
                    .build();
        }
        else
        {
            ErrorMessage msg = new ErrorMessage("Not acceptable", 406, "The episode with ID=" + episodeId + " is either not found"
            + " or it is already added to your watch list. Other possible causes of this error could be that you have not added the"
            + " series and/or season to the watch list before trying to add this episode, or you have provided a wrong season" 
            + " and/or series ID.");
            Response response = Response.status(Status.NOT_ACCEPTABLE).entity(msg).build();
            throw new NotAcceptableException(response);
        }
    }

    @PUT
    @Path("/{seriesId}/{seasonId}/{episodeId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setEpisodeAsFinished(HttpDateModel model, @PathParam("seriesId") long seriesId, @PathParam("seasonId") long seasonId, 
    @PathParam("episodeId") long episodeId, @Context HttpHeaders headers)
    {
        boolean valid = checkDateFormat(model.getFinishDate());
        if(valid)
        {
            long userId = getUserIdFromHeader(headers);
            boolean resp = watchService.setEpisodeAsFinished(userId, seriesId, seasonId, episodeId, model.getFinishDate());
            if(resp)
            {
                WatchesEpisode ep = watchService.getWatchingEpisodeById(userId, seriesId, seasonId, episodeId);
                return Response.accepted().entity(ep).build();
            }
            else
            {
                ErrorMessage msg = new ErrorMessage("Not found", 404, "You seem to have provided either a wrong seried Id, season Id"
                + " and/or episodeId. OR they aren't on your watch list, OR the date you have provided is earlier than the" + 
                " episode's release date.");
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
    @Path("/{seriesId}/{seasonId}/{episodeId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response removeEpisodeFromWatchList(@PathParam("seriesId") long seriesId, @PathParam("seasonId") long seasonId, 
    @PathParam("episodeId") long episodeId, @Context HttpHeaders headers)
    {
        long userId = getUserIdFromHeader(headers);
        boolean resp = watchService.removeEpisodeFromWatchList(userId, seriesId, seasonId, episodeId);
        if(resp)
        {
            return Response.accepted().entity("Episode with id=" + episodeId + " has been removed from your watch list.").build();
        }
        else
        {
            ErrorMessage msg = new ErrorMessage("Not found", 404, "You seem to have provided a wrong series ID, season ID" + 
            " and/or episode ID OR the series/season/episode is not on your watch list.");
            Response response = Response.status(Status.NOT_FOUND).entity(msg).build();
            throw new NotFoundException(response);
        }
    }

    public Season fillSeasonWithWatchingItems(Season s, WatchesSeason ws)
    {
        List<Episode> episodes = new ArrayList<>();
        Season toRet = new Season(s.getId(), s.getSeasonNumber(), s.getSeriesId(), s.getRating(), s.getDescription(), 
        episodes, s.getMainActors());
        List<WatchesEpisode> epsList = ws.getEpisodes();
        if(epsList.size() == 0)
        {
            return toRet;
        }
        for(WatchesEpisode p : epsList)
        {
            List<Episode> episodeList = s.getEpisodes();
            for(Episode ep : episodeList)
            {
                if(p.getEpisodeId() == ep.getId())
                {
                    episodes.add(ep);
                }
            }
        }
        toRet.setEpisodes(episodes);
        return toRet;
    }

    public Series fillSeriesWithWatchingItems(Series s, WatchesSeries ws)
    {
        List<Season> seasons = new ArrayList<>();
        Series toReturn = new Series(s.getId(), s.getName(), s.getRating(), s.getDescription(), seasons, s.getGenres());
        List<WatchesSeason> sesList = ws.getSeasons();
        if(sesList.size() == 0)
        {
            return toReturn;
        }
        for(WatchesSeason ses : sesList)
        {
            List<Season> seasonsList = s.getSeasons();
            for(Season sea : seasonsList)
            {
                if(ses.getSeasonId() == sea.getId())
                {
                    List<Episode> episodes = new ArrayList<>();
                    Season newSeason = new Season(sea.getId(), sea.getSeasonNumber(), s.getId(), sea.getRating(), 
                    sea.getDescription(), episodes, sea.getMainActors());
                    List<WatchesEpisode> watchesEpisodes = ses.getEpisodes();
                    for(WatchesEpisode ep : watchesEpisodes)
                    {
                        List<Episode> epsList = sea.getEpisodes();
                        for(Episode e : epsList)
                        {
                            if(ep.getEpisodeId() == e.getId())
                            {
                                episodes.add(e);
                            }
                        }
                    }
                    newSeason.setEpisodes(episodes);
                    seasons.add(newSeason);
                }
            }
        }
        toReturn.setSeasons(seasons);
        return toReturn;
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