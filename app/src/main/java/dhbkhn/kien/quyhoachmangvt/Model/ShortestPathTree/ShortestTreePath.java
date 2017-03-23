package dhbkhn.kien.quyhoachmangvt.Model.ShortestPathTree;

import java.util.ArrayList;
import java.util.List;

import dhbkhn.kien.quyhoachmangvt.Model.Object.Vertex;


/**
 * Created by kiend on 3/15/2017.
 */

public class ShortestTreePath {
    private List<Vertex> listVertex;
    private List<Vertex> listResult;

    public ShortestTreePath(List<Vertex> listVertex) {
        this.listVertex = listVertex;
        this.listResult = new ArrayList<>();
    }

    public List<Vertex> dijkstraAlgorithm(Vertex start){
        this.listResult.clear();
        //initiate
        for(Vertex v: listVertex){
            v.setWeight(100000);
            v.setVisited(false);
            v.setPrevVertex(null);
        }
        start.setWeight(0);
        List<Vertex> clone = new ArrayList<>();
        clone.addAll(listVertex);
        while (clone != null) {
            Vertex u = findNextVertex(clone);
            if(u==null) return this.listResult;
            u.setVisited(true);
            clone.remove(u);
            this.listResult.add(u);
            for(Vertex v: u.getAdjVertex()){
                if (!v.isVisited()) {
                    int indexOfV = listVertex.indexOf(v);
                    int costFromUtoV = u.getCost()[indexOfV];
                    int distanceU = u.getWeight();
                    int distanceV = v.getWeight();
                    if(distanceV > distanceU + costFromUtoV){
                        v.setWeight(distanceU + costFromUtoV);
                        v.setPrevVertex(u);
                    }
                }
            }
        }
        return this.listResult;
    }

    private Vertex findNextVertex(List<Vertex> clone){
        int minOfIndex = -1;
        int minDist = 100000;
        for(Vertex v: clone){
            if(v.getWeight() < minDist){
                minDist = v.getWeight();
                minOfIndex = listVertex.indexOf(v);//not in list clone
            }
        }
        if(minOfIndex==-1) return null;
        Vertex vertex = listVertex.get(minOfIndex);
        return vertex;
    }
}
