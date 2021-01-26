package webproject.filmreview.Models;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class WatchesMovie implements Serializable
{

    private static final long serialVersionUID = -7743091639380987349L;
    private long Id;
    private long userId;
    private long movieId;

    @XmlAttribute
    private Boolean status;

    @XmlAttribute
    private Date finishedAt; 

    public WatchesMovie() { }

    public WatchesMovie(long Id, long userId, long movieId) 
    {
        this.Id = Id;
        this.userId = userId;
        this.movieId = movieId;
        this.status = false;
        this.finishedAt = null;
    }

    public long getId() 
    {
        return this.Id;
    }

    public void setId(long Id) 
    {
        this.Id = Id;
    }

    public long getUserId() 
    {
        return this.userId;
    }

    public void setUserId(long userId) 
    {
        this.userId = userId;
    }

    public long getMovieId() 
    {
        return this.movieId;
    }

    public void setMovieId(long movieId) 
    {
        this.movieId = movieId;
    }

    public Boolean getStatus() 
    {
        return this.status;
    }

    public Date getFinishedAt() 
    {
        return this.finishedAt;
    }

    public void setAsFinished(Date finishDate)
    {
        this.finishedAt = finishDate;
        this.status = true;
    }

}