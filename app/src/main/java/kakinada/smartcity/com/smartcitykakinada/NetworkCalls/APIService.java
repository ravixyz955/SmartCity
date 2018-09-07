package kakinada.smartcity.com.smartcitykakinada.NetworkCalls;


import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by code1 on 3/7/2018.
 */

public interface APIService {

    @GET("discover")
    Call<Object> discover();

    @GET("dashboard")
    Call<Object> dashboard();

    @GET("govtservicelist")
    Call<Object> govtserviceList();


    @GET("discoverlist")
    Call<Object> discoverList();

    @GET("smartcitylist")
    Call<Object> smartCityList();
}