package app.patuhmobile.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Fahmi Hakim on 25/07/2018.
 * for SERA
 */

public class Artikel implements Serializable{

    private Integer Id;
    private String Category;
    private String Title;
    private String Story;
    private String cCreated;
    private String GPSLocation;
    private double GPSLong;
    private double GPSLat;
    private List<String> ImageIds = null;
    private final static long serialVersionUID = -6562451817571483166L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Artikel() {
    }

    public Artikel(Integer id, String category, String title, String story, String cCreated, String GPSLocation, double GPSLong, double GPSLat, List<String> imageIds) {
        this.Id = id;
        this.Category = category;
        this.Title = title;
        this.Story = story;
        this.cCreated = cCreated;
        this.GPSLocation = GPSLocation;
        this.GPSLong = GPSLong;
        this.GPSLat = GPSLat;
        ImageIds = imageIds;
    }

    public String getcCreated() {
        return cCreated;
    }

    /**
     *
     * @param GPSLocation
     * @param id
     * @param story
     * @param GPSLong
     * @param title
     * @param category
     * @param imageIds
     * @param GPSLat
     */
    /*public Artikel(Integer id, String category, String title, String story, String GPSLocation, Integer GPSLong, Integer GPSLat, List<String> imageIds) {
        super();
        this.Id = id;
        this.Category = category;
        this.Title = title;
        this.Story = story;
        this.GPSLocation = GPSLocation;
        this.GPSLong = GPSLong;
        this.GPSLat = GPSLat;
        this.ImageIds = imageIds;
    }*/





    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        this.Id = id;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        this.Category = category;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public String getStory() {
        return Story;
    }

    public void setStory(String story) {
        this.Story = story;
    }

    public String getGPSLocation() {
        return GPSLocation;
    }

    public void setGPSLocation(String gPSLocation) {
        this.GPSLocation = gPSLocation;
    }

    public double getGPSLong() {
        return GPSLong;
    }

    public void setGPSLong(double gPSLong) {
        this.GPSLong = gPSLong;
    }

    public double getGPSLat() {
        return GPSLat;
    }

    public void setGPSLat(double gPSLat) {
        this.GPSLat = gPSLat;
    }

    public List<String> getImageIds() {
        return ImageIds;
    }

    public void setImageIds(List<String> imageIds) {
        this.ImageIds = imageIds;
    }

}
