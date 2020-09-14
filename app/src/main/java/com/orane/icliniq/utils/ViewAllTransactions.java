package com.orane.icliniq.utils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.orane.icliniq.Model.Model;
import com.orane.icliniq.R;
import com.orane.icliniq.utils.adapters.WalletAdapter;
import com.orane.icliniq.utils.walletModel.WalletDetailsModel;

import java.util.ArrayList;

public class ViewAllTransactions extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView noData;
    ArrayList<WalletDetailsModel> walletList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_transactions);
        Toolbar toolbar = findViewById(R.id.toolbar);
        recyclerView=findViewById(R.id.recyclerView);
        noData=findViewById(R.id.noData);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
        getLastTransactionDetails();
    }
    private void getLastTransactionDetails() {
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Model.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitService service = retrofit.create(RetrofitService.class);
        service.patientTransactionDetails(Model.id,"android ",Model.token,1).enqueue(new Callback<ArrayList<WalletDetailsModel>>() {
            @Override
            public void onResponse(Call<ArrayList<WalletDetailsModel>> call, Response<ArrayList<WalletDetailsModel>> response) {
                progressDialog.dismiss();
                if (response.code()==200){
                    progressDialog.dismiss();
                    if (response.body() != null) {
                        walletList.addAll(response.body());
                    }
                    if (walletList!=null && walletList.size()>0) {
                        noData.setVisibility(View.GONE);
                        WalletAdapter adapter = new WalletAdapter(walletList);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(ViewAllTransactions.this));
                        recyclerView.setAdapter(adapter);
                    }else{
                        noData.setVisibility(View.VISIBLE);
                    }
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ArrayList<WalletDetailsModel>> call, Throwable t) {
                progressDialog.dismiss();
                t.printStackTrace();

            }
        });
    }

}