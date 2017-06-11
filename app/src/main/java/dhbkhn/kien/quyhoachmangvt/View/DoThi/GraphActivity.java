package dhbkhn.kien.quyhoachmangvt.View.DoThi;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import dhbkhn.kien.quyhoachmangvt.Config.ConfigGraph;
import dhbkhn.kien.quyhoachmangvt.R;
import dhbkhn.kien.quyhoachmangvt.View.TrangChu.MainActivity;

import static dhbkhn.kien.quyhoachmangvt.Config.ConfigGraph.step;

public class GraphActivity extends AppCompatActivity {
    private GraphView graphView;
    private LinearLayout llContainer, llStepByStep, llInfo;
    private AppCompatSeekBar seekbarWeight;
    private FloatingActionButton fab, fabPlay, fabAdd, fabView;
    private boolean visible = false;
    private Dialog dialog;
    private TextView tvInfoNode, tvInfoAlpha, tvCurrentWei, tvInfoGamma, tvStep, tvDetailStep, tvMinWeight, tvMaxWeight;
    private boolean generated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        addControls();
        addEvents();
    }

    private void addControls() {
        llContainer = (LinearLayout) findViewById(R.id.llContainer);
        llStepByStep = (LinearLayout) findViewById(R.id.llStepByStep);
        llInfo = (LinearLayout) findViewById(R.id.llInfo);
        tvStep = (TextView) findViewById(R.id.tvStep);
        tvDetailStep = (TextView) findViewById(R.id.tvDetailStep);

        tvInfoNode = (TextView) findViewById(R.id.tvInfoNode);
        tvInfoAlpha = (TextView) findViewById(R.id.tvInfoAlpha);
        tvInfoGamma = (TextView) findViewById(R.id.tvInfoGamma);
        tvMinWeight = (TextView) findViewById(R.id.tvMinWeight);
        tvMaxWeight = (TextView) findViewById(R.id.tvMaxWeight);
        tvCurrentWei = (TextView) findViewById(R.id.tvCurrentWei);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fabPlay = (FloatingActionButton) findViewById(R.id.fabPlay);
        fabAdd = (FloatingActionButton) findViewById(R.id.fabAdd);
        fabView = (FloatingActionButton) findViewById(R.id.fabView);

        seekbarWeight = (AppCompatSeekBar) findViewById(R.id.seekbarWeight);
        registerBR();
    }

    private void addEvents() {
        addListener();
    }

    private void addListener() {
        fab.setOnClickListener(myClick);
        fabPlay.setOnClickListener(myClick);
        fabAdd.setOnClickListener(myClick);
        fabView.setOnClickListener(myClick);
        seekbarWeight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvCurrentWei.setText(String.valueOf(calculateWeight(i, ConfigGraph.minOfWeight, ConfigGraph.maxOfWeight)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private View.OnClickListener myClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id) {
                case R.id.fab:
                    visible = !visible;
                    if (visible) {
                        fabPlay.show();
                        fabAdd.show();
                        fabView.show();
                    }else{
                        fabPlay.hide();
                        fabAdd.hide();
                        fabView.hide();
                    }
                    break;
                case R.id.fabAdd:
                    llContainer.setVisibility(View.VISIBLE);
                    llContainer.removeAllViews();
                    step = 0;
                    ConfigGraph.stepMode = false;
                    ConfigGraph.findPath = false;
                    ConfigGraph.findPathBetweenTwoVertex = false;

                    displayDialog();
                    break;
                case R.id.fabPlay:
                    if (!generated) {
                        Toast.makeText(GraphActivity.this, "Bạn chưa khởi tạo đồ thị mạng!!!", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    llContainer.setVisibility(View.VISIBLE);
                    llStepByStep.setVisibility(View.VISIBLE);
                    llInfo.setVisibility(View.GONE);

                    ConfigGraph.stepMode = true;
                    ConfigGraph.findPath = false;
                    ConfigGraph.findPathBetweenTwoVertex = false;

                    int omega = calculateWeight(seekbarWeight.getProgress(), ConfigGraph.minOfWeight, ConfigGraph.maxOfWeight);
                    ConfigGraph.mOmega = omega;
                    int stepId = ConfigGraph.step;
                    if (stepId < 5) {
                        ++ConfigGraph.step;
                    }
                    String step;
                    if (stepId == 0) {
                        step = "Tìm các nút backbone trong mạng";
                    }
                    else if(stepId == 1){
                        step = "Tìm nút center từ các nút backbone";
                    }
                    else if(stepId == 2){
                        step = "Tìm các nút truy nhập của mỗi backbone";
                    }
                    else if(stepId == 3){
                        step = "Đưa tất cả các nút còn lại vào trong mạng";
                    }
                    else if (stepId == 4) {
                        step = "Tìm đường ngắn nhất giữa các backbone";
                    }
                    else {
                        step = "Đã hoàn thành thuật toán!!! Đường đi ngắn nhất giữa các backbone có độ dài " + ConfigGraph.minLengthPath;
                    };

                    tvStep.setText("Bước " + ConfigGraph.step);
                    tvDetailStep.setText(step);
                    graphView.invalidate();
                    break;
                case R.id.fabView:
                    if(!generated){
                        Toast.makeText(GraphActivity.this, "Bạn chưa khởi tạo đồ thị mạng!!!", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    llInfo.setVisibility(View.VISIBLE);
                    llStepByStep.setVisibility(View.GONE);
                    ConfigGraph.findPath = true;
                    ConfigGraph.stepMode = false;
                    ConfigGraph.findPathBetweenTwoVertex = false;
                    Intent iFindPath = new Intent(ConfigGraph.BROADCAST_FINDPATH);
                    sendBroadcast(iFindPath);
                    break;
            }
        }
    };

    BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConfigGraph.BROADCAST_FINDPATH)) {
                graphView.invalidate();
            } else if (action.equals(ConfigGraph.CHANGE_MIN_WEIGHT)) {
                tvMinWeight.setText(String.valueOf(ConfigGraph.minOfWeight));
            } else if (action.equals(ConfigGraph.CHANGE_MAX_WEIGHT)) {
                tvMaxWeight.setText(String.valueOf(ConfigGraph.maxOfWeight));
            }
        }
    };

    private void displayDialog() {
        dialog = new Dialog(this, R.style.FullHeightDialog);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View showView = inflater.inflate(R.layout.custom_dialog_add_record, null);
        final EditText edtNumberNode = (EditText) showView.findViewById(R.id.edtNumberNode);
        final AppCompatSeekBar seekbarAlpha = (AppCompatSeekBar) showView.findViewById(R.id.seekbarAlpha);
        final AppCompatSeekBar seekbarGamma = (AppCompatSeekBar) showView.findViewById(R.id.seekbarGamma);
        final TextView tvCurrentAlpha = (TextView) showView.findViewById(R.id.tvCurrentAlpha);
        final TextView tvCurrentGamma = (TextView) showView.findViewById(R.id.tvCurrentGamma);
        Button btnGenGraph = (Button) showView.findViewById(R.id.btnGenGraph);
        Button btnCloseGraph = (Button) showView.findViewById(R.id.btnCloseGraph);

        btnGenGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int numNode = Integer.parseInt(edtNumberNode.getText().toString().trim());
                int prAlpha = seekbarAlpha.getProgress();
                float alpha = 1.0f * prAlpha/100;
                int prGamma = seekbarGamma.getProgress();
                float gamma = 0.3f * prGamma/100 + 0.5f;
                changeDatabase(numNode, alpha, gamma);
                dialog.dismiss();
            }
        });

        seekbarAlpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvCurrentAlpha.setText(String.valueOf(1.0f * i/100));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekbarGamma.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvCurrentGamma.setText(String.valueOf(0.3f * i/100 + 0.5f));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btnCloseGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.setCancelable(false);
        dialog.setContentView(showView);
        dialog.show();
    }

    private void changeDatabase(int numNode, float alpha, float gamma) {
        llInfo.setVisibility(View.VISIBLE);
        tvInfoNode.setText(String.valueOf(numNode));
        tvInfoAlpha.setText(String.format("%.2f",alpha));
        tvInfoGamma.setText(String.format("%.3f",gamma));

        ConfigGraph.mGamma = gamma;
        graphView = new GraphView(this, numNode, 0, alpha, gamma);
        llContainer.addView(graphView);
        generated = true;
    }

    private int calculateWeight(int prWeight, int minWeight, int maxWeight) {
        float weight = ((float) (maxWeight - minWeight)) * ((float)prWeight/100) + minWeight;
        int weiChoose = (int) weight;
        return weiChoose;
    }

    private void registerBR() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConfigGraph.BROADCAST_FINDPATH);
        filter.addAction(ConfigGraph.CHANGE_MIN_WEIGHT);
        filter.addAction(ConfigGraph.CHANGE_MAX_WEIGHT);
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
