package com.techyasoft.nfc2.Interfaces;


import com.techyasoft.nfc2.model.CardData;

public interface CheckCardNumerQueryListener {
    void onFound(CardData data);
    void onNotFound();
    void onFailed(String msg);
}
