package dalia.daniele.telegram.taro_web_app.domain.card;

public class OnServerPicture extends CardPicture {
    private final String pictureUrl;

    public OnServerPicture(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }
}
