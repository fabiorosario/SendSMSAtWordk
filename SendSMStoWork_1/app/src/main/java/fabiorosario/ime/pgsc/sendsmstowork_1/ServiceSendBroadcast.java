package fabiorosario.ime.pgsc.sendsmstowork_1;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.Calendar;

public class ServiceSendBroadcast extends Service {
    public ServiceSendBroadcast() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    while(true){
                        Intent broadcastReceiver = new Intent("fabiorosario.ime.pgsc.sendsmstowork.MY_ACTION");
                        getBaseContext().sendBroadcast(broadcastReceiver);
                        Thread.sleep(1000 * 60);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        Thread t = new Thread(runnable);
        t.start();

        return Service.START_STICKY;
    }
}