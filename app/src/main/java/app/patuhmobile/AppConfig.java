package app.patuhmobile;



public class AppConfig {
    public static final String FRAG_NAV_DETAIL_CONTENT = "Content";
    public static final String FRAG_NAV_BTM_HOME = "Home";
    public static final String FRAG_NAV_BTM_UJIAN = "Ujian";
    public static final String FRAG_NAV_BTM_KUPON = "Kupon";
    public static final String FRAG_NAV_BTM_NOTIF = "Notif";
    public static final String FRAG_NAV_SPG_PROFILE = "Profile";
    public static final String FRAG_NAV_SPG_SETTING = "SETTING";

    //- NGROK
    public static final String API_BASE_URL = "https://patuhapp.azurewebsites.net/api/";

    //- Prod
    //public static final String API_BASE_URL = "http://patuhapidev-env.fmzqmjeba2.ap-southeast-1.elasticbeanstalk.com/api/";
    public static final String API_RESOURCE_URL = "http://149.129.218.94/api/resources/";
    public static final String USER_AUTH_INFO = "User Auth Info";

    //- NGROK
    private String apiBaseUrl = "https://patuhapp.azurewebsites.net/api/"; //http://patuhapidev-env.fmzqmjeba2.ap-southeast-1.elasticbeanstalk.com/api/";

    //- Prod
    //private String apiBaseUrl = "http://patuhapidev-env.fmzqmjeba2.ap-southeast-1.elasticbeanstalk.com/api/"; //http://patuhapidev-env.fmzqmjeba2.ap-southeast-1.elasticbeanstalk.com/api/";

    private int appTheme = R.style.AppTheme;

    public String getApiBaseUrl() {
        return this.apiBaseUrl;
    }

    public void setApiBaseUrl(String apiBaseUrl) {
        this.apiBaseUrl = apiBaseUrl;
    }

    public String getApiResourceUrl() {
        return apiBaseUrl + "resources/";
    }

    public int getAppTheme() {
        return appTheme;
    }

    public void setAppTheme(int appTheme) {
        this.appTheme = appTheme;
    }
}