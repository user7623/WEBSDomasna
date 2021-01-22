package webproject.filmreview.Resources;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import webproject.filmreview.Models.ErrorMessage;

@Provider
public class DateFormatExceptionMapper implements ExceptionMapper<DateFormatException> {

    @Override
    public Response toResponse(DateFormatException exception) {
        ErrorMessage message = new ErrorMessage(exception.getMessage(), 400, "Please provide a date in the dd/MM/yyyy format.");
        return Response.status(Status.BAD_REQUEST).entity(message)
        .build();
    }
    
}