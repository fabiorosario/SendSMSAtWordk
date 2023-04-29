package fabiorosario.ime.pgsc.sendsmstowork_1;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import java.util.Calendar;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Intent serviceSendBroadcast = new Intent(context, ReceptorIdentificacaoWifi.class);
            serviceSendBroadcast = new Intent(context, ServiceSendBroadcast.class);
            serviceSendBroadcast.setFlags(Intent.FLAG_EXCLUDE_STOPPED_PACKAGES);
            context.startService(serviceSendBroadcast);
        }
    }
}