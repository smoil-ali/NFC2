package com.techyasoft.nfc2.Interfaces;

import com.techyasoft.nfc2.model.GuardTour;
import com.techyasoft.nfc2.model.Tour;

import java.util.List;

public interface GetTourQueryListener {
    void OnQuerySuccess(List<Tour> tours);
    void onNotFound();
    void OnQueryFail(String msg);
}
