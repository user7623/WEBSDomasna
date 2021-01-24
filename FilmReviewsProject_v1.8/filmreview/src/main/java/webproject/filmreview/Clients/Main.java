package webproject.filmreview.Clients;

import javax.xml.ws.Endpoint;

public class Main {
    
    public static void main(String[] args) {
        Endpoint.publish("http://localhost:8080/ws/seriesSOAP", new SOAPSeriesClient());
        Endpoint.publish("http://localhost:8080/ws/moviesSOAP", new SOAPMoviesClient());
    }



}