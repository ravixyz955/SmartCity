package kakinada.smartcity.com.smartcitykakinada.googlemaps;

/**
 * Created by anupamchugh on 29/03/17.
 */

public class StoreModel {


    public String name, address, distance, duration;

    public StoreModel(String name, String address, String distance, String duration) {

        this.name = name;
        this.address = address;
        this.distance = distance;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
