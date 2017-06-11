package dhbkhn.kien.quyhoachmangvt.Model.Object;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

import dhbkhn.kien.quyhoachmangvt.Config.ConfigGraph;
import dhbkhn.kien.quyhoachmangvt.Model.Database.AsyncGraph;
import dhbkhn.kien.quyhoachmangvt.Model.MinimumSpanningTree.MinimumSpanningTree;
import dhbkhn.kien.quyhoachmangvt.Model.ShortestPathTree.ShortestTreePath;
import dhbkhn.kien.quyhoachmangvt.Model.Simulator.SimulatorNetwork;
import dhbkhn.kien.quyhoachmangvt.View.DoThi.GraphView;

/**
 * Created by kiend on 3/17/2017.
 */
/*
* Issue of this project:
* + bitmap translate faster than path
* + ??? how to solve this issue
* + don't use matrix to draw bitmap -> change by rect?
* */
public class GraphObject {
    private GraphView graphView;
    private int size;
    private Paint mPaint;
    public static List<Vertex> listVertex;
    private List<Matrix> matrixBitmap; //one bitmap has one matrix bitmap
    private List<Bitmap> listIcon;
    private float currentDx;
    private float currentDy;
    private float alpha;
    private float rate = 1.0f;
    //smoothly
    private Interpolator interpolator;
    private long startTime;
    private long endTime;
    //simulator networking
    private SimulatorNetwork simulatorNetwork;
    //choose mode
    private static int CHOOSE_MODE = 0;//initiate choose, if = 1 -> scale, if = 2 -> translate
    private static int SCALE_GRAPH = 1;
    private static int TRANSLATE_GRAPH = 2;
    //size of bitmap (all bitmaps has similar size 24x24)
    private static final float widthBitmap = 24.0f;
    private static final float heightBitmap = 24.0f;

    public GraphObject(GraphView graphView, int size, int omega, float alpha) {
        this.graphView = graphView;
        this.size = size;
        this.alpha = alpha;
        this.listVertex = new ArrayList<>();
        this.simulatorNetwork = new SimulatorNetwork(this, graphView, omega, alpha);
        generateGraph();
        initBitmap(graphView.getContext());
        initPaint();
    }

