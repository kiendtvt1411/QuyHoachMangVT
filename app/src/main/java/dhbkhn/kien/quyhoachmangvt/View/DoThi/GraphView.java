package dhbkhn.kien.quyhoachmangvt.View.DoThi;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import dhbkhn.kien.quyhoachmangvt.Config.ConfigGraph;
import dhbkhn.kien.quyhoachmangvt.Model.Object.GraphObject;
import dhbkhn.kien.quyhoachmangvt.Model.Object.Vertex;
import dhbkhn.kien.quyhoachmangvt.R;
import dhbkhn.kien.quyhoachmangvt.View.ThongSo.ArgumentActivity;

/**
 * Created by kiend on 3/16/2017.
 */

public class GraphView extends SurfaceView implements SurfaceHolder.Callback {
    private GestureDetector translate;
    private ScaleGestureDetector scale;
    private GraphObject graphObject;
    private Dialog dialog;
    private float alpha;
    private int omega;
    private float gamma;
    private int idStart = -1;
    private int idEnd = -1;

    public GraphView(Context context, int size, int omega, float alpha, float gamma) {
        super(context);
        this.alpha = alpha;
        this.omega = omega;
        this.gamma = gamma;
        this.setBackgroundColor(Color.WHITE);
        ScaleListener scaleListener = new ScaleListener(this);
        scale = new ScaleGestureDetector(context, scaleListener);
        TranslateListener translateListener = new TranslateListener(this);
        translate = new GestureDetector(context, translateListener, null, true);
        graphObject = new GraphObject(this, size, omega, alpha);
        this.setFocusable(true);
        this.getHolder().addCallback(this);
    }

    private void displayDialog(final Vertex vertex) {
        dialog = new Dialog(this.getContext(), R.style.FullHeightDialog);
        final LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewDialog = inflater.inflate(R.layout.custom_view_dialog, null);
        TextView tvDialogId = (TextView) viewDialog.findViewById(R.id.tvDialogId);
        TextView tvDialogCoor = (TextView) viewDialog.findViewById(R.id.tvDialogCoor);
        TextView tvDialogType = (TextView) viewDialog.findViewById(R.id.tvDialogType);
        TextView tvDialogWeight = (TextView) viewDialog.findViewById(R.id.tvDialogWeight);
        TextView tvDialogAlpha = (TextView) viewDialog.findViewById(R.id.tvDialogAlpha);
        TextView tvDialogOmega = (TextView) viewDialog.findViewById(R.id.tvDialogOmega);
        TextView tvDialogGamma = (TextView) viewDialog.findViewById(R.id.tvDialogGamma);
        TextView tvDialogDetail = (TextView) viewDialog.findViewById(R.id.tvDialogDetail);
        final EditText edtDialogFind = (EditText) viewDialog.findViewById(R.id.edtDialogFind);
        Button btnDialogFind = (Button) viewDialog.findViewById(R.id.btnDialogFind);
        Button btnDialogClose = (Button) viewDialog.findViewById(R.id.btnDialogClose);

        tvDialogId.setText(String.valueOf(vertex.getId()));
        tvDialogCoor.setText(String.format("%.2f", vertex.getCoorX()) + ", " + String.format("%.2f", vertex.getCoorY()));
        if (vertex.getType() == 0) {
            tvDialogType.setText("Not connected");
        } else if (vertex.getType() == 1) {
            tvDialogType.setText("Connector");
        } else if (vertex.getType() == 2) {
            tvDialogType.setText("Backbone");
        } else {
            tvDialogType.setText("Center");
        }
        tvDialogWeight.setText(String.valueOf(vertex.getWeight()));
        tvDialogAlpha.setText(String.format("%.2f",alpha));
        tvDialogGamma.setText(String.format("%.2f",gamma));
        tvDialogOmega.setText(String.valueOf(ConfigGraph.mOmega));

        tvDialogDetail.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iDetail = new Intent(getContext(), ArgumentActivity.class);
                iDetail.putExtra("id", vertex.getId());
                iDetail.putExtra("alpha", alpha);
                iDetail.putExtra("weight", ConfigGraph.mOmega);
                iDetail.putExtra("gamma", gamma);
                getContext().startActivity(iDetail);
            }
        });

        btnDialogClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnDialogFind.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (edtDialogFind.getText().toString().trim() != null
                        || !edtDialogFind.getText().toString().trim().equals("")) {
                    idEnd = Integer.parseInt(edtDialogFind.getText().toString().trim());
                    idStart = vertex.getId();
                    ConfigGraph.findPathBetweenTwoVertex = true;
                    invalidate();
                }
            }
        });

        dialog.setContentView(viewDialog);
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (ConfigGraph.findPathBetweenTwoVertex && idStart != -1 && idEnd != -1) {
            graphObject.drawMinimumPathBetweenTwoVertex(canvas, GraphObject.listVertex.get(idStart),
                    GraphObject.listVertex.get(idEnd), alpha);
        }
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

    private class TranslateListener extends GestureDetector.SimpleOnGestureListener {
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
            } else {
                Vertex v = GraphObject.listVertex.get(result);
                displayDialog(v);
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
            graphObject.flingView(velocityX, velocityY);
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
