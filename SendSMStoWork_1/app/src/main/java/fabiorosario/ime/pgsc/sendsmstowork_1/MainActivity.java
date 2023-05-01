package fabiorosario.ime.pgsc.sendsmstowork_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    Intent enviarSMS;
    EnviarSMSDbHelper dbHelper;
    EditText editTextSSID;
    TextView textViewData;
    String idSSID = "";
    Date dataUltimoEnvioSMS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextSSID = (EditText)findViewById(R.id.ssid);
        textViewData = (TextView)findViewById(R.id.data);

        final Button buttonSalvarSSID = findViewById(R.id.buttonSalvarSSID);
        buttonSalvarSSID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ssid = editTextSSID.getText().toString();
                Date data = new Date(1);
                if (EnviarSMSDbHelper.atualizarSSID(dbHelper, idSSID, ssid, data)){
                    Toast.makeText(MainActivity.this, "Atualização do SSID realizada!", Toast.LENGTH_SHORT).show();
                    dataUltimoEnvioSMS = data;
                    textViewData.setText(dataUltimoEnvioSMS.toString());
                }
                else
                    Toast.makeText(MainActivity.this, "Não foi possível realizar a atualização do SSID!",
                            Toast.LENGTH_SHORT).show();
            }
        });

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_CONTACTS,
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION},
                PackageManager.PERMISSION_GRANTED);

        pedirPermissaoUsarLocalizacaoSegundoPlano();

        Context context = getBaseContext();
        Intent enviarSMS = new Intent(context, EnviarSMSService.class);
        enviarSMS.setFlags(Intent.FLAG_EXCLUDE_STOPPED_PACKAGES);
        context.startService(enviarSMS);

        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    @Override
    protected void onStart(){
        super.onStart();
        dbHelper = EnviarSMSDbHelper.getDbHelper(getApplicationContext());
        String[] info = EnviarSMSDbHelper.informacoesEnvioSMS(dbHelper);
        idSSID = info[0];
        editTextSSID.setText(info[1]);
        dataUltimoEnvioSMS = new Date(Long.parseLong(info[2]));
        textViewData.setText(dataUltimoEnvioSMS.toString());
    }
    @Override
    protected void onStop(){
        super.onStop();
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        if (enviarSMS != null)
            stopService(enviarSMS);
    }

    private void pedirPermissaoUsarLocalizacaoSegundoPlano() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permissão Necessária!")
                    .setMessage("O aplicativo necessita de Permissão à Localização em segundo plano! Escolha \"Permitir o tempo todo\" na próxima tela.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, PackageManager.PERMISSION_GRANTED);
                        }
                    })
                    .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, PackageManager.PERMISSION_GRANTED);
        }
    }
}