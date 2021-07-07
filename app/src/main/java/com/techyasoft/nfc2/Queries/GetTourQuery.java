package com.techyasoft.nfc2.Queries;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.techyasoft.nfc2.Connections.mConnection;
import com.techyasoft.nfc2.Interfaces.GetTourQueryListener;
import com.techyasoft.nfc2.model.GuardDetail;
import com.techyasoft.nfc2.model.Profile;
import com.techyasoft.nfc2.model.Tour;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GetTourQuery {
    final String TAG = GetTourQuery.class.getSimpleName();
    GetTourQueryListener listener;
    Context context;
    String query;

    public GetTourQuery(GetTourQueryListener listener, Context context,String id,String date) {
        this.listener = listener;
        this.context = context;
        query = "select * from Tour " +
                "where guard_id='"+id+"' and date='"+date+"';";
    }

    public void executeQuery(){
        if (mConnection.connection!=null){
            try {
                Log.i(TAG,query);
                Statement statement=mConnection.connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                List<Tour> tours = new ArrayList<>();
                while (resultSet.next()){
                    Tour data = new Tour();
                    data.setId(resultSet.getString(1));
                    data.setGuard_id(resultSet.getString(2));
                    data.setTour_number(resultSet.getString(3));
                    data.setTotal_swipes(resultSet.getString(4));
                    data.setTour_counter(resultSet.getString(5));
                    data.setDate(resultSet.getString(6));
                    tours.add(data);
                }
                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listener.OnQuerySuccess(tours);
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
