package fabiorosario.ime.pgsc.sendsmstowork_1;


import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EnviarSMS extends Service {
    String ssid;
    long data;
    Calendar dataEnvioSMS;
    private EnviarSMSDbHelper dbHelper;
    private final IBinder mBinder = new EnviarSMS.MyBinder();
    public class MyBinder extends Binder {
        EnviarSMS getService(){
            return EnviarSMS.this;
        }
    }

    @Override
    public void onCreate(){
        dbHelper = EnviarSMSDbHelper.getDbHelper(getApplicationContext());
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        inicializarVariaveis();
        if (aptoEnviarSMS() && chegouTrabalho()) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    enviarSMSContatos();
                    //  stopSelf(startId);
                }
            };
            Thread t = new Thread(runnable);
            t.start();
        }
        return Service.START_STICKY;
    }

    public EnviarSMS() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    void inicializarVariaveis(){
        String info[] = EnviarSMSDbHelper.informacoesEnvioSMS(dbHelper);
        ssid = info[1];
        data = Long.parseLong(info[2]);
    }
    Boolean aptoEnviarSMS(){
        dataEnvioSMS = Calendar.getInstance();
        Calendar dataBD = Calendar.getInstance();
        dataBD.setTime(new Date(data));
        if (dataEnvioSMS.get(Calendar.DAY_OF_MONTH) != dataBD.get(Calendar.DAY_OF_MONTH))
            return true;
        return false;
    }
    Boolean chegouTrabalho(){
        Boolean retorno = false;
        Context context = getApplicationContext();
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null){
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                retorno = false;
            }
            List<ScanResult> listResult = (List<ScanResult>) wifiManager.getScanResults();
            for (ScanResult result : listResult){
                if (ssid != null && result.SSID.contains(ssid)){
                    retorno = true;
                    break;
                }
            }
        }
        return retorno;
    }
    public Boolean enviarSMSContatos(){
        SmsManager sms = SmsManager.getDefault();
        Boolean ok = false;

        ArrayList<String> contatos = buscarContatos();
        for (String contato : contatos){
            sms.sendTextMessage(contato, null, "Oi, cheguei no trabalho!", null, null);
            ok = true;
        }

        if (ok){
            Date dataEnvioSMSAtual = Calendar.getInstance().getTime();
            EnviarSMSDbHelper.atualizarDataEnvioSMS(dbHelper, dataEnvioSMSAtual.getTime(), ssid);
        }
        return ok;
    }

    public ArrayList<String> buscarContatos(){
        ArrayList<String> contatos = new ArrayList<String>();
        Uri endereco = Uri.withAppendedPath(ContactsContract.CommonDataKinds.Phone.CONTENT_FILTER_URI,
                Uri.encode("#F#"));

        String selecion = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME  + " like '%#F#%'";
        Cursor cursor = getContentResolver().query(endereco,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                selecion, null, null);

        if(cursor != null){
            while (cursor.moveToNext()){
                contatos.add(cursor.getString(0));
            }
        }
        return contatos;
    }

    @Override
    public void onDestroy(){
        if (dbHelper != null)
            dbHelper.close();
        super.onDestroy();
    }
}