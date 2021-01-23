package webproject.filmreview.Clients;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.jws.WebMethod;
import javax.jws.WebService;

//import java.io.File;
/*
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.FileRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import webproject.filmreview.Models.Movie;
import webproject.filmreview.Models.ResponseModel;
import webproject.filmreview.Utilities.Database;

@WebService
public class SOAPMoviesClient {
    
    @WebMethod
    public String doGetAllMoviesSOAP()
    {
        
        Map<Long , Movie> seriesDictionary = Database.getAllMovies();
        
        String allMoviesString = "";

        for (Map.Entry<Long, Movie> entry : seriesDictionary.entrySet()) {
            //Long mLong = entry.getKey();
            Movie mMovie = entry.getValue();
            allMoviesString.concat("Name: ");
            allMoviesString.concat(mMovie.getName());
            allMoviesString.concat("\n");
            allMoviesString.concat("Main actors: ");
            allMoviesString.concat(mMovie.getMainActors());
            allMoviesString.concat("\n");
            allMoviesString.concat("Rating: ");
            allMoviesString.concat(Float.toString(mMovie.getRating()));
            allMoviesString.concat("\n");
            allMoviesString.concat("Description: ");
            allMoviesString.concat(mMovie.getDescription());      
            allMoviesString.concat("\n");
       
        }
        return "Movies on database: " + allMoviesString;
    }


    @WebMethod
    public String doGetAMovieByNameSOAP(String nameOfRequestedMovie)
    {
        
        Map<Long , Movie> seriesDictionary = Database.getAllMovies();
        
        String allMoviesString = "";

        for (Map.Entry<Long, Movie> entry : seriesDictionary.entrySet()) {
            Movie mMovie = entry.getValue();
            if (mMovie.getName().contains(nameOfRequestedMovie))
            {
                allMoviesString.concat("Name: ");
                allMoviesString.concat(mMovie.getName());
                allMoviesString.concat("\n");
                allMoviesString.concat("Main actors: ");
                allMoviesString.concat(mMovie.getMainActors());
                allMoviesString.concat("\n");
                allMoviesString.concat("Rating: ");
                allMoviesString.concat(Float.toString(mMovie.getRating()));
                allMoviesString.concat("\n");
                allMoviesString.concat("Description: ");
                allMoviesString.concat(mMovie.getDescription());      
                allMoviesString.concat("\n");
            }
        }
        return "Movies that contain" + nameOfRequestedMovie + " in their title: \n";
    }


    @WebMethod
    public String doGetAMovieByIdSOAP(Long movieId)
    {
        
        Map<Long , Movie> seriesDictionary = Database.getAllMovies();
        
        String allMoviesString = "";

        for (Map.Entry<Long, Movie> entry : seriesDictionary.entrySet()) {
            Movie mMovie = entry.getValue();
            if (mMovie.getId() == movieId)
            {
                allMoviesString.concat("Name: ");
                allMoviesString.concat(mMovie.getName());
                allMoviesString.concat("\n");
                allMoviesString.concat("Main actors: ");
                allMoviesString.concat(mMovie.getMainActors());
                allMoviesString.concat("\n");
                allMoviesString.concat("Rating: ");
                allMoviesString.concat(Float.toString(mMovie.getRating()));
                allMoviesString.concat("\n");
                allMoviesString.concat("Description: ");
                allMoviesString.concat(mMovie.getDescription());      
                allMoviesString.concat("\n");
            }
        }
        return "Basic information aboit the movie requested by Movieid: \n " + allMoviesString;
    }

    public void geHelloAge(String clientName, Long movieId) {
        String wsURL = "http://localhost:8080/ws/moviesSOAP";
        URL url = null;
        URLConnection connection = null;
        HttpURLConnection httpConn = null;
        String responseString = null;
        String outputString="";
        OutputStream out = null;
        InputStreamReader isr = null;
        BufferedReader in = null;
        
        String movieIdString = Long.toString(movieId);

        String xmlInput =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:hel=\"http://helloage.app.web.softwarepulse/\">" +
                        "<soapenv:Header/>" +
                        "<soapenv:Body>" +
                           "<hel:sayhello>" +
                              "<!--Optional:-->" +
                              "<arg0>" + clientName + "</arg0>" +
                              "<arg1>" + movieIdString + "</arg1>" +
                           "</hel:sayhello>" +
                        "</soapenv:Body>" +
                     "</soapenv:Envelope>";
         
        try
        {
            url = new URL(wsURL);
            connection = url.openConnection();
            httpConn = (HttpURLConnection) connection;
 
            byte[] buffer = new byte[xmlInput.length()];
            buffer = xmlInput.getBytes();
 
            String SOAPAction = "";
            // Set the appropriate HTTP parameters.
             httpConn.setRequestProperty("Content-Length", String
                     .valueOf(buffer.length));
            httpConn.setRequestProperty("Content-Type",
                    "text/xml; charset=utf-8");
             
             
            httpConn.setRequestProperty("SOAPAction", SOAPAction);
            httpConn.setRequestMethod("POST");
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            out = httpConn.getOutputStream();
            out.write(buffer);
            out.close();
             
            // Read the response and write it to standard out.
            isr = new InputStreamReader(httpConn.getInputStream());
            in = new BufferedReader(isr);
             
            while ((responseString = in.readLine()) != null) 
            {
                outputString = outputString + responseString;
            }
            System.out.println(outputString);
            System.out.println("");
             
            // Get the response from the web service call
            Document document = parseXmlFile(outputString);
             
            NodeList nodeLst = document.getElementsByTagName("ns2:sayhelloResponse");
            if (nodeLst.getLength() > 0)
            {
            String webServiceResponse = nodeLst.item(0).getTextContent();
            System.out.println("The response from the web service call is : " + webServiceResponse);
            } else {
            System.out.println("The response from the web service call is : empty");
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
     
    private Document parseXmlFile(String in) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
             InputSource is = new InputSource(new StringReader(in));
            return db.parse(is);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }





    /*
    public void callWebServiceSOAP(String soapAction, String soapEnvBody)  throws IOException {
        // Create a StringEntity for the SOAP XML.
        String body ="<?xml version=\"1.0\" encoding=\"UTF-8\"?><SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns1=\"http://example.com/v1.0/Records\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><SOAP-ENV:Body>"+soapEnvBody+"</SOAP-ENV:Body></SOAP-ENV:Envelope>";
        StringEntity stringEntity = new StringEntity(body, "UTF-8");
        stringEntity.setChunked(true);
    
        // Request parameters and other properties.
        HttpPost httpPost = new HttpPost("http://example.com?soapservice");
        httpPost.setEntity(stringEntity);
        httpPost.addHeader("Accept", "text/xml");
        httpPost.addHeader("SOAPAction", soapAction);
    
        // Execute and get the response.
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();
    
        String strResponse = null;
        if (entity != null) {
            strResponse = EntityUtils.toString(entity);
        }
    }*/


}