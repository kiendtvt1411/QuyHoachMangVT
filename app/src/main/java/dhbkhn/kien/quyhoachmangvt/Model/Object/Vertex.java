package dhbkhn.kien.quyhoachmangvt.Model.Object;

import java.util.List;

import dhbkhn.kien.quyhoachmangvt.Config.ConfigGraph;

/**
 * Created by kiend on 3/16/2017.
 */

public class Vertex {
    int id; //according to id can determine vertex.getCost[idA], that is cost from vertex to the vertexA has idA
    int type;//connector, backbone, center
    private int[] cost;
    private int[] erlangOut;
    private int[] erlangIn;
    private int weight;
    private int distance; //only use for find minimum path
    private Vertex prevVertex;
    private List<Vertex> adjVertex;
    private boolean visited; //only use for find minimum path
    private float coorX;
    private float coorY;

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int[] getErlangIn() {
        return erlangIn;
    }

    public void setErlangIn(int[] erlangIn) {
        this.erlangIn = erlangIn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int[] getCost() {
        return cost;
    }

    public void setCost(int[] cost) {
        this.cost = cost;
    }

    public int[] getErlangOut() {
        return erlangOut;
    }

    public void setErlangOut(int[] erlangOut) {
        this.erlangOut = erlangOut;
    }

    public Vertex getPrevVertex() {
        return prevVertex;
    }

    public void setPrevVertex(Vertex prevVertex) {
        this.prevVertex = prevVertex;
    }

    public List<Vertex> getAdjVertex() {
        return adjVertex;
    }

    public void setAdjVertex(List<Vertex> adjVertex) {
        this.adjVertex = adjVertex;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public float getCoorX() {
        return coorX;
    }

    public void setCoorX(float coorX) {
        this.coorX = coorX;
    }

    public float getCoorY() {
        return coorY;
    }

    public void setCoorY(float coorY) {
        this.coorY = coorY;
    }

    public Vertex(float coorX, float coorY) {
        //coorX, coorY is coordinate of this vertex in screen
        this.coorX = coorX;
        this.coorY = coorY;
        this.type = 0;//connector
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Vertex ").append(String.valueOf(this.getId()))
                .append(": ")
                .append("\nCoordinate: ")
                .append(String.valueOf(this.getCoorX()))
                .append(", ")
                .append(String.valueOf(this.getCoorY()))
                .append("\nCost: ");
        int size = ConfigGraph.sizeOfVertex;
        for(int i = 0;i<size;i++){
            sb.append(String.valueOf(this.getCost()[i])).append(", ");
        }
        sb.append("\nWeight: ")
                .append(String.valueOf(this.getWeight()));
        return sb.toString();
    }
}
