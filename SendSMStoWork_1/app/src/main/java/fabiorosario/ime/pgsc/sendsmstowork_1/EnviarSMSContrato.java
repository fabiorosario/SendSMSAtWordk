package fabiorosario.ime.pgsc.sendsmstowork_1;

import android.provider.BaseColumns;

public final class EnviarSMSContrato {

    private EnviarSMSContrato() {}

    public static class EsquemaBD implements BaseColumns {
        public static final String TABLE_NAME = "enviarSMS";
        public static final String COLUMN_NAME_DATA = "data";
        public static final String COLUMN_NAME_SSID = "ssid";
    }
}
