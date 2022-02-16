package PictureJSON;

import lombok.Data;

@Data
public class PicJSONModel
{
    long total;
    long totalHits;
    PictureModel[] hits;
}
