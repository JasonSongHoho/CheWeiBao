package cheweibao.jason.com.carPlace.Bean;


import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobGeoPoint;

/**
 * Created by JasonSong on 2017/4/18.
 */

public class carPlace extends BmobObject {
    private String name;
    private String user;
    private String owner;
    private String address;
    private String price;
    private Boolean available;
    private BmobGeoPoint geoPoint;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public BmobGeoPoint getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(BmobGeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
