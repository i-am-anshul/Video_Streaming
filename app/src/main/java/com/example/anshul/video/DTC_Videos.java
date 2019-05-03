package com.example.anshul.video;

/**
 * Created by Anshul on 1/22/2018.
 */

public class DTC_Videos {
    private String name, file, thumb, link;
    int count, id, length;
    float complete;

    public DTC_Videos() {
        complete = 0;
        length = 0;
    }

    public DTC_Videos(String link, String thumb, String file, String name, int count, int id) {
        this.link = link;
        this.thumb = thumb;
        this.name = name;
        this.file = file;
        this.count = count;
        this.id = id;
        complete = 0;
        length = 0;
    }

    public DTC_Videos(String link, String thumb, String file, String name, int count, int id, float complete) {

        this.link = link;
        this.thumb = thumb;
        this.file = file;
        this.name = name;
        this.count = count;
        this.id = id;
        this.complete = complete;
        length = 0;
    }


    public DTC_Videos(String link, String thumb, String file, String name, int count, int id, float complete, int length) {

        this.link = link;
        this.thumb = thumb;
        this.file = file;
        this.name = name;
        this.count = count;
        this.id = id;
        this.complete = complete;

        this.length = length;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public float getComplete() {
        return complete;
    }

    public void setComplete(float complete) {
        this.complete = complete;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
