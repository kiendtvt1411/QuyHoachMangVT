package dhbkhn.kien.quyhoachmangvt.View.ThongSo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import dhbkhn.kien.quyhoachmangvt.Config.ConfigGraph;
import dhbkhn.kien.quyhoachmangvt.Model.Object.GraphObject;
import dhbkhn.kien.quyhoachmangvt.Model.Object.Vertex;
import dhbkhn.kien.quyhoachmangvt.R;

public class ArgumentActivity extends AppCompatActivity {
    private TextView tvNodeId, tvNodeCoor, tvNodeType, tvNodeWeight, tvNodeAlpha, tvNodeOmega, tvNodeGamma;
    private ListView rvErlang, rvCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_argument);
        Log.d(ConfigGraph.TAG_LOG, "haha");
        addControls();
        addEvents();
    }

    private void addControls() {
        tvNodeId = (TextView) findViewById(R.id.tvNodeId);
        tvNodeCoor = (TextView) findViewById(R.id.tvNodeCoor);
        tvNodeType = (TextView) findViewById(R.id.tvNodeType);
        tvNodeWeight = (TextView) findViewById(R.id.tvNodeWeight);
        tvNodeAlpha = (TextView) findViewById(R.id.tvNodeAlpha);
        tvNodeOmega = (TextView) findViewById(R.id.tvNodeOmega);
        tvNodeGamma = (TextView) findViewById(R.id.tvNodeGamma);
        rvErlang = (ListView) findViewById(R.id.rvErlang);
        rvCost = (ListView) findViewById(R.id.rvCost);
    }

    private void addEvents() {
        int id = getIntent().getIntExtra("id", 0);
        float alpha = getIntent().getFloatExtra("alpha", 0);
        int weight = getIntent().getIntExtra("weight", 0);
        float gamma = getIntent().getFloatExtra("gamma", 0);
        Vertex vertex = GraphObject.listVertex.get(id);
        int size = GraphObject.listVertex.size();
        if (vertex != null) {
            tvNodeId.setText(String.valueOf(vertex.getId()));
            tvNodeCoor.setText(String.format("%.2f", vertex.getCoorX()) + ", " + String.format("%.2f", vertex.getCoorY()));
            int type = vertex.getType();
            String sType;
            if (type == 0) {
                sType = "Not connected";
            } else if (type == 1) {
                sType = "Connector";
            } else if (type == 2) {
                sType = "Backbone";
            } else {
                sType = "Center";
            }
            tvNodeType.setText(sType);
            tvNodeAlpha.setText(String.format("%.2f",alpha));
            tvNodeOmega.setText(String.valueOf(weight));
            tvNodeGamma.setText(String.format("%.2f",gamma));
            tvNodeWeight.setText(String.valueOf(vertex.getWeight()));
            int[] cost = vertex.getCost();
            String[] sCost = new String[size];
            int lengCost = cost.length;
            for (int i = 0; i < lengCost; i++) {
                sCost[i] = "Với nút " + i + ": " + cost[i];
            }
            ArrayAdapter<String> adapterCost = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sCost);
            rvCost.setAdapter(adapterCost);

            int[] erlangIn = vertex.getErlangIn();
            String[] sErlang = new String[size];
            int lengEr = erlangIn.length;
            for (int i = 0; i < lengEr; i++) {
                sErlang[i] = "Từ nút " + i + ": " + erlangIn[i];
            }
            ArrayAdapter adapterErlang = new ArrayAdapter(this, android.R.layout.simple_list_item_1, sErlang);
            rvErlang.setAdapter(adapterErlang);
        }
    }
}
