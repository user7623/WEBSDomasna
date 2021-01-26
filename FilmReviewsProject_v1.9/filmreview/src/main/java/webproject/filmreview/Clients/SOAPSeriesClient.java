package webproject.filmreview.Clients;

import java.util.List;
import java.util.Map;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import webproject.filmreview.Models.Genres;
import webproject.filmreview.Models.Season;
import webproject.filmreview.Models.Series;
import webproject.filmreview.Utilities.Database;

@WebService
public class SOAPSeriesClient {

    @WebMethod
    public String greetuser(String name)
    {
        return "Hello" + name;
    }
    
    @WebMethod
    public String doGetAllSeriesSOAP()
    {
        
        Map<Long , Series> seriesDictionary = Database.getAllSeries();
        
        String allSeriesString = "";

        if (seriesDictionary.size() == 0)
        {
            return "Database is empty";
        }

        for (Map.Entry<Long, Series> entry : seriesDictionary.entrySet()) {
            //Long mLong = entry.getKey();
            Series mSeries = entry.getValue();
            allSeriesString = allSeriesString + "Name: ";
            allSeriesString = allSeriesString + mSeries.getName();
            allSeriesString = allSeriesString + "\n";
            allSeriesString = allSeriesString + "Rating: ";
            allSeriesString = allSeriesString + String.valueOf(mSeries.getRating());
            allSeriesString = allSeriesString + "\n";
            allSeriesString = allSeriesString + "Description: ";
            allSeriesString = allSeriesString + mSeries.getDescription();
            allSeriesString = allSeriesString + "\n";
            allSeriesString = allSeriesString + "Seasons: ";
            allSeriesString = allSeriesString + mSeries.getSeasons();
            allSeriesString = allSeriesString + "\n";
            allSeriesString = allSeriesString + "Genres: ";     
            allSeriesString = allSeriesString + mSeries.getGenres();
        }
        return allSeriesString;
    }

    @WebMethod
    public String doGetAllFinishedSeriesSOAP()
    {
        
        Map<Long , Series> seriesDictionary = Database.getAllSeries();
        
        String allSeriesString = "";

        if (seriesDictionary.size() == 0)
        {
            return "Database is empty";
        }

        for (Map.Entry<Long, Series> entry : seriesDictionary.entrySet()) {
            //Long mLong = entry.getKey();
            Series mSeries = entry.getValue();
            allSeriesString = allSeriesString + "Name: ";
            allSeriesString = allSeriesString + mSeries.getName();
            
        }
        return allSeriesString;
    }

    @WebMethod
    public String doGetASeriesSOAP(String nameOfRequestedSeries)
    {
        
        Map<Long , Series> seriesDictionary = Database.getAllSeries();
        
        String allSeriesString = "";

        if (seriesDictionary.size() == 0)
        {
            return "Database is empty";
        }

        for (Map.Entry<Long, Series> entry : seriesDictionary.entrySet()) {
            //Long mLong = entry.getKey();
            Series mSeries = entry.getValue();
            if (mSeries.getName().contains(nameOfRequestedSeries))
            {
                allSeriesString = allSeriesString + "Name: ";
                allSeriesString = allSeriesString + mSeries.getName();
            }
        }
        return allSeriesString;
    }
    @WebMethod
    public String doGetASeason(long seriesId, long seasonId)
    {
        
        Map<Long , Series> seriesDictionary = Database.getAllSeries();
        
        Season seasonToreturn = new Season();
        for (Map.Entry<Long, Series> entry : seriesDictionary.entrySet()) {
            //Long mLong = entry.getKey();
            Series mSeries = entry.getValue();
            
            if (mSeries.getId() == seasonId)
            {
                List<Season> seasons = mSeries.getSeasons();
                for (Season fseason : seasons)
                {
                    if (fseason.getId() == seasonId)
                    {
                        seasonToreturn = fseason;   
                    
                    }
                }
            }      
        }

        String returnString = "";
        returnString = returnString + "Main actors: ";
        returnString = returnString + seasonToreturn.getMainActors();
        returnString = returnString + "\n";
        returnString = returnString + "Number of episodes: ";
        returnString = returnString + String.valueOf(seasonToreturn.getEpisodes().size());
        returnString = returnString + "\n";
        returnString = returnString + "Description: ";
        returnString = returnString + seasonToreturn.getDescription();
        returnString = returnString + "\n";
        returnString = returnString + "Number of seasons: ";
        returnString = returnString + String.valueOf(seasonToreturn.getSeasonNumber()); 
        returnString = returnString + "\n";
        returnString = returnString + "Rating: ";
        returnString = returnString + String.valueOf(seasonToreturn.getRating()); 
        returnString = returnString + "\n";

        return returnString;
    }

    @WebMethod
    public String addSeriesToDatabase(long Id, String name, float rating, String description, String seasons, String genres)
    {
        Series seriesToAdd = new Series();
        String infoString = "";
        if (String.valueOf(Id) == "?")
        {
            infoString = infoString + "No Id set \n";   
        }else { seriesToAdd.setId(Id);
        }
        if (!name.equals(""))
        {
            seriesToAdd.setName(name);
        }else {infoString = infoString + "No name given! \n";return infoString;}
        if (String.valueOf(rating) == "?")
        {
            seriesToAdd.setRating(rating);
        }else {infoString = infoString + "No rating set \n";}
        if (!description.equals(""))
        {
            seriesToAdd.setDescription(description);
        }else {infoString = infoString + "no description added v";}
        if (seasons.equals(""))
        {
            infoString = infoString + "no seasons added";
        }else{
            //TODO : trgni gi kosite crti na redot odma pod ovoj
            //seriesToAdd.setSeasons(seasons);
        }
        if (genres.equals(""))
        {
            infoString = infoString + "No genres added";
        }else {
            //TODO : trgni gi kosite crti na redot odma pod ovoj
            //seriesToAdd.setGenres(genres);
        }

        try {
            Database.addSeries(Id, seriesToAdd);
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
            return e.getLocalizedMessage();
        }



        if (infoString.equals(""))
        {
            return "series added to database";
        }else {
            return "series added, but with problems: " + infoString;
        }

        
    }

}