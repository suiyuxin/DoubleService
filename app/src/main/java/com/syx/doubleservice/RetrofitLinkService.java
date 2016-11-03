package com.syx.doubleservice;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Administrator on 2016/7/20.
 */
public interface RetrofitLinkService {

    @GET("pushdata")
    Call<ResponseBody> GetPullData();

}