    private void initBitmap(Context context) {
        matrixBitmap = new ArrayList<>();
        listIcon = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                    ConfigGraph.getDrawableIdResByName(context, ConfigGraph.AN_BN_BLUE[0]));
            listIcon.add(bitmap);
            Matrix matrix = new Matrix();
            matrixBitmap.add(matrix);
        }
    }

    public void draw(Canvas canvas) {
        drawBitmapToSurfaceView(canvas);
        if (listVertex.size() <= 0) {
            graphView.invalidate();
        } else if (ConfigGraph.stepMode) {
            simulator(canvas, ConfigGraph.step);
        } else if (ConfigGraph.findPath) {
            drawMinimumPath(canvas, alpha);
        };
    }

    //if touch inside one of vertices, return id of vertex, else, return -1
    public int checkInsideOfVertex(float dx, float dy) {
        dx = dx * rate + currentDx;
        dy = dy * rate + currentDy;
        float radius = (float) Math.sqrt(widthBitmap * widthBitmap + heightBitmap * heightBitmap) * rate;
        for (Vertex v : listVertex) {
            float coorX = v.getCoorX();
            float coorY = v.getCoorY();
            float distance = (float) Math.sqrt((dx - coorX) * (dx - coorX) + (dy - coorY) * (dy - coorY));
            if (distance < radius) {
                return v.getId();
            }
        }
        return -1;
    }

    public void changeIconToCenterOrBackbone(Vertex vertex, int indexBackBone) {
        //find index of vertex in listVertex original
        int indexOfVertex = listVertex.indexOf(vertex);

        if (indexOfVertex >= 0) {
            int typeVertex = vertex.getType();
            String nameRes = null;
            int argu = indexBackBone % 5;
            switch (argu) {
                case -1:
                    nameRes = ConfigGraph.AN_BN_LAST[typeVertex];
                    break;
                case 0:
                    nameRes = ConfigGraph.AN_BN_BLUE[typeVertex];
                    break;
                case 1:
                    nameRes = ConfigGraph.AN_BN_DK_BLUE[typeVertex];
                    break;
                case 2:
                    nameRes = ConfigGraph.AN_BN_GREEN[typeVertex];
                    break;
                case 3:
                    nameRes = ConfigGraph.AN_BN_ORANGE[typeVertex];
                    break;
                case 4:
                    nameRes = ConfigGraph.AN_BN_PINK[typeVertex];
                    break;
                case 100:
                    nameRes = ConfigGraph.AN_BN_RED[typeVertex];
                    break;
                default:
                    nameRes = ConfigGraph.AN_BN_BLUE[typeVertex];
                    break;
            }
            Bitmap bitmap = BitmapFactory.decodeResource(graphView.getResources(),
                    ConfigGraph.getDrawableIdResByName(graphView.getContext(), nameRes));
            listIcon.remove(indexOfVertex);
            listIcon.add(indexOfVertex, bitmap);
        }
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(10);
        mPaint.setColor(Color.RED);
    }

    public void generateGraph() {
        AsyncGraph task = new AsyncGraph(graphView, listVertex, simulatorNetwork);
        task.execute(size);
    }

    //draw area effection of backbone_orange vertex
    public void drawAreaCircle(Canvas canvas, Vertex backbone, float radius) {
        mPaint.setColor(Color.parseColor("#abefefef"));
        mPaint.setStrokeWidth(2);
        canvas.drawCircle(backbone.getCoorX(), backbone.getCoorY(), radius, mPaint);
    }

    public void scaleView(float factor) {
        CHOOSE_MODE = SCALE_GRAPH;
        rate = factor;
        rate = Math.max(0.8f, Math.min(rate, 3.0f));
        if (listVertex.size() <= 0) {
            return;
        }
        for (Matrix scale : matrixBitmap) {
            scale.preScale(rate, rate);
            graphView.invalidate();
        }
    }

    public void translateView(float dx, float dy) {
        CHOOSE_MODE = TRANSLATE_GRAPH;
        currentDx = dx;
        currentDy = dy;
        if (listVertex.size() <= 0) {
            return;
        }
        for (Matrix translate : matrixBitmap) {
            translate.postTranslate(currentDx, currentDy);
            graphView.invalidate();
        }
    }

    public void drawBitmapToSurfaceView(Canvas canvas) {
        for (Vertex vertex : listVertex) {
            int indexOfVertex = listVertex.indexOf(vertex);
//            Matrix matrixBm = matrixBitmap.get(indexOfVertex);
//            Matrix transform = new Matrix(matrixBm);//new matrix
            Bitmap droid = listIcon.get(indexOfVertex);
//            transform.postTranslate(-widthBitmap, -heightBitmap); // Centers image
//            transform.postConcat(matrixBm);
            float vx, vy;
            if (CHOOSE_MODE == TRANSLATE_GRAPH) {
                vx = vertex.getCoorX() + currentDx;
                vy = vertex.getCoorY() + currentDy;
            } else if (CHOOSE_MODE == SCALE_GRAPH) {
                vx = vertex.getCoorX() * rate;
                vy = vertex.getCoorY() * rate;
            } else {
                vx = vertex.getCoorX();
                vy = vertex.getCoorY();
            }
//            transform.postTranslate(vx, vy);
            vertex.setCoorX(vx);
            vertex.setCoorY(vy);
//            canvas.drawBitmap(droid, transform, null);
            canvas.drawBitmap(droid, vx - widthBitmap / 2, vy - heightBitmap / 2, mPaint); //good!!!!!!!!!!!!!!
        }
    }

    public void drawMinimumPath(Canvas canvas, float alpha) {
        if (listVertex.size() <= 0) {
            return;
        }
        mPaint.setStrokeWidth(5 * rate);
        mPaint.setStyle(Paint.Style.STROKE);
        List<Vertex> result = new ArrayList<>();
        if (alpha == 0f) {
            MinimumSpanningTree minimumSpanningTree = new MinimumSpanningTree(listVertex);
            result = minimumSpanningTree.primeAlgorithm(listVertex.get(0),null);
        }
        else{
            ShortestTreePath shortestTreePath = new ShortestTreePath(listVertex, alpha);
            result = shortestTreePath.dijkstraAlgorithm(listVertex.get(0), null);
        }
        Path path = new Path();
        for (Vertex v : result) {
            int index = result.indexOf(v);
            float vx = v.getCoorX();
            float vy = v.getCoorY();
            if (index == 0) {
                path.moveTo(vx, vy);
            } else {
                path.lineTo(vx, vy);
            }
            v.setCoorX(vx);
            v.setCoorY(vy);
        }
        canvas.drawPath(path, mPaint);
    }

    public void flingView(float velocityX, float velocityY) {
        if (listVertex.size() <= 0) {
            return;
        }
        final float distanceTimeFactor = 0.4f; //using 40% second = 400 milliseconds
        final float totalDx = (distanceTimeFactor * velocityX / 2);
        final float totalDy = (distanceTimeFactor * velocityY / 2);
        animateMove(totalDx, totalDy, (long) (distanceTimeFactor * 1000));
        graphView.invalidate();
    }

    //animation fling
    public void animateMove(float dx, float dy, long duration) {
        interpolator = new LinearInterpolator();
        startTime = System.currentTimeMillis();
        endTime = startTime + duration;
        currentDx = dx;
        currentDy = dy;
        this.graphView.post(new Runnable() {
            @Override
            public void run() {
                animateStep();
            }
        });
    }

    private void animateStep() {
        long curTime = System.currentTimeMillis();
        float perTime = (curTime - startTime) / (endTime - startTime);
        float perDist = interpolator.getInterpolation(perTime);
        float dx = currentDx * perDist;
        float dy = currentDy * perDist;
        translateView(dx, dy);
        if (perTime < 1.0f) {
            this.graphView.post(new Runnable() {
                @Override
                public void run() {
                    animateStep();
                }
            });
        }
    }

    public void simulator(Canvas canvas, int step) {
        if (simulatorNetwork != null) {
            simulatorNetwork.setOmega(ConfigGraph.mOmega);
            simulatorNetwork.simulateNetwork(canvas, step);
        }
    }

    public void drawMinimumPathBetweenTwoVertex(Canvas canvas, Vertex start, Vertex end, float alpha) {
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(5 * rate);
        List<Vertex> result = new ArrayList<>();
        if (alpha == 0f) {
            MinimumSpanningTree minimumSpanningTree = new MinimumSpanningTree(listVertex);
            result = minimumSpanningTree.primeAlgorithm(start, end);
        }else{
            ShortestTreePath shortestTreePath = new ShortestTreePath(listVertex, alpha);
            result = shortestTreePath.dijkstraAlgorithm(start, end);
        }
        if (result == null) return;
        for (Vertex v : result) {
            Vertex u = v.getPrevVertex();
            if (u != null) {
                float vx = v.getCoorX();
                float vy = v.getCoorY();
                float ux = u.getCoorX();
                float uy = u.getCoorY();
                canvas.drawLine(ux, uy, vx, vy, mPaint);
            }
        }
    }

    public void drawMinimumPath(Canvas canvas, List<Vertex> clone, float alpha) {
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(5 * rate);
        if (clone.size() > 0) {
            List<Vertex> result = new ArrayList<>();
            if (alpha == 0) {
                MinimumSpanningTree minimumSpanningTree = new MinimumSpanningTree(clone);
                result = minimumSpanningTree.primeAlgorithm(clone.get(0),null);
                if (ConfigGraph.minLengthPath != minimumSpanningTree.getMinCost()) {
                    ConfigGraph.minLengthPath = minimumSpanningTree.getMinCost();
                }
            }else{
                ShortestTreePath shortestTreePath = new ShortestTreePath(clone, alpha);
                result = shortestTreePath.dijkstraAlgorithm(clone.get(0), null);
                if (ConfigGraph.minLengthPath != shortestTreePath.getLengthDistance()) {
                    ConfigGraph.minLengthPath = shortestTreePath.getLengthDistance();
                }
            }
            if (result == null) return;
            for (Vertex v : result) {
                Vertex u = v.getPrevVertex();
                if (u != null) {
                    float vx = v.getCoorX();
                    float vy = v.getCoorY();
                    float ux = u.getCoorX();
                    float uy = u.getCoorY();
                    canvas.drawLine(ux, uy, vx, vy, mPaint);
                }
            }
        }
    }
}
