package fabiorosario.ime.pgsc.sendsmstowork_1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.material.badge.ExperimentalBadgeUtils;

import java.util.Calendar;
import java.util.Date;

public class ReceptorIdentificacaoWifi extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String acao = intent.getAction();
        if (SmsNaoEnviado() &&
                (acao.equals("android.intent.action.BATTERY_CHANGED") ||
                        acao.equals("android.net.wifi.STATE_CHANGE"))) {
            try{
                Intent myService = new Intent(context, EnviarSMS.class);
                context.startService(myService);
            }catch (Exception e){

            }
        }
    }
    Boolean SmsNaoEnviado(){
        Calendar dataAtual = Calendar.getInstance();
        Calendar dataEnvioSMS = Calendar.getInstance();
        dataEnvioSMS.setTime(new Date(MainActivity.dataUltimoEnvioSMS.getTime()));

        long data = dataEnvioSMS.getTime().getTime();
        if (data > 0 && (dataEnvioSMS.get(Calendar.DAY_OF_MONTH) != dataAtual.get(Calendar.DAY_OF_MONTH) ||
                dataEnvioSMS.get(Calendar.MONTH) != dataAtual.get(Calendar.MONTH) ||
                dataEnvioSMS.get(Calendar.YEAR) != dataAtual.get(Calendar.YEAR)))
            return true;
        return false;
    }
}