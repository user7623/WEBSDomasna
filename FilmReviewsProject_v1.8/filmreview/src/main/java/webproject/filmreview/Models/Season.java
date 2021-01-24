package webproject.filmreview.Models;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Season implements Serializable
{

    private static final long serialVersionUID = -8817002834479142404L;
    private long Id;
    private int seasonNumber;
    private long seriesId;
    private float rating;
    private String description;

    @XmlList
    private List<Episode> episodes;
    
    private String mainActors;  
    
    public Season() 
    { }

    public Season(long Id, int seasonNumber, long seriesId, float rating, String description, List<Episode> episodes, String mainActors) 
    {
        this.Id = Id;
        this.seasonNumber = seasonNumber;
        this.seriesId = seriesId;
        this.rating = rating;
        this.description = description;
        this.episodes = episodes;
        this.mainActors = mainActors;
    }

    public long getId() 
    {
        return this.Id;
    }

    public void setId(long Id) 
    {
        this.Id = Id;
    }

    public int getSeasonNumber() 
    {
        return this.seasonNumber;
    }

    public void setSeasonNumber(int seasonNumber) 
    {
        this.seasonNumber = seasonNumber;
    }

    public long getSeriesId() 
    {
        return this.seriesId;
    }

    public void setSeriesId(long seriesId) 
    {
        this.seriesId = seriesId;
    }

    public float getRating() 
    {
        return this.rating;
    }

    public void setRating(float rating) 
    {
        this.rating = rating;
    }

    public String getDescription() 
    {
        return this.description;
    }

    public void setDescription(String description) 
    {
        this.description = description;
    }

    public List<Episode> getEpisodes() 
    {
        return this.episodes;
    }

    public void setEpisodes(List<Episode> episodes) 
    {
        this.episodes = episodes;
    }

    public String getMainActors() 
    {
        return this.mainActors;
    }

    public void setMainActors(String mainActors) 
    {
        this.mainActors = mainActors;
    }

}