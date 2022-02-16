package PictureJSON;

import lombok.Data;

@Data
public class PictureModel
{
    private long id;
    private String pageURL;
    private String type;
    private String tags;
    private String previewURL;
    private int previewWidth;
    private int previewHeight;
    private String webformatURL;
    private int webformatWidth;
    private int webformatHeight;
    private String largeImageURL;
    private int imageWidth;
    private int imageHeight;
    private long imageSize;
    private long views;
    private long downloads;
    private long favorites;
    private long likes;
    private long comments;
    private long user_id;
    private String user;
    private String userImageURL;
}
