package dhbkhn.kien.quyhoachmangvt.Model.Database;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dhbkhn.kien.quyhoachmangvt.Model.Object.Vertex;

/**
 * Created by kiend on 3/16/2017.
 */

public class ProcessingRandom {
    private int size;

    public ProcessingRandom(int size) {
        this.size = size;
    }

    public List<Vertex> randomGraph(){
        List<Vertex> listVertex = new ArrayList<>();
        for(int i = 0;i<size;i++){
            Vertex rdVertex = randomVertex();
            if (i == 0) {
                rdVertex.setCoorX(30);
                rdVertex.setCoorY(30);
            } else if (i == size - 1) {
                rdVertex.setCoorX(720);
                rdVertex.setCoorY(720);
            }
            listVertex.add(rdVertex);
        }
        for(Vertex v: listVertex){
            //initiate erlangIn, cost, adjacency of vertex
            int[] erlangIns = new int[size];
            int[] costs = new int[size];
            List<Vertex> adjVertex = new ArrayList<>();

            int indexOfV = listVertex.indexOf(v);
            for(Vertex u: listVertex){
                int indexOfU = listVertex.indexOf(u);
                //for erlangIn
                Random rd = new Random();
                int erlangIn = rd.nextInt(10) + 1;
                if(indexOfU == indexOfV) erlangIn = 0;
                erlangIns[indexOfU] = erlangIn;
                //for cost(distance)
                int costFromVtoU = calculateCostByDistance(v,u);
                costs[indexOfU] = costFromVtoU;
                //for adjacency of vertex v
                if(indexOfU>=0 && indexOfU!=indexOfV){
                    adjVertex.add(u);
                }
            }

            v.setId(indexOfV);
            v.setErlangIn(erlangIns);
            v.setCost(costs);
            v.setAdjVertex(adjVertex);
            //calculate weight of vertex
            int weightIn = this.calculateWeightInVertex(v);
            int weightOut = this.calculateWeightOutVertex(v);
            v.setWeight(weightIn + weightOut);
        }
        return listVertex;
    }

    private Vertex randomVertex(){
        //first, random coordinate of vertex onto screen
        Random rd = new Random();
        Random rd2 = new Random();
        Random rd3 = new Random();
        Random rd4 = new Random();
        float coorX = (rd.nextInt(150) + 30) * (rd2.nextInt(4) + 1);
        float coorY = (rd3.nextInt(100) + 30)  * (rd4.nextInt(6) + 1);
        Vertex vertex = new Vertex(coorX,coorY);
        vertex.setVisited(false);
        vertex.setPrevVertex(null);
        vertex.setErlangOut(randomErlangOut(vertex));
        return vertex;
    }

    private int calculateCostByDistance(Vertex v1, Vertex v2){
        //cost is distance from this vertex to another vertexes -> don't use random
        int cost;
        double x1 = v1.getCoorX();
        double y1 = v1.getCoorY();
        double x2 = v2.getCoorX();
        double y2 = v2.getCoorY();
        cost = (int) ((Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)) + 2500) / 5 * (size / 50) / 0.8);
        return cost;
    }

    private int[] randomErlangOut(Vertex v){
        int[] erlangOuts = new int[size];
        for(int i = 0;i<size;i++){
            Random rd = new Random();
            int er = rd.nextInt(10) + 1; //from 1->10
            erlangOuts[i] = er;
            if(v.getId() == i){
                erlangOuts[i] = 0; //erlangs for itself
            }
        }
        return erlangOuts;
    }

    private int calculateWeightOutVertex(Vertex v){
        int weightOut = 0;
        int[] erlangOuts = v.getErlangOut();
        if (erlangOuts != null) {
            for(int i = 0;i<size;i++){
                weightOut += erlangOuts[i];
            }
        }
        return weightOut;
    }

    private int calculateWeightInVertex(Vertex v){
        int weightIn = 0;
        int[] erlangIns = v.getErlangIn();
        if (erlangIns != null) {
            for(int i = 0;i<size;i++) {
                weightIn += erlangIns[i];
            }
        }
        return weightIn;
    }
}
