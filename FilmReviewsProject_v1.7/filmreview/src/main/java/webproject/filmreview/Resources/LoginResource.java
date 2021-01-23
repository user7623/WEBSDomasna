package webproject.filmreview.Resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import webproject.filmreview.Models.RegisterModel;
import webproject.filmreview.Models.User;
import webproject.filmreview.Utilities.AuthenticationService;

@Path("/login")
public class LoginResource 
{
    private AuthenticationService authService = new AuthenticationService();

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response loginUser(RegisterModel model)
    {
        User user = authService.authenticate(model.getUsername(), model.getPassword());
        if(user == null)
        {
            return Response.status(Response.Status.BAD_REQUEST).entity("Error, while logging in, username and/or password is incorrect")
            .build();
        }
        else
        {
            String token = authService.issueSecureToken(user);
            return Response.accepted().entity(token).build();
        }
    }
}