package materialtest.theartistandtheengineer.co.materialtest.logging;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by mpcen-desktop on 3/27/15.
 */
public class L {
    public static void m(String message){
        Log.d("FRESH", "" + message);
    }

    public static void t(Context context, String message){
        Toast.makeText(context, message + "", Toast.LENGTH_LONG).show();
    }

    public static void T(Context context, String message){
        Toast.makeText(context, message + "", Toast.LENGTH_LONG).show();
    }
}
