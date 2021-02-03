package PictureJSON;

public class PicJSON
{
    long total;
    long totalHits;
    Picture hits[];

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(long totalHits) {
        this.totalHits = totalHits;
    }

    public Picture[] getHits() {
        return hits;
    }

    public void setHits(Picture[] hits) {
        this.hits = hits;
    }
}
