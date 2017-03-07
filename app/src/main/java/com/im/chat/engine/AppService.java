//package com.im.chat.engine;
//
//import com.imib.cctv.bean.CommonRequestBean;
//import com.imib.cctv.bean.CommonResponseBean;
//import com.imib.cctv.bean.ConfigMenuRequestBean;
//import com.imib.cctv.bean.ConfigMenuResponse;
//import com.imib.cctv.bean.FavorCheckResponseBean;
//import com.imib.cctv.bean.FavorFullRequestBean;
//import com.imib.cctv.bean.FavoriteFullListResponseBean;
//import com.imib.cctv.bean.FavoriteListRequestBean;
//import com.imib.cctv.bean.FavoriteListResponseBean;
//import com.imib.cctv.bean.HomeSectionBean;
//import com.imib.cctv.bean.LoginInBean;
//import com.imib.cctv.bean.LoginInResponseBean;
//import com.imib.cctv.bean.RecommendNewsBean;
//import com.imib.cctv.bean.RecommendNewsRequestBean;
//import com.imib.cctv.bean.UpdateRequestBean;
//import com.imib.cctv.bean.UpdateResponseBean;
//import com.imib.cctv.bean.dataBean.NewsListBean;
//import com.imib.cctv.bean.dataBean.SearchListResponseBean;
//import com.imib.cctv.bean.newslist.NewsListQuery;
//import com.imib.cctv.bean.search.SearchListRequestBean;
//
//import okhttp3.ResponseBody;
//import retrofit2.http.Body;
//import retrofit2.http.GET;
//import retrofit2.http.POST;
//import retrofit2.http.Query;
//import retrofit2.http.Streaming;
//import retrofit2.http.Url;
//import rx.Observable;
//
///**
// * Created by lzb on 16/9/1.
// */
//public interface AppService {
//
//    @POST("login/in")
//    Observable<LoginInResponseBean> login(@Body LoginInBean loginInBean);
//
//    /**
//     * 加载有序新闻列表
//     *
//     * @param query
//     * @return
//     */
//    @POST("news/list")
//    Observable<NewsListBean> loadNewsList(@Body NewsListQuery query);
//
//    /**
//     * 下载节目单
//     *
//     * @param fileUrl
//     * @return
//     */
//    @Streaming
//    @GET
//    Observable<ResponseBody> downProgram(@Url String fileUrl);
//
//    @POST("favorite/add")
//    Observable<CommonResponseBean> favorAdd(@Body CommonRequestBean commonRequestBean);
//
//    @POST("favorite/delete")
//    Observable<CommonResponseBean> favorDelete(@Body CommonRequestBean favoriteDeleteBean);
//
//    @POST("favorite/list")
//    Observable<FavoriteListResponseBean> favorList(@Body FavoriteListRequestBean favoriteListRequestBean);
//
//    @POST("like/do")
//    Observable<CommonResponseBean> likeDo(@Body CommonRequestBean likeDoBean);
//
//    @POST("favorite/check")
//    Observable<FavorCheckResponseBean> favorCheck(@Body CommonRequestBean favoriteDeleteBean);
//
//    @POST("config/menu")
//    Observable<ConfigMenuResponse> configMenu(@Body ConfigMenuRequestBean configMenuRequestBean);
//
//    @POST("favorite/fulllist")
//    Observable<FavoriteFullListResponseBean> favorFullList(@Body FavorFullRequestBean favorFullRequestBean);
//
//    @POST("search/list")
//    Observable<SearchListResponseBean> searchFullList(@Body SearchListRequestBean searchListRequestBean);
//
//
//    /**
//     * version update
//     * @param updateRequestBean
//     * @return
//     */
//    @POST("config/menu")
//    Observable<UpdateResponseBean> getUpdateInfo(@Body UpdateRequestBean updateRequestBean);
//
//    @POST("recommend/news/list")
//    Observable<RecommendNewsBean> getRecommendNewsList(@Body RecommendNewsRequestBean recommendNewsRequestBean);
//
//    @GET("config/info")
//    Observable<HomeSectionBean> getHomeSection(@Query("key") String section);
//
//}
