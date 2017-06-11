package dhbkhn.kien.quyhoachmangvt.Config;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by kiend on 3/16/2017.
 */

public class ConfigGraph {
    public static final String TAG_LOG = "MyApp";
    public static final String BROADCAST_FINDPATH = "FIND_PATH";
    public static final String CHANGE_MIN_WEIGHT = "MIN_WEIGHT";
    public static final String CHANGE_MAX_WEIGHT = "MAX_WEIGHT";

    public static int minOfWeight = 0;
    public static int maxOfWeight = 1;
    public static float mGamma = 0f;
    public static int mOmega = 0;
    public static int minLengthPath = 0;

    public static int sizeOfVertex = 0;
    public static boolean findPath = false;
    public static boolean stepMode = false;
    public static boolean findPathBetweenTwoVertex = false;
    public static int step = 0;

    public static final String[] AN_BN_DK_BLUE = {"node_black", "node_dark_blue","backbone_dark_blue","center"};
    public static final String[] AN_BN_ORANGE = {"node_black", "node_orange","backbone_orange","center"};
    public static final String[] AN_BN_GREEN = {"node_black", "node_green","backbone_green","center"};
    public static final String[] AN_BN_PINK = {"node_black", "node_pink","backbone_pink","center"};
    public static final String[] AN_BN_BLUE = {"node_black", "node_blue","backbone_blue","center"};
    public static final String[] AN_BN_RED = {"node_black", "node_red","backbone_red","center"};
    public static final String[] AN_BN_LAST = {"node_black", "node_last","backbone_last","center"};

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
