package com.xforg.gank2020.mvp.model.api.service;

import com.xforg.gank2020.beans.GanHuoList;
import com.xforg.gank2020.mvp.model.entity.User;
import java.util.List;
import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * ================================================
 * 展示 {@link Retrofit#create(Class)} 中需要传入的 ApiService 的使用方式
 * 存放关于用户的一些 API
 * ================================================
 */
public interface UserService {
    String HEADER_API_VERSION = "Accept: application/vnd.github.v3+json";

    @Headers({HEADER_API_VERSION})
    @GET("/users")
    Observable<List<User>> getUsers(@Query("per_page") int perPage);

    @GET("data/{type}/{pageSize}/{page}")
    Observable<GanHuoList> getGanHuoList(@Path("type") String type, @Path("pageSize") int pageSize,@Path("page") int page);
}
