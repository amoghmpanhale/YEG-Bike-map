package com.example.bikemap;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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


    private void openDirectory(Uri uriToLoad) {
        // Choose a directory using the system's file picker.
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);

        // Optionally, specify a URI for the directory that should be opened in
        // the system file picker when it loads.
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, uriToLoad);

        startActivityForResult(intent, PICK_DIRECTORY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PICK_PDF_FILE:
                    // The result data contains a URI for the PDF document that
                    // the user selected.
                    Uri pdfUri = null;
                    if (resultData != null) {
                        pdfUri = resultData.getData();
                        // Do something with the PDF document.
                        String pdfName = getFileNameFromUri(pdfUri);
                        Log.w("PDF name", pdfName);
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

    private String getFileNameFromUri(Uri uri) {
        String fileName = null;
        if (uri != null) {
            ContentResolver resolver = getContentResolver();
            Cursor cursor = resolver.query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                cursor.close();
            }
        }
        return fileName;
    }

}
