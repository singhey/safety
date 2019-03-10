package com.singhey.womenux.sqlite.model;

/**
 * Created by singh on 24-03-2018.
 */

public class Story {

    private String heading;
    private String name;
    private String story;
    private String imageUrl;
    private int id;
    private int readCount;

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }



    public Story(String heading, String name, String story, String imageUrl, int id, int readCount) {
        this.heading = heading;
        this.name = name;
        this.story = story;
        this.imageUrl = imageUrl;
        this.id = id;
        this.readCount = readCount;
    }

    @Override
    public String toString() {
        return "StoryActivity{" +
                "heading='" + heading + '\'' +
                ", name='" + name + '\'' +
                ", story='" + story + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", id=" + id +
                ", readCount=" + readCount +
                '}';
    }

    public Story() {

    }
}
