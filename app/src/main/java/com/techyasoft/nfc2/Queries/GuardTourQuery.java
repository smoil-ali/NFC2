package com.techyasoft.nfc2.Queries;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.techyasoft.nfc2.Connections.mConnection;
import com.techyasoft.nfc2.Interfaces.GuardTourQueryListener;
import com.techyasoft.nfc2.model.GuardTour;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GuardTourQuery {
    final String TAG = GuardTourQuery.class.getSimpleName();
    GuardTourQueryListener listener;
    Context context;
    String query;
    int size=0;

    public GuardTourQuery(Context context,String guard_id) {
        this.context = context;
        query="select * from GuardData "+
                "where guard_id='"+guard_id+"'";
    }

    public void setListener(GuardTourQueryListener listener) {
        this.listener = listener;
    }

    public void executeQuery(){
        if (mConnection.connection!=null){
            try {
                Log.i(TAG,query);
                Statement statement=mConnection.connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                GuardTour data =new GuardTour();
                while (resultSet.next()){
                    size++;
                    data.setId(resultSet.getString(1));
                    data.setGuard_id(resultSet.getString(2));
                    data.setTour_minutes(resultSet.getString(3));
                    data.setTour_hour(resultSet.getString(4));
                    data.setTour_seconds(resultSet.getString(5));
                    data.setTour_container(resultSet.getString(6));
                }
                if (size>0){
                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listener.OnQuerySuccess(data);
                        }
                    });
                }else {
                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listener.onNotFound();
                        }
                    });
                }

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
