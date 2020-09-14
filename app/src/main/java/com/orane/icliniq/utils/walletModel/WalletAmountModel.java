package com.orane.icliniq.utils.walletModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WalletAmountModel {

@SerializedName("walletAmount")
@Expose
private String walletAmount;
@SerializedName("refWalletAmount")
@Expose
private String refWalletAmount;

public String getWalletAmount() {
return walletAmount;
}

public void setWalletAmount(String walletAmount) {
this.walletAmount = walletAmount;
}

public String getRefWalletAmount() {
return refWalletAmount;
}

public void setRefWalletAmount(String refWalletAmount) {
this.refWalletAmount = refWalletAmount;
}

}