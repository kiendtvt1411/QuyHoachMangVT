package dhbkhn.kien.quyhoachmangvt.View.TrangChu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import dhbkhn.kien.quyhoachmangvt.Config.ConfigGraph;
import dhbkhn.kien.quyhoachmangvt.Model.Database.ProcessingRandom;
import dhbkhn.kien.quyhoachmangvt.Model.Object.Vertex;
import dhbkhn.kien.quyhoachmangvt.R;
import dhbkhn.kien.quyhoachmangvt.View.DoThi.GraphActivity;

public class MainActivity extends AppCompatActivity {
    ListView rvGraph;
    ArrayAdapter<Vertex> adapter;
    List<Vertex> listVertex;
    ProcessingRandom processingRandom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
        addEvents();
    }

    private void addControls() {
        rvGraph = (ListView) findViewById(R.id.rvGraph);

        ConfigGraph.sizeOfVertex = 50;
        int size = ConfigGraph.sizeOfVertex;
        processingRandom = new ProcessingRandom(size);
        listVertex = processingRandom.randomGraph();

        Log.d(ConfigGraph.TAG_LOG, "My graph: " + listVertex.size() + " - " + ConfigGraph.sizeOfVertex);
        adapter = new ArrayAdapter<Vertex>(this, android.R.layout.simple_list_item_1, listVertex);
        rvGraph.setAdapter(adapter);
    }

    private void addEvents() {
    }

    public void btnOpenGraphView(View v){
        Intent iGraph = new Intent(this, GraphActivity.class);
        iGraph.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        ConfigGraph.step = 0;
        startActivity(iGraph);
    }
}
