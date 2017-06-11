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
    private float alpha;

    public ShortestTreePath(List<Vertex> listVertex, float alpha) {
        this.listVertex = listVertex;
        this.listResult = new ArrayList<>();
        this.alpha = alpha;
    }

    public int getLengthDistance() {
        int lengthMin = 0;
        for (Vertex v : listResult) {
            int dist = v.getDistance();
            lengthMin += dist;
        }
        return lengthMin;
    }

    public List<Vertex> dijkstraAlgorithm(Vertex start, Vertex end){
        this.listResult.clear();
        //initiate
        for(Vertex v: listVertex){
            v.setDistance(100000);
            v.setVisited(false);
            v.setPrevVertex(null);
        }
        start.setDistance(0);
        List<Vertex> clone = new ArrayList<>();
        clone.addAll(listVertex);
        while (clone != null) {
            Vertex u = findNextVertex(clone);
            if(u==null) return this.listResult;
            if(u == end) {
                this.listResult.add(u);
                break;
            }
            u.setVisited(true);
            clone.remove(u);
            this.listResult.add(u);
            for(Vertex v: u.getAdjVertex()){
                int indexOfV = listVertex.indexOf(v);
                if (!v.isVisited() && indexOfV >= 0) {
                    int costFromUtoV = u.getCost()[indexOfV];
                    int distanceU = u.getDistance();
                    int distanceV = v.getDistance();
                    if(distanceV > (int) (distanceU + alpha * costFromUtoV)){
                        v.setDistance((int) (distanceU + alpha * costFromUtoV));
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
            if(v.getDistance() < minDist){
                minDist = v.getDistance();
                minOfIndex = listVertex.indexOf(v);//not in list clone
            }
        }
        if(minOfIndex==-1) return null;
        Vertex vertex = listVertex.get(minOfIndex);
        return vertex;
    }
}
