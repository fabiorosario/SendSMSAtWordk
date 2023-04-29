package fabiorosario.ime.pgsc.sendsmstowork_1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.badge.ExperimentalBadgeUtils;

import java.util.Calendar;
import java.util.Date;

public class ReceptorIdentificacaoWifi extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String acao = intent.getAction();
        if(acao.equals("fabiorosario.ime.pgsc.sendsmstowork.MY_ACTION") ||
                acao.equals("android.net.wifi.STATE_CHANGE")){
            try{
                Intent enviarSMS = new Intent(context, EnviarSMS.class);
                context.startService(enviarSMS);
            }catch (Exception e){

            }
        }
    }

}