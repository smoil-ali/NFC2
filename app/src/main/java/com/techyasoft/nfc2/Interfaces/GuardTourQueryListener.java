package com.techyasoft.nfc2.Interfaces;

import com.techyasoft.nfc2.model.GuardTour;
import com.techyasoft.nfc2.model.Profile;

public interface GuardTourQueryListener {
    void OnQuerySuccess(GuardTour guardTour);
    void onNotFound();
    void OnQueryFail(String msg);
}
