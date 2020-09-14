package com.orane.icliniq.utils.walletModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WalletDetailsModel {

@SerializedName("id")
@Expose
private String id;
@SerializedName("date")
@Expose
private String date;
@SerializedName("description")
@Expose
private String description;
@SerializedName("invoiceUrl")
@Expose
private String invoiceUrl;
@SerializedName("amount")
@Expose
private String amount;

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public String getDate() {
return date;
}

public void setDate(String date) {
this.date = date;
}

public String getDescription() {
return description;
}

public void setDescription(String description) {
this.description = description;
}

public String getInvoiceUrl() {
return invoiceUrl;
}

public void setInvoiceUrl(String invoiceUrl) {
this.invoiceUrl = invoiceUrl;
}

public String getAmount() {
return amount;
}

public void setAmount(String amount) {
this.amount = amount;
}

}