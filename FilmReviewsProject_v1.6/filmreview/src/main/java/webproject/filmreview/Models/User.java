package webproject.filmreview.Models;

import java.util.List;

public class User 
{

    private long Id;
    private String username;
    private String password;
    private List<WatchesMovie> watchedMoviesList;
    private List<WatchesSeries> watchedSeriesList;

    public User() 
    { }

    public User(long Id, String username, String password, List<WatchesMovie> watchedMoviesList, List<WatchesSeries> watchedSeriesList) 
    {
        this.Id = Id;
        this.username = username;
        this.password = password;
        this.watchedMoviesList = watchedMoviesList;
        this.watchedSeriesList = watchedSeriesList;
    }

    public long getId() 
    {
        return this.Id;
    }

    public void setId(long Id) 
    {
        this.Id = Id;
    }

    public String getUsername() 
    {
        return this.username;
    }

    public void setUsername(String username) 
    {
        this.username = username;
    }

    public String getPassword() 
    {
        return this.password;
    }

    public void setPassword(String password) 
    {
        this.password = password;
    }

    public List<WatchesMovie> getWatchedMoviesList() 
    {
        return this.watchedMoviesList;
    }

    public void setWatchedMoviesList(List<WatchesMovie> watchedMoviesList) 
    {
        this.watchedMoviesList = watchedMoviesList;
    }

    public List<WatchesSeries> getWatchedSeriesList() 
    {
        return this.watchedSeriesList;
    }

    public void setWatchedSeriesList(List<WatchesSeries> watchedSeriesList) 
    {
        this.watchedSeriesList = watchedSeriesList;
    }

}