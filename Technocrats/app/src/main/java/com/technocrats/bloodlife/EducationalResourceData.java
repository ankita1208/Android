package com.technocrats.bloodlife;

public class EducationalResourceData {
    private String title;
    private String description;
    private String link;

    public EducationalResourceData(String title, String description, String link) {
        this.title = title;
        this.description = description;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }
}
