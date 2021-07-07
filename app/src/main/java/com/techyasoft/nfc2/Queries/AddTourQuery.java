package com.techyasoft.nfc2.Queries;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.techyasoft.nfc2.Connections.mConnection;
import com.techyasoft.nfc2.Interfaces.QueryListener;
import com.techyasoft.nfc2.model.Tour;

import java.sql.SQLException;
import java.sql.Statement;

public class AddTourQuery {
    final String TAG = AddTourQuery.class.getSimpleName();
    Context context;
    QueryListener listener;
    String query;

    public AddTourQuery(Context context, Tour tour,QueryListener listener) {
        this.context = context;
        query="Insert into Tour (guard_id,tour_number,total_swipes,tour_counter,date)" +
                " values ('"+tour.getGuard_id()+"',"+"'"+tour.getTour_number()+"',"+"'"+tour.getTotal_swipes()+"',"+"'"+tour.getTour_counter()+"',"+"'"+tour.getDate()+"')";
        this.listener = listener;
        Log.i(TAG,query);
    }

    public void executeQuery(){
        if (mConnection.connection!=null){
            try {
                Log.i(TAG,query);
                Statement statement=mConnection.connection.createStatement();
                int resultSet = statement.executeUpdate(query);
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
