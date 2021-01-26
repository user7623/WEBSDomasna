package webproject.filmreview.Models;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ErrorMessage implements Serializable
{
    
    private static final long serialVersionUID = 335203307114980010L;
    private String errorMessage;
    private int errorCode;
    private String documentation;

    public ErrorMessage() {
    }

    public ErrorMessage(String errorMessage, int errorCode, String documentation) {
        super();
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
        this.documentation = documentation;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getDocumentation() {
        return this.documentation;
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

}