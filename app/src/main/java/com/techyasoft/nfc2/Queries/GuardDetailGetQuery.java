package com.techyasoft.nfc2.Queries;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.techyasoft.nfc2.Connections.mConnection;
import com.techyasoft.nfc2.Interfaces.GuardDetailQueryListener;
import com.techyasoft.nfc2.Interfaces.LoginQueryListener;
import com.techyasoft.nfc2.model.GuardDetail;
import com.techyasoft.nfc2.model.Profile;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GuardDetailGetQuery {
    final String TAG = GuardDetailQuery.class.getSimpleName();
    Context context;
    GuardDetailQueryListener listener;
    int size=0;
    String query;

    public GuardDetailGetQuery(Context context,String id,String date,GuardDetailQueryListener listener) {
        this.context = context;
        this.listener=listener;
        query = "select * from Guard_Detail " +
                "where guard_id='"+id+"' and date='"+date+"';";
    }

    public void executeQuery(){
        if (mConnection.connection!=null){
            try {
                Log.i(TAG,query);
                Statement statement=mConnection.connection.createStatement();
                Profile mProfile= new Profile();
                GuardDetail data = new GuardDetail();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()){
                    size++;
                    data.setGuard_id(resultSet.getString(1));
                    data.setDetails(resultSet.getString(2));
                    data.setBox(resultSet.getString(3));
                    data.setDate(resultSet.getString(4));
                    data.setTour_counter(resultSet.getString(5));
                }
                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listener.OnQuerySuccess(data);
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
