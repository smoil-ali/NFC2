package com.techyasoft.nfc2.Queries;

import android.app.Activity;
import android.content.Context;
import android.util.Log;


import com.techyasoft.nfc2.Interfaces.QueryListener;

import java.sql.SQLException;
import java.sql.Statement;

public class RegisterQuery {
//    final String TAG = RegisterQuery.class.getSimpleName();
//    Context context;
//    QueryListener listener;
//    int size=0;
//    String query;
//
//    public RegisterQuery(Context context, QueryListener listener,Register register) {
//        this.context = context;
//        this.listener = listener;
//        query="Insert into History (user_name,card_number,time_stamp,comment,box_name)" +
//                " values ('"+register.getUser_name()+"',"+"'"+register.getCard_number()+"','"+register.getTime_stamp()+"','"+register.getComment()+"','"+register.getBox_name()+"')";
//    }
//
//    public void executeQuery(){
//        if (mConnection.connection!=null){
//            try {
//                Log.i(TAG,query);
//                Statement statement=mConnection.connection.createStatement();
//                int resultSet = statement.executeUpdate(query);
//                ((Activity)context).runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        listener.OnQuerySuccess(resultSet,null);
//                    }
//                });
//            } catch (SQLException e) {
//                ((Activity)context).runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        listener.OnQueryFail(e.getMessage());
//                    }
//                });
//            }
//        }else {
//            ((Activity)context).runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    listener.OnQueryFail("Connection is null");
//                }
//            });
//        }
//    }
}
