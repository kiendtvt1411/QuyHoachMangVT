package dhbkhn.kien.quyhoachmangvt.Model.MinimumSpanningTree;

import java.util.ArrayList;
import java.util.List;

import dhbkhn.kien.quyhoachmangvt.Model.Object.Vertex;


/**
 * Created by kiend on 3/15/2017.
 */

public class MinimumSpanningTree {
    private List<Vertex> listVertex;
    private List<Vertex> listResult;
    private int[] cost;

    public MinimumSpanningTree(List<Vertex> listVertex) {
        this.listVertex = listVertex;
        int sizeVertex = listVertex.size();
        this.listResult = new ArrayList<>();
        this.cost = new int[sizeVertex];
    }

    public List<Vertex> primeAlgorithm(Vertex start){
        this.listResult.clear();
        //initiate value
        for(Vertex v: this.listVertex){
            int indexOfV = listVertex.indexOf(v);
            v.setVisited(false);
            v.setPrevVertex(null);
            cost[indexOfV] = 100000;
        }

        int indexOfStart = listVertex.indexOf(start);
        cost[indexOfStart] = 0;

        int sizeVertex = listVertex.size();
        for(int i = 0;i<sizeVertex;i++){
            Vertex u = findVertexNextContected();
            u.setVisited(true);
            for(Vertex v: this.listVertex){
                int indexOfV = listVertex.indexOf(v);
                if(!v.isVisited() && u.getCost()[indexOfV] < cost[indexOfV]){
                    cost[indexOfV] = u.getCost()[indexOfV];
                    v.setPrevVertex(u);
                }
            }
            this.listResult.add(u);
        }
        return this.listResult;
    }

    private Vertex findVertexNextContected(){
        int minEdge = 100000;
        int minOfIndex = -1;
        for(Vertex v: listVertex){
            int indexOfV = listVertex.indexOf(v);
            if(cost[indexOfV] < minEdge && !v.isVisited()){
                minEdge = cost[indexOfV];
                minOfIndex = indexOfV;
            }
        }
        if(minOfIndex == -1) return null;
        Vertex vertex = listVertex.get(minOfIndex);
        return vertex;
    }
}
