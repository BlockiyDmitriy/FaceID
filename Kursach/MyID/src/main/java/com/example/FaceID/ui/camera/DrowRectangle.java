package com.example.FaceID.ui.camera;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.SparseArray;

import com.google.android.gms.vision.face.Face;

public class DrowRectangle {
    private SparseArray<Face> faces;
    private Canvas canvas;
    private Paint rectPaint;

    public DrowRectangle(SparseArray<Face> faces, Canvas tempCanvas, Paint myRectPaint){
        this.faces = faces;
        this.canvas = tempCanvas;
        this.rectPaint = myRectPaint;
        Drow();
    }

    private void Drow() {
        for (int i = 0; i < faces.size(); i++) {
            Face thisFace = faces.valueAt(i);
            float x1 = thisFace.getPosition().x;
            float y1 = thisFace.getPosition().y;
            float x2 = x1 + thisFace.getWidth();
            float y2 = y1 + thisFace.getHeight();
            canvas.drawRoundRect(new RectF(x1, y1, x2, y2), 2, 2, rectPaint);
        }
    }
}
