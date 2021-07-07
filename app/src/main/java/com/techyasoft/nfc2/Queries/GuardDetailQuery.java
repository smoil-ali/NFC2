package com.techyasoft.nfc2.Queries;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.techyasoft.nfc2.Connections.mConnection;
import com.techyasoft.nfc2.Interfaces.GuardDetailQueryListener;
import com.techyasoft.nfc2.Interfaces.GuardTourQueryListener;
import com.techyasoft.nfc2.Interfaces.QueryListener;
import com.techyasoft.nfc2.model.GuardDetail;
import com.techyasoft.nfc2.model.GuardTour;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GuardDetailQuery {
    final String TAG = GuardDetailQuery.class.getSimpleName();
    QueryListener listener;
    Context context;
    String query;
    int size=0;

    public GuardDetailQuery(Context context) {
        this.context = context;
    }

    public void addDetail(GuardDetail guardDetail) {
        query="Insert into Guard_Detail (guard_id,details,box,date,tour_counter)" +
                " values ('"+guardDetail.getGuard_id()+"',"+"'"+guardDetail.getDetails()+"','"+guardDetail.getBox()+"',"+"'"+guardDetail.getDate()+"',"+"'"+guardDetail.getTour_counter()+"')";
    }

    public void updateDetail(GuardDetail guardDetail){
        query="Update Guard_Detail Set" +
                " details =  '"+guardDetail.getDetails()+"'," + "tour_counter = '"+guardDetail.getTour_counter()+"'," +"box = '"+guardDetail.getBox()+"'" +"Where guard_id = '"+guardDetail.getGuard_id()+"' And date = '"+guardDetail.getDate()+"'";
        Log.i(TAG,query);
    }

    public void setListener(QueryListener listener) {
        this.listener = listener;
    }

    public void executeQuery(){
        ResultSet resultSet = null;
        if (mConnection.connection!=null){
            try {
                Log.i(TAG,query);
                Statement statement=mConnection.connection.createStatement();
                resultSet = statement.executeQuery(query);
                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listener.OnQuerySuccess();
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
