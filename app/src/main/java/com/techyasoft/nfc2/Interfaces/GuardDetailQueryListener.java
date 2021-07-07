package com.techyasoft.nfc2.Interfaces;

import com.techyasoft.nfc2.model.GuardDetail;
import com.techyasoft.nfc2.model.GuardTour;

public interface GuardDetailQueryListener {
    void OnQuerySuccess(GuardDetail guardDetail);
    void onNotFound();
    void OnQueryFail(String msg);
}
