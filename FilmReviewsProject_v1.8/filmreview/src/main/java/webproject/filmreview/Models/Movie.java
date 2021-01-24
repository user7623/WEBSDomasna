package webproject.filmreview.Models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Movie implements Serializable
{
    
    private static final long serialVersionUID = 6853441421576301177L;
    private long Id;
    private String name;

    @XmlAttribute
    private Date releaseDate;
    
    private float rating;
    private String description;
    private String mainActors;
    private List<Genres> genres;   

    public Movie() 
    { }

    public Movie(long Id, String name, Date releaseDate, float rating, String description, String mainActors, List<Genres> genres) 
    {
        this.Id = Id;
        this.name = name;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.description = description;
        this.mainActors = mainActors;
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

    public Date getReleaseDate() 
    {
        return this.releaseDate;
    }

    public void setReleaseDate(Date releaseDate) 
    {
        this.releaseDate = releaseDate;
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

    public String getMainActors() 
    {
        return this.mainActors;
    }

    public void setMainActors(String mainActors) 
    {
        this.mainActors = mainActors;
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
