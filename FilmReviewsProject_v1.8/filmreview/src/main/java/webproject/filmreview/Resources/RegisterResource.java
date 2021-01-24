package webproject.filmreview.Resources;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import webproject.filmreview.Models.RegisterModel;
import webproject.filmreview.Models.User;
import webproject.filmreview.Utilities.UserService;

@Path("/register")
public class RegisterResource 
{
    private UserService usersService = new UserService();

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerUser(RegisterModel model, @Context UriInfo uriInfo)
    {
        User user = usersService.addUser(model.getUsername(), model.getPassword());
        if(user == null)
        {
            return Response.status(Response.Status.BAD_REQUEST).entity("Error, creating a new account, username: " + 
            model.getUsername() + " is already taken.").build();
        }
        else
        {
            URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(user.getId())).build();
            return Response.created(uri).build();
        }
    }
}