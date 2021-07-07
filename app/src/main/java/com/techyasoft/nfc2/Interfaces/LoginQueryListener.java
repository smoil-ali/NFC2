package com.techyasoft.nfc2.Interfaces;


import com.techyasoft.nfc2.model.Profile;

public interface LoginQueryListener {
    void OnQuerySuccess(int size, Profile mProfile);
    void OnQueryFail(String msg);
}
