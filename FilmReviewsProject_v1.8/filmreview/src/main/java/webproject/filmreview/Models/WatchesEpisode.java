package webproject.filmreview.Models;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class WatchesEpisode implements Serializable
{

    private static final long serialVersionUID = -3116522679540020549L;
    private long Id;
    private long watchesSeasonId;
    private long episodeId; 

    @XmlAttribute
    private Boolean status;

    @XmlAttribute
    private Date finishedAt;   

    public WatchesEpisode() {
    }

    public WatchesEpisode(long Id, long watchesSeasonId, long episodeId) {
        this.Id = Id;
        this.watchesSeasonId = watchesSeasonId;
        this.episodeId = episodeId;
        this.status = false;
        this.finishedAt = null;
    }

    public long getId() {
        return this.Id;
    }

    public void setId(long Id) {
        this.Id = Id;
    }

    public long getWatchesSeasonId() {
        return this.watchesSeasonId;
    }

    public void setWatchesSeasonId(long watchesSeasonId) {
        this.watchesSeasonId = watchesSeasonId;
    }

    public long getEpisodeId() {
        return this.episodeId;
    }

    public void setEpisodeId(long episodeId) {
        this.episodeId = episodeId;
    }

    public Boolean isStatus() {
        return this.status;
    }

    public Boolean getStatus() {
        return this.status;
    }

    public Date getFinishedAt() {
        return this.finishedAt;
    }

    public void setAsFinished(Date finishDate)
    {
        this.finishedAt = finishDate;
        this.status = true;
    }

}