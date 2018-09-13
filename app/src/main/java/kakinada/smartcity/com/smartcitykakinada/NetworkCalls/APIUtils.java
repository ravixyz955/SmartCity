package kakinada.smartcity.com.smartcitykakinada.NetworkCalls;

public class APIUtils {

    private APIUtils() {
    }

    //    public static final String BASE_URL = "http://demo7202769.mockable.io/";
    public static final String BASE_URL = "http://demo7202769.mockable.io/";

    public static APIService getAPIService() {
        return APIClient.getClient(BASE_URL).create(APIService.class);
    }
}