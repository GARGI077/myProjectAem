package com.myproject.aem.core.models;

public class SearchResultItem {

    String title;
    String description;
    String pageUrl;

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

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    @Override
    public String toString() {
        return "SearchResultItem [title=" + title + ", description=" + description + ", pageUrl=" + pageUrl + "]";
    }

}

