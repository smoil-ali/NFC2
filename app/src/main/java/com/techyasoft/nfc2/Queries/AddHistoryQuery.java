package com.techyasoft.nfc2.Queries;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.techyasoft.nfc2.Connections.mConnection;
import com.techyasoft.nfc2.Interfaces.QueryListener;
import com.techyasoft.nfc2.model.History;

import java.sql.SQLException;
import java.sql.Statement;

public class AddHistoryQuery {
    final String TAG = AddTourQuery.class.getSimpleName();
    QueryListener listener;
    Context context;
    String query;

    public AddHistoryQuery(QueryListener listener, Context context, History history) {
        this.listener = listener;
        this.context = context;
        query="Insert into History (guard_id,card_number,date,comment,tour_counter)" +
                " values ('"+history.getGuard_id()+"',"+"'"+history.getCard_number()+"',"+"'"+history.getDate()+"',"+"'"+history.getComment()+"',"+"'"+history.getTour_counter()+"')";
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
