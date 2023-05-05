package in.fiberstory.tfsplaytv.network;

import in.fiberstory.tfsplaytv.model.PlexigoChannelCheckExpReqModel;
import in.fiberstory.tfsplaytv.model.PlexigoChannelReqModel;
import in.fiberstory.tfsplaytv.model.PlexigoDoRegisterRequestModel;
import in.fiberstory.tfsplaytv.model.PlexigoOnRentReqModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface APIInterface {

    //  prod
    String API_PAYMENT = "https://app.secure.tfsplay.in/payment/initiate_payment.php?";
    String API_DOMAIN = "https://app.api.tfsplay.in/";
    String AUTH_USER_NAME = "tfsprduser";
    String AUTH_PASSWORD = "hRgEcpSR7qMmkt2%=H";
    String xApiKey = "f367aa64f65911eaa3d10675b05e64d8";

    //    dev
//    String API_PAYMENT = "https://dev.tfsplay.in/version1/payment/initiate_payment.php?";
//    String API_DOMAIN = "https://api.dev.tfsplay.in/";
//    String AUTH_USER_NAME = "tfsuser";
//    String AUTH_PASSWORD = "QZk3wUxSpHBrr9HNVg";
//        String xApiKey = "03a53461dfdf11ea85d10a5cd31394da";


    String DOMAIN_URL = "https://userapi.plexigo.com/api/"; //prod
//    String DOMAIN_URL = "https://userqaapi.plexigo.com/api/"; // QA
String CONTENT_EXPIRY_BASE_URL = "https://billingcoreapi.plexigo.com/api/"; // prod
//    String CONTENT_EXPIRY_BASE_URL = "https://billingcoreqaapi.plexigo.com/api/"; // QA

    String API_SUBSCRIBER = "subscriber/";
    String API_OTT = "ott/";
    String API_SUPPORT = "support/";
    String API_PROMOTION = "promotion/";
    String API_HOME = "home/";
    String API_PAYMENT_API = "payment/";
    String GetContentToken = "content/getContentToken";
    String GetMovieDetail = "content/getmoviedetail";
    String CheckExpiry = "ValidateContent/CheckExpiry";
    String getOnRentChannelCategories = "content/ChannelWiseContentlisting";
    String getContentByChannel = "content/getcontentbyChannel";



    String checkUserExistOrNot = "user/CheckUserExists";
    String plexigoUserRegistration = "user/UserRegistration";
    String apiVersionDetail = "apk/";

    String youtubeBaseURI = "https://www.youtube.com/watch?v=";
    String youtubePlaylistGetContentBaseURL = "https://www.googleapis.com/youtube/v3/playlistItems";





    // Get Promotion
    @GET(API_DOMAIN + API_PROMOTION + "get_promotion.php?")
    Call<String> getPromotion(@Query("subscriber_id") String subscriber_id);

    // Get Home listing
    @GET(API_DOMAIN + API_HOME + "get_home_page_listing.php?")
    Call<String> getHomePageListing(@Query("subscriber_id") String subscriber_id);

    // Get channel list
    @GET(API_DOMAIN + API_OTT + "get_live_channels.php?")
    Call<String> getLiveTVChannelList(@Query("subscriber_id") String subscriber_id);

    // Get Documentary
    @GET(API_DOMAIN + API_OTT + "get_documentaries.php")
    Call<String> getDocumentaries();

    // Get Documentary with paginatation
    @GET(API_DOMAIN + API_OTT + "get_documentaries.php?")
    Call<String> getDocumentary(@Query("page") String page);

    // Get Short Film with paginatation
    @GET(API_DOMAIN + API_OTT + "get_short_films.php?")
    Call<String> getShortFilm(@Query("page") String page);

    //Get latest movie
    @GET(API_DOMAIN + API_OTT + "get_latest_movies.php?")
    Call<String> getLatestMovies(@Query("subscriber_id") String subscriber_id,
                                 @Query("page") String page);

    //Get featured movie
    @GET(API_DOMAIN + API_OTT + "get_featured_movies.php?")
    Call<String> getFeaturedMovies(@Query("subscriber_id") String subscriber_id,@Query("page") String page);

    //Get language movie with pagination
    @GET(API_DOMAIN + API_OTT + "get_movies.php?")
    Call<String> getMovie(@Query("language") String language,
                          @Query("page") String page,
                          @Query("subscriber_id") String subscriber_id);

    //Get language movie
    @GET(API_DOMAIN + API_OTT + "get_movies.php?")
    Call<String> getMovie(@Query("language") String language,
                          @Query("subscriber_id") String subscriber_id);

    //Get tv shows
    @GET(API_DOMAIN + API_OTT + "get_shows.php?")
    Call<String> getTVShows(@Query("category") String category,
                            @Query("subscriber_id") String subscriber_id);

    //Get tv shows with pagination
    @GET(API_DOMAIN + API_OTT + "get_shows.php?")
    Call<String> getTVShows(@Query("category") String category,
                            @Query("page") String page,
                            @Query("subscriber_id") String subscriber_id);

    //Get tv shows with pagination
    @GET(API_DOMAIN + API_OTT + "get_episodes.php?")
    Call<String> getEpisode(@Query("show_id") String show_id,
                            @Query("page") String page,
                            @Query("subscriber_id") String subscriber_id);

    // Get search
    @GET(API_DOMAIN + API_OTT + "global_search.php")
    Call<String> getSearch(@Query("searchstr") String searchstr);

    // User login
    @FormUrlEncoded
    @POST(API_DOMAIN + API_SUBSCRIBER + "mobile_login.php")
    Call<String> getLogin(@Field("login_id") String login_id,
                          @Field("country_code") String countryCode,
                          @Field("mobile_device_id") String mobile_device_id,
                          @Field("mobile_device_name") String mobile_device_name,
                          @Field("fcm_id") String fcm_id,
                          @Field("otp") String otp);

    // User login with OTP
    @FormUrlEncoded
    @POST(API_DOMAIN + API_SUBSCRIBER + "mobile_login.php")
    Call<String> getLoginOTP(@Field("login_id") String login_id,
                             @Field("country_code") String countryCode,
                             @Field("mobile_device_id") String mobile_device_id,
                             @Field("mobile_device_name") String mobile_device_name,
                             @Field("fcm_id") String fcm_id,
                             @Field("otp_no") String otp_no);

    // User logout
    @FormUrlEncoded
    @POST(API_DOMAIN + API_SUBSCRIBER + "mobile_logout.php")
    Call<String> getSelfLogout(@Field("self_id") String self_id,
                               @Field("self_mobile_id") String self_mobile_id,
                               @Field("self_mobile_name") String self_mobile_name,
                               @Field("self") String self);

    // Query submit
    @FormUrlEncoded
    @POST(API_DOMAIN + API_SUPPORT + "send_query.php")
    Call<String> addSendQuery(@Field("subscriber_id") String subscriber_id,
                              @Field("support_type") String support_type,
                              @Field("description") String description);


    @GET(DOMAIN_URL + GetContentToken + "/{contentId}/{userId}")
    Call<String> getContentTokenAPI(@Path("contentId") String cId,
                                    @Path("userId") int uId);

    @GET(DOMAIN_URL + GetMovieDetail + "/{movieId}/{userId}")
    Call<String> getMovieDetailAPI(@Path("movieId") int mId,
                                                     @Path("userId") int uId);

    @POST(CONTENT_EXPIRY_BASE_URL + CheckExpiry)
    Call<String> checkExpiry(@Body PlexigoChannelCheckExpReqModel plexigoChannelReqModel);


    @POST(DOMAIN_URL + checkUserExistOrNot)
    Call<String> checkUserExistOrNot(@Body PlexigoChannelReqModel plexigoChannelReqModel);



    @POST(DOMAIN_URL + plexigoUserRegistration)
    Call<String> doPlexigoUserRegistration(@Body PlexigoDoRegisterRequestModel plexigoDoRegisterRequestModel);



    //getAppVersion
    @GET(API_DOMAIN + apiVersionDetail + "check_tfs_app_updates.php")
    Call<String> getAppVersion();

    @POST(DOMAIN_URL + getOnRentChannelCategories)
    Call<String> getOnRentChannelCategoryListing(@Body PlexigoOnRentReqModel plexigoOnRentReqModel);

    @GET(DOMAIN_URL + getContentByChannel + "/{channelId}/{pageNo}/{NoOfRecord}/{userID}")
    Call<String> getContentByChannelAPI(@Path("channelId") int cID,
                                        @Path("pageNo") int pNo,
                                        @Path("NoOfRecord") int noOfrecord,
                                        @Path("userID") int userId);



    @GET
    Call<String>
    getYoutubePlayListVideoItems(
            @Url String youtubeurl ,
            @Query("part") String part,
            @Query("playlistId") String playlistId,
            @Query("order") String order,
            @Query("maxResults") Integer maxResults,
            @Query("pageToken") String pageToken,
            @Query("key") String apikey
    ) ;

}
