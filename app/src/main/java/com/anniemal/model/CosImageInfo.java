package com.anniemal.model;

/**
 * Created by dell on 2015/8/19.
 */
public class CosImageInfo {

    private String cosTitle;
    private String updateTime;
    private String cosImage;
    private String imagePath;

    public String getCosImage() {
        return cosImage;
    }

    public void setCosImage(String cosImage) {
        this.cosImage = cosImage;
    }

    public String getCosTitle() {
        return cosTitle;
    }

    public void setCosTitle(String cosTitle) {
        this.cosTitle = cosTitle;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
