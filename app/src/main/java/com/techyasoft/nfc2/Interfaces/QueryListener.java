package com.techyasoft.nfc2.Interfaces;

import java.sql.ResultSet;

public interface QueryListener {
    void OnQuerySuccess();
    void OnQueryFail(String msg);
}
