package webproject.filmreview.Resources;

import java.util.List;
import java.util.StringTokenizer;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.internal.util.Base64;

import webproject.filmreview.Utilities.AuthenticationService;

@Path("/secured")
public class LogoutResource 
{
    private AuthenticationService authService = new AuthenticationService();
    private static final String AUTHORIZATION_HEADER_KEY = "Authorization";
    private static final String AUTHORIZATION_HEADER_PREFIX = "Basic ";

    @POST
    @Path("/logout")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response logout(@Context HttpHeaders headers)
    {
        List<String> authHeader = headers.getRequestHeaders().get(AUTHORIZATION_HEADER_KEY);
        String authToken = authHeader.get(0);
        authToken = authToken.replace(AUTHORIZATION_HEADER_PREFIX, "");
        String decoded = Base64.decodeAsString(authToken);
        StringTokenizer tokenizer = new StringTokenizer(decoded, ":");
        String userId = tokenizer.nextToken();
        String token = tokenizer.nextToken();
        long id = Long.valueOf(userId);
        authService.deleteSecureToken(id, token);
        return Response.ok("You have been logged out").build();
    }
}