package fabiorosario.ime.pgsc.sendsmstowork_1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class EnviarSMSReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String acao = intent.getAction();
        if(acao.equals("fabiorosario.ime.pgsc.sendsmstowork.MY_ACTION") ||
                acao.equals("android.net.wifi.STATE_CHANGE")){
            Intent enviarSMS = new Intent(context, EnviarSMSService.class);
            context.startService(enviarSMS);
        }
    }
}