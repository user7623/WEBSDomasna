package webproject.filmreview.Models;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Series implements Serializable
{
    
    private static final long serialVersionUID = 3658492853623216037L;
    private long Id;
    private String name;
    private float rating;
    private String description;

    @XmlList
    private List<Season> seasons;
    
    private List<Genres> genres;

    public Series() 
    { }

    public Series(long Id, String name, float rating, String description, List<Season> seasons, List<Genres> genres) 
    {
        this.Id = Id;
        this.name = name;
        this.rating = rating;
        this.description = description;
        this.seasons = seasons;
        this.genres = genres;
    }

    public long getId() 
    {
        return this.Id;
    }

    public void setId(long Id) 
    {
        this.Id = Id;
    }

    public String getName() 
    {
        return this.name;
    }

    public void setName(String name) 
    {
        this.name = name;
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

    public List<Season> getSeasons() 
    {
        return this.seasons;
    }

    public void setSeasons(List<Season> seasons) 
    {
        this.seasons = seasons;
    }

    public List<Genres> getGenres() 
    {
        return this.genres;
    }

    public void setGenres(List<Genres> genres) 
    {
        this.genres = genres;
    }

}