package webproject.filmreview.Models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AuthenticationDetails 
{
    private long userId;
    private String token;

    public AuthenticationDetails() { }

    public AuthenticationDetails(long userId, String token)
    {
        this.userId = userId;
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

}