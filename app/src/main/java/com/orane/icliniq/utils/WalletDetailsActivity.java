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
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.orane.icliniq.Model.Model;
import com.orane.icliniq.R;
import com.orane.icliniq.utils.adapters.WalletAdapter;
import com.orane.icliniq.utils.walletModel.WalletAmountModel;
import com.orane.icliniq.utils.walletModel.WalletDetailsModel;

import java.util.ArrayList;

public class WalletDetailsActivity extends AppCompatActivity {
    TextView txt_wallet,txt_referal;
    RecyclerView recyclerView;
    ArrayList<WalletDetailsModel>walletList=new ArrayList<>();
    Button btn_transaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        txt_referal=findViewById(R.id.txt_referal);
        txt_wallet=findViewById(R.id.txt_wallet);
        recyclerView=findViewById(R.id.recyclerView);
        btn_transaction=findViewById(R.id.btn_transaction);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
        btn_transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(WalletDetailsActivity.this,ViewAllTransactions.class);
                startActivity(intent);
            }
        });
        getWalletAmount();
        getLastTransactionDetails();

    }

    private void getLastTransactionDetails() {
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
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
                WalletAdapter adapter = new WalletAdapter(walletList);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(WalletDetailsActivity.this));
                recyclerView.setAdapter(adapter);
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

    private void getWalletAmount() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Model.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitService service = retrofit.create(RetrofitService.class);
        service.walletDetails(Model.id,"android ",Model.token).enqueue(new Callback<WalletAmountModel>() {
            @Override
            public void onResponse(Call<WalletAmountModel> call, Response<WalletAmountModel> response) {
              if (response.code()==200){
                txt_wallet.setText(response.body().getWalletAmount());
               txt_referal.setText(response.body().getRefWalletAmount());

              }

            }

            @Override
            public void onFailure(Call<WalletAmountModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}