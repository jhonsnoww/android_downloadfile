package com.fb.downloadfile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    String url = "https://upload.wikimedia.org/wikipedia/commons/6/60/The_Organ_at_Arches_National_Park_Utah_Corrected.jpg";
    String FileName = "hello";
    private static final int WRITE_PERMISSION = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {

                downloadFile("haha", url);
            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION);
            }
        }


    }

    private void downloadFile(String fileName, String url) {
        Uri downloadUri = Uri.parse(url);
        DownloadManager manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

        try {

            // DownloadMManager can be null ,So we hv to open link in browser.

            if (manager != null) {
                DownloadManager.Request request = new DownloadManager.Request(downloadUri);

                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                        .setTitle(fileName)
                        .setDescription("Downloading "+fileName)
                        .setAllowedOverMetered(true)
                        .setAllowedOverRoaming(true)
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,fileName)
                        .setMimeType(getMineType(downloadUri));

                manager.enqueue(request);

                Toast.makeText(this,"Download Start!",Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(Intent.ACTION_VIEW, downloadUri);
                startActivity(intent);
            }

        } catch (Exception e) {
            Toast.makeText(this, "Something was wrong!", Toast.LENGTH_SHORT).show();
            Log.e("ERROR", "E: " + e.getMessage());
        }
    }

    private String getMineType (Uri uri) {
        ContentResolver resolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(resolver.getType(uri));
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == WRITE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                downloadFile("haha", url);
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

}