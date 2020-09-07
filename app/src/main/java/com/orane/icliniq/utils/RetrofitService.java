package com.orane.icliniq.utils;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitService {
  @GET("users/{user}/repos")
  Call<ResponseBody>addressCall();
}