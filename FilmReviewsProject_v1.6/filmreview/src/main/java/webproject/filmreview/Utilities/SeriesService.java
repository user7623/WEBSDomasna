package webproject.filmreview.Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import webproject.filmreview.Models.Episode;
import webproject.filmreview.Models.Season;
import webproject.filmreview.Models.Series;

public class SeriesService 
{
    private static Map<Long, Series> series = new HashMap<>();

    public SeriesService()
    {

    }

    public List<Series> getAllSeries()
    {
        return new ArrayList<Series>(series.values());
    }

    public Series getSeriesById(long id)
    {
        if(id<0)
        {
            return null;
        }
        return series.get(id);
    }

    public Series addSeries(Series series1)
    {
        series1.setId(series.size()+1);
        series.put(series1.getId(), series1);
        return series1;
    }

    public Series updateSeries(Series series1)
    {
        if(series1.getId()<=0)
        {
            return null;
        }
        series.put(series1.getId(), series1);
        return series1;
    }

    public void deleteSeries(long id)
    {
        series.remove(id);
    }

    public boolean addSeasonToSeries(Season season, long seriesId)
    {
        if(seriesId<=0)
        {
            return false;
        }
        Series ser = series.get(seriesId);
        if(ser.equals(null)) { return false; }
        List<Season> sesList = ser.getSeasons();
        season.setId(sesList.size()+1);
        season.setSeasonNumber(sesList.size()+1);
        season.setSeriesId(seriesId);
        sesList.add(season);
        ser.setSeasons(sesList);
        series.put(ser.getId(), ser);
        return true;
    }

    public boolean addEpisodeToSeason(Episode episode, long seasonId, long seriesId)
    {
        if(seriesId<=0)
        {
            return false;
        }
        Series ser = series.get(seriesId);
        if(ser.equals(null)) { return false; }
        List<Season> sList = ser.getSeasons();
        int i = 0;
        for(Season s : sList)
        {
            if(s.getId() == seasonId)
            {
                List<Episode> epsList = s.getEpisodes();
                episode.setId(epsList.size()+1);
                episode.setSeasonId(s.getId());
                epsList.add(episode);
                s.setEpisodes(epsList);
                sList.set(i, s);
                ser.setSeasons(sList);
                series.put(seriesId, ser);
                return true;
            }
            i++;
        }
        return false;                                                                 
    }

    public Season deleteSeasonFromSeries(long seriesId, long seasonId)
    {
        if(seriesId<=0)
        {
            return null;
        }
        Series ser = series.get(seriesId);
        if(ser.equals(null)) { return null; }
        List<Season> sList = ser.getSeasons();
        for(Season s : sList)
        {
            if(s.getId() == seasonId)
            {
                sList.remove(s);
                ser.setSeasons(sList);
                series.put(seriesId, ser);
                return s;
            }
        }
        return null;
    }

    public Episode deleteEpisodeFromSeason(long seriesId, long seasonId, long episodeId)
    {
        if(seriesId<=0)
        {
            return null;
        }
        Series ser = series.get(seriesId);
        if(ser.equals(null)) { return null; }
        List<Season> sList = ser.getSeasons();
        int i = 0;
        for(Season s : sList)
        {
            if(s.getId() == seasonId)
            {
                List<Episode> epsList = s.getEpisodes();
                for(Episode p : epsList)
                {
                    if(p.getId() == episodeId)
                    {
                        epsList.remove(p);
                        s.setEpisodes(epsList);
                        sList.set(i, s);
                        ser.setSeasons(sList);
                        series.put(seriesId, ser);
                        return p;
                    }
                }
            }
            i++;
        }
        return null;
    }

    
}