package dhbkhn.kien.quyhoachmangvt.Model.Database;

import java.util.List;

import dhbkhn.kien.quyhoachmangvt.Model.Object.Vertex;

/**
 * Created by kiend on 3/27/2017.
 */

public class CalculateMinMax {
    private List<Vertex> listVertex;
    private float gamma;

    public CalculateMinMax(List<Vertex> listVertex, float gamma) {
        this.listVertex = listVertex;
        this.gamma = gamma;
    }

    private int findMinCostOfList(Vertex vertex){
        int minCost = 1000;
        int[] listCost = vertex.getCost();
        int length = listCost.length;
        for(int i = 0; i < length; i++) {
            if (listCost[i] < minCost && listCost[i] != 0) {
                minCost = listCost[i];
            }
        }
        return minCost;
    }

    private int findMinCostOfGraph() {
        int minCost = 1000;
        for (Vertex v : listVertex) {
            int minV = findMinCostOfList(v);
            if (minV < minCost) {
                minCost = minV;
            }
        }
        return minCost;
    }

    public int minWeightOfGraph(){
        int minCost = findMinCostOfGraph();
        int minWei = (int) (minCost * gamma);
        return minWei;
    }

    public int maxWeightOfGraph(){
        int maxCost;
        Vertex firstV = listVertex.get(0);
        maxCost = firstV.getCost()[firstV.getCost().length - 1];
        int maxWei = (int) (maxCost * gamma);
        return maxWei;
    }
}
