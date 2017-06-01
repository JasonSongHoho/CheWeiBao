package cheweibao.jason.com.carPlace.Bean;






import java.util.List;
import cn.bmob.v3.BmobObject;

/**
 * Created by JasonSong on 2017/4/18.
 */

public class user extends BmobObject {
    private String name;
    private String password;
    private String nickname;
    private String carNum;
    private String sex;
    private String phone;
    private String city;
    private List<String> havePlaces;
    private List<String> usePlaces;
    private Boolean havePlace;
    private Boolean usePlace;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public List getHavePlaces() {
        return havePlaces;
    }

    public void setHavePlaces(List havePlaces) {
        this.havePlaces = havePlaces;
    }

    public List getUsePlaces() {
        return usePlaces;
    }

    public void setUsePlaces(List usePlaces) {
        this.usePlaces = usePlaces;
    }

    public Boolean getHavePlace() {
        return havePlace;
    }

    public void setHavePlace(Boolean havePlace) {
        this.havePlace = havePlace;
    }

    public Boolean getUsePlace() {
        return usePlace;
    }

    public void setUsePlace(Boolean usePlace) {
        this.usePlace = usePlace;
    }

}
