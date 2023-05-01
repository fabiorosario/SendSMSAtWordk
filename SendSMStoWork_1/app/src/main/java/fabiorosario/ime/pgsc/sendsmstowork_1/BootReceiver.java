package fabiorosario.ime.pgsc.sendsmstowork_1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Intent enviarSMS = new Intent(context, EnviarSMSService.class);
            enviarSMS.setFlags(Intent.FLAG_RECEIVER_FOREGROUND);
            context.startForegroundService(enviarSMS);
        }
    }
}