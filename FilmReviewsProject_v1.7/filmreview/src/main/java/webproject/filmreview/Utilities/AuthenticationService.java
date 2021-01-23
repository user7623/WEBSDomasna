package webproject.filmreview.Utilities;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import webproject.filmreview.PasswordHasher;
import webproject.filmreview.Models.AuthenticationDetails;
import webproject.filmreview.Models.User;
import webproject.filmreview.Models.WatchesMovie;
import webproject.filmreview.Models.WatchesSeries;

public class AuthenticationService 
{
    public static Map<Long, User> users = Database.getAllUsers();
    public static Map<Long, AuthenticationDetails> sessions = Database.getSessions();
    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256;

    public AuthenticationService()
    {
        seedData();
    }

    public void seedData()
    {
        List<WatchesMovie> watchedmovies = new ArrayList<>();
        List<WatchesSeries> watchedseries = new ArrayList<>();
        String hashedPass = PasswordHasher.generateSecurePassword("123", "zoran");
        User user = new User(Long.valueOf(1), "zoran", hashedPass, watchedmovies, watchedseries);
        users.put(user.getId(), user);
        String newSaltAsPostfix = user.getUsername();
        String accessTokenMaterial = user.getId() + newSaltAsPostfix;
        byte[] encryptedAccessToken = null;
        try
        {
            encryptedAccessToken = encrypt(user.getPassword(), accessTokenMaterial);
        }
        catch(Exception e)
        {
            System.out.println(e.getLocalizedMessage());
        }
        String encryptedAccessTokenBase64Encoded = Base64.getEncoder().encodeToString(encryptedAccessToken);
        String tokenToSaveToDatabase = encryptedAccessTokenBase64Encoded;
        AuthenticationDetails details = new AuthenticationDetails(user.getId(), tokenToSaveToDatabase);
        details.setUserId(user.getId());
        sessions.put(details.getUserId(), details);
    }

    //funkcija koja se povikuva pri login pred da se izdade access token, koj sluzi za aftenticiranje na korisnici
    public User authenticate(String username, String password) //ako ne vrati null, togas prodolzi so issueSecureToken
    {
        int i = users.size();
        if(i == 0)
        {
            return null;
        }
        User us = null;
        for(int j=1;j<=i;j++)
        {
            us = users.get(new Long(j));
            if(us.getUsername() == username)
            {
                break;
            }

        }
        boolean ver = PasswordHasher.verifyUserPassword(password, us.getPassword(), username);
        if(ver)
        {
            return us;
        }
        else
        {
            return null;
        }
    }

    public String issueSecureToken(User user) // pri logiranje
    {
        String returnValue = null;
        String newSaltAsPostfix = user.getUsername();
        String accessTokenMaterial = user.getId() + newSaltAsPostfix;
        byte[] encryptedAccessToken = null;
        try
        {
            encryptedAccessToken = encrypt(user.getPassword(), accessTokenMaterial);
        }
        catch(Exception e)
        {
            System.out.println(e.getLocalizedMessage());
        }
        String encryptedAccessTokenBase64Encoded = Base64.getEncoder().encodeToString(encryptedAccessToken);
        String tokenToSaveToDatabase = encryptedAccessTokenBase64Encoded;
        AuthenticationDetails details = new AuthenticationDetails(user.getId(), tokenToSaveToDatabase);
        details.setUserId(user.getId());
        sessions.put(details.getUserId(), details);
        returnValue = user.getId() + " " + encryptedAccessTokenBase64Encoded;
        return returnValue;
    }

    public String issueSecureTokenTest(User user)
    {
        String returnValue = null;
        String newSaltAsPostfix = user.getUsername();
        String accessTokenMaterial = user.getId() + newSaltAsPostfix;
        byte[] encryptedAccessToken = null;
        try
        {
            encryptedAccessToken = encrypt(user.getPassword(), accessTokenMaterial);
        }
        catch(Exception e)
        {
            System.out.println(e.getLocalizedMessage());
        }
        String encryptedAccessTokenBase64Encoded = Base64.getEncoder().encodeToString(encryptedAccessToken);
        returnValue = user.getId() + " " + encryptedAccessTokenBase64Encoded;
        return returnValue;
    }

    public User getLoggedUserFromToken(long userId, String token)
    {
        AuthenticationDetails dets = sessions.get(userId);
        if(dets == null)
        {
            return null;
        }
        if(dets.getToken().equals(token))
        {
            return users.get(userId);
        }
        else
        {
            return null;
        }
    }

    public void deleteSecureToken(long userId, String token) // pri logout
    {
        AuthenticationDetails dets = sessions.get(userId);
        if(dets.getToken() == dets.getToken());
        {
            sessions.remove(userId);
        }
    }

    public boolean isValidToken(long userId, String token)
    {
        AuthenticationDetails dets = sessions.get(userId);
        if(dets.getToken() == token)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public byte[] encrypt(String securePassword, String accessTokenMaterial) throws InvalidKeySpecException 
    {
        return hash(securePassword.toCharArray(), accessTokenMaterial.getBytes());
    }

    private byte[] hash(char[] password, byte[] salt) throws InvalidKeySpecException 
    {
        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
        Arrays.fill(password, Character.MIN_VALUE);
        try 
        {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return skf.generateSecret(spec).getEncoded();
        } 
        catch (NoSuchAlgorithmException | InvalidKeySpecException e) 
        {
            throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
        } 
        finally 
        {
            spec.clearPassword();
        }
    }

}