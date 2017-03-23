package dhbkhn.kien.quyhoachmangvt.View.DoThi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import dhbkhn.kien.quyhoachmangvt.Config.ConfigGraph;
import dhbkhn.kien.quyhoachmangvt.R;
import dhbkhn.kien.quyhoachmangvt.View.TrangChu.MainActivity;

public class GraphActivity extends AppCompatActivity {
    GraphView graphView;
    LinearLayout llContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        addControls();
        addEvents();
    }

    private void addControls() {
        llContainer = (LinearLayout) findViewById(R.id.llContainer);
        graphView = new GraphView(this);
        llContainer.addView(graphView);
        registerBR();
    }

    private void addEvents() {

    }

    public void btnFindBackbone(View v){

    }

    public void btnFindPath(View v){
        ConfigGraph.findPath = true;
        Intent iFindPath = new Intent(ConfigGraph.BROADCAST_FINDPATH);
        sendBroadcast(iFindPath);
    }

    public void btnStepByStep(View v){
        ConfigGraph.stepMode = 0;
        int stepId = ConfigGraph.step;
        ++ConfigGraph.step;
        if(stepId > 3) {
            Toast.makeText(this, "No connector outside network!", Toast.LENGTH_SHORT).show();
            graphView.invalidate();
            return;
        };
        graphView.invalidate();
    }

    BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConfigGraph.BROADCAST_FINDPATH)) {
                graphView.invalidate();
            }
        }
    };

    private void registerBR() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConfigGraph.BROADCAST_FINDPATH);
        registerReceiver(br,filter);
    }

    private void unregisterBR(){
        unregisterReceiver(br);
    }

    @Override
    protected void onDestroy() {
        unregisterBR();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Intent iBack = new Intent(this, MainActivity.class);
        iBack.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(iBack);
        super.onBackPressed();
    }
}
