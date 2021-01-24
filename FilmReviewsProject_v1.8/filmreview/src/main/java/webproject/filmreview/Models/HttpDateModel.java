package webproject.filmreview.Models;

public class HttpDateModel 
{
    private String finishDate;

    public HttpDateModel() {
    }

    public HttpDateModel(String finishDate) {
        this.finishDate = finishDate;
    }

    public String getFinishDate() {
        return this.finishDate;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }

}