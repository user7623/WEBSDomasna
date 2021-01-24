package webproject.filmreview.Clients;

import java.util.List;
import java.util.Map;

import javax.jws.WebMethod;
import javax.jws.WebService;
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

        for (Map.Entry<Long, Series> entry : seriesDictionary.entrySet()) {
            //Long mLong = entry.getKey();
            Series mSeries = entry.getValue();
            allSeriesString.concat("Name: ");
            allSeriesString.concat(mSeries.getName());
            allSeriesString.concat("\n");     
        }
        return "a list of all the series: " + allSeriesString;
    }

    @WebMethod
    public String doGetAllFinishedSeriesSOAP()
    {
        
        Map<Long , Series> seriesDictionary = Database.getAllSeries();
        
        String allSeriesString = "";

        for (Map.Entry<Long, Series> entry : seriesDictionary.entrySet()) {
            //Long mLong = entry.getKey();
            Series mSeries = entry.getValue();
            allSeriesString.concat("Name: ");
            allSeriesString.concat(mSeries.getName());
            allSeriesString.concat("\n");
        }
        return "a list of all finished series: " + allSeriesString;
    }

    @WebMethod
    public String doGetASeriesSOAP(String nameOfRequestedSeries)
    {
        
        Map<Long , Series> seriesDictionary = Database.getAllSeries();
        
        String allSeriesString = "";

        for (Map.Entry<Long, Series> entry : seriesDictionary.entrySet()) {
            //Long mLong = entry.getKey();
            Series mSeries = entry.getValue();
            if (mSeries.getName().contains(nameOfRequestedSeries))
            {
                allSeriesString.concat("Name: ");
                allSeriesString.concat(mSeries.getName());
                allSeriesString.concat("\n");
            }
        }
        return "Series that contain "+ nameOfRequestedSeries+ "in their name: " + allSeriesString;
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

        String actorsString = seasonToreturn.getMainActors();
        String numberOfEpisodes = Integer.toString(seasonToreturn.getEpisodes().size()); 
        String descriptionString = seasonToreturn.getDescription();
        String seasonNumber = Integer.toString(seasonToreturn.getSeasonNumber());
        String ratingstring = Float.toString(seasonToreturn.getRating());
        String returnString = "";
        returnString.concat("Main actors: ");
        returnString.concat(actorsString);
        returnString.concat("\n");
        returnString.concat("Number of episodes: ");
        returnString.concat(numberOfEpisodes);
        returnString.concat("\n");
        returnString.concat("Description: ");
        returnString.concat(descriptionString);
        returnString.concat("\n");
        returnString.concat("Season number:");
        returnString.concat(seasonNumber);
        returnString.concat("\n");
        returnString.concat("Rating: ");
        returnString.concat(ratingstring);
        returnString.concat("\n");

        return returnString;
    }



}