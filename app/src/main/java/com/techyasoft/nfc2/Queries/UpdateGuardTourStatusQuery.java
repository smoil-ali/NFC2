package com.techyasoft.nfc2.Queries;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.techyasoft.nfc2.Connections.mConnection;
import com.techyasoft.nfc2.Interfaces.GuardTourQueryListener;
import com.techyasoft.nfc2.Interfaces.QueryListener;
import com.techyasoft.nfc2.model.GuardTour;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UpdateGuardTourStatusQuery {
    final String TAG = UpdateGuardTourStatusQuery.class.getSimpleName();
    QueryListener listener;
    Context context;
    String query;

    public UpdateGuardTourStatusQuery(Context context,GuardTour guardTour) {
        this.context = context;
        query="Update Guard_Tour Set" +
                " detail =  '"+"hi"+"'" + "Where id = '"+guardTour.getId()+"'";
        Log.i(TAG,query);
    }

    public void setListener(QueryListener listener) {
        this.listener = listener;
    }

    public void executeQuery(){
        if (mConnection.connection!=null){
            try {
                Log.i(TAG,query);
                Statement statement=mConnection.connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                listener.OnQuerySuccess();
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
