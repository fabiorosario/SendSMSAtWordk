package fabiorosario.ime.pgsc.sendsmstowork_1;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class EnviarBroadcastService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Intent broadcastReceiver = new Intent("fabiorosario.ime.pgsc.sendsmstowork.MY_ACTION");
                    while(true){
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
    public EnviarBroadcastService() {
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}