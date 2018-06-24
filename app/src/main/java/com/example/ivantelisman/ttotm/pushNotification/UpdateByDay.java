package com.example.ivantelisman.ttotm.pushNotification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.ivantelisman.ttotm.PreferenceUtil;

public class UpdateByDay extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        PreferenceUtil.getInstance(context).saveUpdateDayBool(false);
        Log.d("boolRec: ", String.valueOf(PreferenceUtil.getInstance(context).getUpdateDayBool()));
    }
}
