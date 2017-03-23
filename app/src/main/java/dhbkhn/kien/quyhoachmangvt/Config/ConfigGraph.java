package dhbkhn.kien.quyhoachmangvt.Config;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by kiend on 3/16/2017.
 */

public class ConfigGraph {
    public static int sizeOfVertex = 0;
    public static final String TAG_LOG = "MyApp";
    public static boolean findPath = false;
    public static final String BROADCAST_FINDPATH = "FIND_PATH";
    public static int stepMode = 1;
    public static int step = 0;
    public static final int MODE_STEP_BY_STEP = 0;
    public static final int MODE_PROMTP_RESULT = 1;

    public static int getDrawableIdResByName(Context context, String nameRes) {
        String pkgName = context.getPackageName();
        int idRes = context.getResources().getIdentifier(nameRes, "drawable", pkgName);
        return idRes;
    }

    public static double getWidthOfActivity(Context context){
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        Display display = windowManager.getDefaultDisplay();
        display.getSize(size);
        return size.x;
    }

    public static double getHeightOfActivity(Context context){
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        Display display = windowManager.getDefaultDisplay();
        display.getSize(size);
        return size.y;
    }
}
