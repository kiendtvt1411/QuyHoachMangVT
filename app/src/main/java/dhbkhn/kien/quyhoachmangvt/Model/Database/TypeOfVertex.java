package dhbkhn.kien.quyhoachmangvt.Model.Database;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dhbkhn.kien.quyhoachmangvt.Model.Object.Vertex;

/**
 * Created by kiend on 3/18/2017.
 */

public class TypeOfVertex {
    //first time, create clone of list vertex, then after action, remove backbone_orange, connector (verified) of list clone
    public static List<Vertex> findBackbone(List<Vertex> allVertex, float weight) {
        List<Vertex> listBackbone = new ArrayList<>();
        for (int i = 0; i < allVertex.size(); i++) {
            Vertex v = allVertex.get(i);
            if (v.getWeight() > weight) {
                listBackbone.add(v);
                allVertex.remove(v);
            }
        }
        return listBackbone;
    }

    //after find backbone_orange, then find connector
    public static List<Vertex> findConnectorOfBackbone(Vertex backbone, List<Vertex> withoutBackbone, float weight, float radius) {
        List<Vertex> listConnector = new ArrayList<>();
        if (withoutBackbone == null) return null;
        for (int i = 0; i < withoutBackbone.size(); i++) {
            Vertex v = withoutBackbone.get(i);
            float dx = v.getCoorX() - backbone.getCoorX();
            float dy = v.getCoorY() - backbone.getCoorY();
            float distance = (float) Math.sqrt(dx * dx + dy * dy);
            if (v.getWeight() <= weight && distance <= radius) {
                listConnector.add(v);
                withoutBackbone.remove(v);
            }
        }
        return listConnector;
    }

    //find center network in list backbone_orange
    public static Vertex findCenterNetwork(List<Vertex> listBackbone) {
        int indexOfCenter = -1;
        float min = 10000000f;
        for (Vertex v : listBackbone) {
            float sum = 0f;
            int indexOfV = listBackbone.indexOf(v);
            for (Vertex u : listBackbone) {
                int indexOfU = listBackbone.indexOf(u);
                sum += v.getCost()[indexOfU] * u.getWeight();
            }
            if (sum < min) {
                min = sum;
                indexOfCenter = indexOfV;
            }
        }
        if(indexOfCenter==-1) return null;
        return listBackbone.get(indexOfCenter);
    }

    //after find backbone_orange, center and connector -> only has that vertex did't verify in clone list
    public static Vertex findBackboneLast(List<Vertex> lastVertex, Vertex center, float weight, float distanceOfNetwork) {
        //syntax
        float min = 100000f;
        float sum;
        int indexBackbone = -1;
        if (lastVertex != null && center != null) {
            for (Vertex vertex : lastVertex) {
                int indexOfV = lastVertex.indexOf(vertex);
                int per = new Random().nextInt(10000);
                float pe = 1.0f * per / 10000;
                sum = pe * vertex.getCost()[center.getId()] / distanceOfNetwork + (1 - pe) * vertex.getWeight() / weight;
                if (sum < min) {
                    min = sum;
                    indexBackbone = indexOfV;
                }
            }
        }
        if (indexBackbone == -1) return null;
        Vertex lastBackbone = lastVertex.get(indexBackbone);
        lastVertex.remove(lastBackbone);
        return lastBackbone;
    }

    //find last connector, use findConnectorOfBackbone method with argument is vertex lastBackbone
}
