package com.example.bikemap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ZoomControls;

import androidx.appcompat.app.AppCompatActivity;

public class HomepageActivity extends AppCompatActivity {

    private ImageView imageView;
    private ZoomControls zoomControls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        // Get a reference to the ImageView in the layout
        imageView = findViewById(R.id.mapImage);

        // Get a reference to the ZoomControls in the layout
        zoomControls = findViewById(R.id.zoomControls);

        // Get the JPEG file path from the intent that started this activity
        String jpegFilePath = getIntent().getStringExtra("image_path");

        // Load the JPEG file into a Bitmap
        Bitmap bitmap = BitmapFactory.decodeFile(jpegFilePath);

        // Set the Bitmap as the content of the ImageView
        imageView.setImageBitmap(bitmap);

        // Set an onZoomInClickListener on the ZoomControls
        zoomControls.setOnZoomInClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the current scale type of the ImageView
                ImageView.ScaleType scaleType = imageView.getScaleType();

                // If the current scale type is FIT_CENTER, change it to MATRIX and set the matrix scale factor to 1.25
                if (scaleType == ImageView.ScaleType.FIT_CENTER) {
                    imageView.setScaleType(ImageView.ScaleType.MATRIX);
                    float[] matrixValues = new float[9];
                    imageView.getImageMatrix().getValues(matrixValues);
                    matrixValues[0] *= 1.25f;
                    matrixValues[4] *= 1.25f;
                    imageView.getImageMatrix().setValues(matrixValues);
                }
                // If the current scale type is MATRIX, multiply the matrix scale factor by 1.25
                else if (scaleType == ImageView.ScaleType.MATRIX) {
                    float[] matrixValues = new float[9];
                    imageView.getImageMatrix().getValues(matrixValues);
                    matrixValues[0] *= 1.25f;
                    matrixValues[4] *= 1.25f;
                    imageView.getImageMatrix().setValues(matrixValues);
                }
                // Invalidate the ImageView to redraw it
                imageView.invalidate();
            }
        });

// Set an onZoomOutClickListener on the ZoomControls
        zoomControls.setOnZoomOutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the current scale type of the ImageView
                ImageView.ScaleType scaleType = imageView.getScaleType();

                // If the current scale type is FIT_CENTER, change it to MATRIX and set the matrix scale factor to 0.8
                if (scaleType == ImageView.ScaleType.FIT_CENTER) {
                    imageView.setScaleType(ImageView.ScaleType.MATRIX);
                    float[] matrixValues = new float[9];
                    imageView.getImageMatrix().getValues(matrixValues);
                    matrixValues[0] *= 0.8f;
                    matrixValues[4] *= 0.8f;
                    imageView.getImageMatrix().setValues(matrixValues);
                }
                // If the current scale type is MATRIX, multiply the matrix scale factor by 0.8
                else if (scaleType == ImageView.ScaleType.MATRIX) {
                    float[] matrixValues = new float[9];
                    imageView.getImageMatrix().getValues(matrixValues);
                    matrixValues[0] *= 0.8f;
                    matrixValues[4] *= 0.8f;
                    imageView.getImageMatrix().setValues(matrixValues);
                }
                // Invalidate the ImageView to redraw it
                imageView.invalidate();
            }
        });
    }

}

