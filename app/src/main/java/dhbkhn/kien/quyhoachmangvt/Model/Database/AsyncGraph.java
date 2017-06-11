package dhbkhn.kien.quyhoachmangvt.Model.Database;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import dhbkhn.kien.quyhoachmangvt.Config.ConfigGraph;
import dhbkhn.kien.quyhoachmangvt.Model.Object.Vertex;
import dhbkhn.kien.quyhoachmangvt.Model.Simulator.SimulatorNetwork;
import dhbkhn.kien.quyhoachmangvt.View.DoThi.GraphView;

/**
 * Created by kiend on 3/17/2017.
 */

public class AsyncGraph extends AsyncTask<Integer, Void, List<Vertex>> {
    private List<Vertex> vertexList;
    private GraphView graphView;
    private Dialog alertDialog;
    //simulator networking
    private SimulatorNetwork simulatorNetwork;

    public AsyncGraph(GraphView graphView, List<Vertex> vertexList, SimulatorNetwork simulatorNetwork) {
        this.graphView = graphView;
        this.vertexList = vertexList;
        this.simulatorNetwork = simulatorNetwork;
        alertDialog = new Dialog(graphView.getContext());
        alertDialog.setTitle("Generating Graph");
        alertDialog.setCancelable(false);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        alertDialog.show();
        this.vertexList.clear();
    }

    @Override
    protected void onPostExecute(List<Vertex> list) {
        super.onPostExecute(list);
        if (list != null) {
            this.vertexList.addAll(list);
            List<Vertex> clone = new ArrayList<>();
            clone.addAll(vertexList);
            simulatorNetwork.setClone(clone);
            CalculateMinMax calculateMinMax = new CalculateMinMax(clone, ConfigGraph.mGamma);
            int minWei = calculateMinMax.minWeightOfGraph();
            int maxWei = calculateMinMax.maxWeightOfGraph();
            ConfigGraph.minOfWeight = minWei;
            ConfigGraph.maxOfWeight = maxWei;
            Intent iMin = new Intent(ConfigGraph.CHANGE_MIN_WEIGHT);
            graphView.getContext().sendBroadcast(iMin);
            Intent iMax = new Intent(ConfigGraph.CHANGE_MAX_WEIGHT);
            graphView.getContext().sendBroadcast(iMax);
        }else {
            Toast.makeText(this.graphView.getContext(), "Don't generate!!!", Toast.LENGTH_SHORT).show();
        }
        alertDialog.dismiss();
    }

    @Override
    protected List<Vertex> doInBackground(Integer... integers) {
        int size = integers[0];
        List<Vertex> listVt = new ArrayList<>();
        if (size > 0) {
            ProcessingRandom processingRandom = new ProcessingRandom(size);
            listVt.addAll(processingRandom.randomGraph());
        }
        return listVt;
    }
}
