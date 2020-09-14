package com.orane.icliniq.utils;

import com.orane.icliniq.utils.walletModel.SearchDoctorModel;
import com.orane.icliniq.utils.walletModel.WalletAmountModel;
import com.orane.icliniq.utils.walletModel.WalletDetailsModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitService {
  @GET("users/{user}/repos")

  Call<ResponseBody>addressCall();


//  https://sriramakrishnahospital.icliniq.com/sapp/patientTransactionsDet?
  // user_id=123092&os_type=ios&token=51ae70519fa9b8b15b8df810fe2089de-d7afde3e7059cd0a0fe09eec4b0008cd&page=1

  @GET("sapp/patientTransactionsDet?")
  Call<ArrayList<WalletDetailsModel>>patientTransactionDetails(@Query("user_id") String user_id, @Query("os_type") String os_type, @Query("token") String token, @Query("page")int page);

  @GET("sapp/patientWalletDet?")
  Call<WalletAmountModel>walletDetails(@Query("user_id") String user_id,@Query("os_type") String os_type,@Query("token") String token);

//"?user_id=" + (Model.id) + "&page=1&sp_id=" + Model.select_spec_val + "&token=" + Model.token + "&enc=1"+"doc_name"+name;

  @GET("sapp/doctors?")
  Call<List<SearchDoctorModel>>searchDoctorDetails(@Query("user_id") String user_id, @Query("page") String page, @Query("sp_id")String selectspl, @Query("token") String token, @Query("enc") String enc, @Query("doc_name") String doc_name);


  @GET("sapp/getAllSpecialities?")
  Call<JSONObject>specialistDetails(@Query("user_id") String user_id, @Query("token") String token);


}

//https://sriramakrishnahospital.icliniq.com/sapp/patientWalletDet?user_id=123092&os_type=ios&token=51ae70519fa9b8b15b8df810fe2089de-d7afde3e7059cd0a0fe09eec4b0008cd