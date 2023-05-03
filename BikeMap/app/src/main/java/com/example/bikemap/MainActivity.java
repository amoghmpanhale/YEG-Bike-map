package com.example.bikemap;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    // Request code for selecting a PDF document.
    private static final int PICK_PDF_FILE = 2;
    private static final int PICK_DIRECTORY = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get a reference to the "Upload" button in the layout.
        Button uploadButton = findViewById(R.id.upload);

        // Set an onClickListener on the button.
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the file system to select a PDF.
                openFile();
            }
        });
    }

    private void openFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");

        // Set the initial directory to the device's shared storage.
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, Uri.parse("content://com.android.externalstorage.documents/root/"));

        startActivityForResult(intent, PICK_PDF_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PICK_PDF_FILE:
                    Uri pdfUri = null;
                    if (resultData != null) {
                        pdfUri = resultData.getData();
                        convertFirstPageToJPEG(pdfUri);
                    }
                    break;

                case PICK_DIRECTORY:
                    // The result data contains a URI for the directory that the
                    // user selected.
                    Uri directoryUri = null;
                    if (resultData != null) {
                        directoryUri = resultData.getData();
                        // Do something with the directory.
                    }
                    break;
            }
        }
    }

    private void convertFirstPageToJPEG(Uri pdfUri) {
        try {
            // Open the PDF document
            ParcelFileDescriptor fileDescriptor = getContentResolver().openFileDescriptor(pdfUri, "r");
            PdfRenderer renderer = new PdfRenderer(fileDescriptor);
            System.out.println("PDF RENDERRRRRRRRRRR:" + renderer);

            // Get the first page of the PDF
            PdfRenderer.Page page = renderer.openPage(0);
            System.out.println("FIRST PAGE" + page);

            // Create a bitmap of the page
            Bitmap bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
            System.out.println("Bitmap" + bitmap);

            // Save the bitmap as a JPEG file
            String jpegName = "page_0.jpg";
            File jpegFile = new File(getFilesDir(), jpegName);
            FileOutputStream out = new FileOutputStream(jpegFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            Log.i("Image Generation", "Image generation and storage successful. JPEG file path: " + jpegFile.getAbsolutePath());

            Intent intent = new Intent(this, HomepageActivity.class);
            intent.putExtra("image_path", jpegFile.getAbsolutePath());
            startActivity(intent);

            // Clean up
            page.close();
            renderer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
