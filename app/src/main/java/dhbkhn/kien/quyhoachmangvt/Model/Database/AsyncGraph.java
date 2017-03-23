package dhbkhn.kien.quyhoachmangvt.Model.Database;

import android.app.Dialog;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import dhbkhn.kien.quyhoachmangvt.Model.Object.Vertex;
import dhbkhn.kien.quyhoachmangvt.Model.SimulatorNetwork;
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
