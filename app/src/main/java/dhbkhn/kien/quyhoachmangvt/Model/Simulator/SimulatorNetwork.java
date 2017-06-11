package dhbkhn.kien.quyhoachmangvt.Model.Simulator;

import android.graphics.Canvas;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import dhbkhn.kien.quyhoachmangvt.Config.ConfigGraph;
import dhbkhn.kien.quyhoachmangvt.Model.Database.TypeOfVertex;
import dhbkhn.kien.quyhoachmangvt.Model.Object.GraphObject;
import dhbkhn.kien.quyhoachmangvt.Model.Object.Vertex;
import dhbkhn.kien.quyhoachmangvt.View.DoThi.GraphView;

/**
 * Created by kiend on 3/19/2017.
 */

public class SimulatorNetwork {
    private GraphView graphView;
    private GraphObject graphObject;
    private int omega;
    private float alpha;

    private List<Vertex> clone;
    private List<Vertex> listBackbone;
    private Vertex center;
    private float distanceOfNetwork;

    private int access2 = 0;
    private int access3 = 0;
    private int access4 = 0;
    private int access5 = 0;

    public SimulatorNetwork(GraphObject graphObject, GraphView graphView, int omega, float alpha) {
        this.graphObject = graphObject;
        this.graphView = graphView;
        this.omega = omega;
        this.alpha = alpha;
        this.clone = new ArrayList<>();
        this.listBackbone = new ArrayList<>();
        this.center = null;
        //distance of 2 vertex most far, is 2*radius of network, const
        distanceOfNetwork = (float) Math.sqrt((720 - 30) * (720 - 30) + (720 - 30) * (720 - 30));
    }

    public void setOmega(int omega) {
        this.omega = omega;
    }

    public void setClone(List<Vertex> clone) {
        this.clone = clone;
    }

    public void simulateNetwork(Canvas canvas, int step) {
        switch (step) {
            case 1:
                if (listBackbone.size() <= 0) {
                    listBackbone.addAll(TypeOfVertex.findBackbone(clone, omega));
                    for (Vertex bb : listBackbone) {
                        bb.setType(2);
                        graphObject.changeIconToCenterOrBackbone(bb, listBackbone.indexOf(bb));
                        graphView.invalidate();
                    }
                    if (listBackbone.size() <= 0) {
                        Toast.makeText(graphView.getContext(), "Không có backbone nào, kết thúc thuật toán!", Toast.LENGTH_SHORT).show();
                        ConfigGraph.stepMode = false;
                        ConfigGraph.step = 0;
                        break;
                    }
                }
                break;
            case 2:
                if (access2 == 0) {
                    access2++;
                    center = TypeOfVertex.findCenterNetwork(listBackbone);
                    if (center == null) break;
                    center.setType(3);
                    graphObject.changeIconToCenterOrBackbone(center, 100);
                    graphView.invalidate();
                }
                break;
            case 3:
                if (access3 == 0) {
                    access3++;
                    for (Vertex backbone : listBackbone) {
                        List<Vertex> listConn = TypeOfVertex.findConnectorOfBackbone(backbone, clone, omega, 300);
                        for (Vertex v : listConn) {
                            if (v.getType() != 2 && v.getType() != 3) {
                                v.setType(1);
                                graphObject.changeIconToCenterOrBackbone(v, listBackbone.indexOf(backbone));
                            }
                        }
                    }
                    graphView.invalidate();
                }
                break;
            case 4:
                if (access4 == 0) {
                    access4++;
                    while (clone.size() > 0) {
                        Vertex lastBackbone = TypeOfVertex.findBackboneLast(clone, center, omega, distanceOfNetwork);
                        listBackbone.add(lastBackbone);
                        lastBackbone.setType(2);
                        graphObject.changeIconToCenterOrBackbone(lastBackbone, -1);
                        List<Vertex> lastConnector = TypeOfVertex.findConnectorOfBackbone(lastBackbone, clone, omega, distanceOfNetwork);
                        if (lastConnector != null) {
                            for (Vertex v : lastConnector) {
                                if (v.getType() != 2 && v.getType() != 3) {
                                    v.setType(1);
                                    graphObject.changeIconToCenterOrBackbone(v, -1);
                                }
                            }
                        }
                    }
                }
                break;
            case 5:
                graphObject.drawMinimumPath(canvas, listBackbone, alpha);
                break;
        }
    }

    private int calculateCostByDistance(Vertex v1, Vertex v2, int size){
        //cost is distance from this vertex to another vertexes -> don't use random
        int cost;
        double x1 = v1.getCoorX();
        double y1 = v1.getCoorY();
        double x2 = v2.getCoorX();
        double y2 = v2.getCoorY();
        cost = (int) ((Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)) + 2500) / 5 * (size / 50) / 0.8);
        return cost;
    }
}
