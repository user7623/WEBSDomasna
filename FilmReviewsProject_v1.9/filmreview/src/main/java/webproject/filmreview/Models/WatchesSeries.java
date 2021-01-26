package webproject.filmreview.Models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class WatchesSeries implements Serializable
{

    private static final long serialVersionUID = -1718741879065451192L;
    private long Id;
    private long userId;
    private long seriesId; 

    @XmlAttribute
    private Boolean status;

    @XmlAttribute
    private Date finishedAt;

    @XmlList
    private List<WatchesSeason> seasons;

    public WatchesSeries() {
    }

    public WatchesSeries(long Id, long userId, long seriesId, List<WatchesSeason> seasons) {
        this.Id = Id;
        this.userId = userId;
        this.seriesId = seriesId;
        this.status = false;
        this.finishedAt = null;
        this.seasons = seasons;
    }

    public long getId() {
        return this.Id;
    }

    public void setId(long Id) {
        this.Id = Id;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getSeriesId() {
        return this.seriesId;
    }

    public void setSeriesId(long seriesId) {
        this.seriesId = seriesId;
    }

    public Boolean getStatus() {
        return this.status;
    }

    public Date getFinishedAt() {
        return this.finishedAt;
    }

    public List<WatchesSeason> getSeasons() {
        return this.seasons;
    }

    public void setSeasons(List<WatchesSeason> seasons) {
        this.seasons = seasons;
    }

    public void setAsFinished(Date finishDate)
    {
        this.status = true;
        this.finishedAt = finishDate;
    }

}