package com.techyasoft.nfc2.Queries;

import android.app.Activity;
import android.content.Context;
import android.util.Log;


import com.techyasoft.nfc2.Connections.mConnection;
import com.techyasoft.nfc2.Interfaces.LoginQueryListener;
import com.techyasoft.nfc2.model.Profile;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginQuery {
    final String TAG = LoginQuery.class.getSimpleName();
    Context context;
    LoginQueryListener listener;
    int size=0;
    String query;
    public LoginQuery(Context context,String userName,String password,LoginQueryListener listener) {
        this.context = context;
        this.listener=listener;
        query = "select * from Profile " +
                "where user_name='"+userName+"'"+
                "and password='"+password+"';";
    }

    public void executeQuery(){
        if (mConnection.connection!=null){
            try {
                Log.i(TAG,query);
                Statement statement=mConnection.connection.createStatement();
                Profile mProfile= new Profile();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()){
                    size++;
                    mProfile.setId(resultSet.getString(1));
                    mProfile.setUserName(resultSet.getString(2));
                    mProfile.setPassword(resultSet.getString(3));
                }
                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listener.OnQuerySuccess(size,mProfile);
                    }
                });
            } catch (SQLException e) {
                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listener.OnQueryFail(e.getMessage());
                    }
                });
            }
        }else {
            ((Activity)context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    listener.OnQueryFail("Connection is null");
                }
            });
        }
    }
}
