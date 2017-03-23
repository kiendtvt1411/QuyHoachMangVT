package dhbkhn.kien.quyhoachmangvt.Model;

import android.graphics.Canvas;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import dhbkhn.kien.quyhoachmangvt.Config.ConfigGraph;
import dhbkhn.kien.quyhoachmangvt.Model.Object.GraphObject;
import dhbkhn.kien.quyhoachmangvt.Model.Object.Vertex;
import dhbkhn.kien.quyhoachmangvt.View.DoThi.GraphView;

/**
 * Created by kiend on 3/19/2017.
 */

public class SimulatorNetwork {
    GraphView graphView;
    GraphObject graphObject;
    List<Vertex> clone;
    List<Vertex> listBackbone;
    Vertex center;
    float distanceOfNetwork;
    int accessStep2 = 0;
    int accessStep3 = 0;
    int accessStep4 = 0;

    public SimulatorNetwork(GraphObject graphObject, GraphView graphView) {
        this.graphObject = graphObject;
        this.graphView = graphView;
        this.clone = new ArrayList<>();
        this.listBackbone = new ArrayList<>();
        this.center = null;
        //distance of 2 vertex most far, is 2*radius of network, const
        distanceOfNetwork = (float) Math.sqrt((720 - 30) * (720 - 30) + (720 - 30) * (720 - 30));
    }

    public void setClone(List<Vertex> clone) {
        this.clone = clone;
    }

    public void simulateNetwork(Canvas canvas, int step) {
        if (clone.size() > 0) {
            switch (step) {
                case 1:
                    if (listBackbone.size() <= 0) {
                        listBackbone.addAll(TypeOfVertex.findBackbone(clone, 550));
                        for (Vertex bb : listBackbone) {
                            bb.setType(1);
                            graphObject.changeIconToCenterOrBackbone(bb);
                            graphView.invalidate();
                        }
                        Toast.makeText(graphView.getContext(), "Step 1: Backbone size: " + listBackbone.size(),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    if (listBackbone.size() > 0 && accessStep2 == 0) {
                        accessStep2++;
                        center = TypeOfVertex.findCenterNetwork(listBackbone);
                        center.setType(2);
                        graphObject.changeIconToCenterOrBackbone(center);
                        graphView.invalidate();
                        graphObject.drawMinimumPath(canvas, listBackbone);
                        Toast.makeText(graphView.getContext(), "Step 2: Vertex center: vertex " + center.getId(),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        //to do
                    }
                    break;
                case 3:
                    if (listBackbone.size() > 0 && accessStep3 == 0) {
                        for (Vertex backbone : listBackbone) {
                            accessStep3++;
                            List<Vertex> listConn = TypeOfVertex.findConnectorOfBackbone(backbone, clone, 550, 300);
                            graphObject.drawAreaCircle(canvas, backbone, 300);
                            if (listConn.size() > 0) {
                                graphObject.drawMinimumPath(canvas, listConn);
                            }
                        }
                        Toast.makeText(graphView.getContext(), "Step 3: find connector", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 4:
                    if(accessStep4 >0) break;
                    Toast.makeText(graphView.getContext(), "Step 4: abundant: " + clone.size(), Toast.LENGTH_SHORT).show();
                    while (clone.size() > 0) {
                        accessStep4++;
                        Log.d(ConfigGraph.TAG_LOG, "abundant: " + clone.size());
                        Vertex lastBackbone = TypeOfVertex.findBackboneLast(clone, center, 550, distanceOfNetwork);
                        List<Vertex> lastConnector = TypeOfVertex.findConnectorOfBackbone(lastBackbone, clone, 550, distanceOfNetwork);
                        if (lastConnector != null) {
                            graphObject.drawMinimumPath(canvas, lastConnector);
                        }
                        graphObject.drawMinimumPath(canvas, clone);
                    }
                    break;
            }
        } else {
            //to do
        }
    }
}
