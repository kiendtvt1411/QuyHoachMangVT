package dhbkhn.kien.quyhoachmangvt.View.DoThi;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import dhbkhn.kien.quyhoachmangvt.Model.Object.GraphObject;

/**
 * Created by kiend on 3/16/2017.
 */

public class GraphView extends SurfaceView implements SurfaceHolder.Callback {
    private GestureDetector translate;
    private ScaleGestureDetector scale;
    private GraphObject graphObject;

    public GraphView(Context context) {
        super(context);
        this.setBackgroundColor(Color.WHITE);
        ScaleListener scaleListener = new ScaleListener(this);
        scale = new ScaleGestureDetector(context,scaleListener);
        TranslateListener translateListener = new TranslateListener(this);
        translate = new GestureDetector(context,translateListener,null,true);
        graphObject = new GraphObject(this, 50);
        this.setFocusable(true);
        this.getHolder().addCallback(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.graphObject.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean retVal = scale.onTouchEvent(event) && translate.onTouchEvent(event);
        return retVal;
    }

    private class ScaleListener implements ScaleGestureDetector.OnScaleGestureListener {
        GraphView view;

        public ScaleListener(GraphView view) {
            this.view = view;
        }

        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            float factor = scaleGestureDetector.getScaleFactor();
            graphObject.scaleView(factor);
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {

        }
    }

    private class TranslateListener extends GestureDetector.SimpleOnGestureListener{
        GraphView view;

        public TranslateListener(GraphView view) {
            this.view = view;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            float dx = e.getX();
            float dy = e.getY();
            int result = graphObject.checkInsideOfVertex(dx, dy);
            if (result < 0) {
                //to to
            }else{
                Toast.makeText(getContext(), "You touch inside vertex " + result, Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            graphObject.translateView(-distanceX, -distanceY);
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            graphObject.flingView(velocityX,velocityY);
            return true;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
}
