package cf.androidprojects.onlinediary;

public class Diary {

    Diary()
    {

    }

    public Diary(String title, String description, String date,String id) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.id = id;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    String title;
    String description;
    String date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;

}
