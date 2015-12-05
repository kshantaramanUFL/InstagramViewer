package com.ebay.kshantaraman.instagramviewer;

/**
 * Created by kshantaraman on 12/1/15.
 */
public class ImageDisplay {


    private String imageURL;
    private String username;
    private String caption;

    public ImageDisplay(){}
    //String imageURL
    public ImageDisplay(String imageURL,int resourceID, String username, String caption) {

        this.username = username;
        this.caption = caption;
        this.imageURL=imageURL;
    }

    public String getImageUrl(){
        return imageURL;
    }

    public void setImageURL(String imageURL){
        this.imageURL=imageURL;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
}
