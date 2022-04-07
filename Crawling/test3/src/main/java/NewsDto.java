public class NewsDto {
    private String image;
    private String title;
    private String press;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getPress() {
        return press;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPress(String press) {
        this.press = press;
    }

    @Override
    public String toString() {
        return "NewsDto{" +
                "image='" + image + '\'' +
                ", title='" + title + '\'' +
                ", press='" + press + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
