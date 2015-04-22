package com.a202.ala.photo;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;


public class MainActivity extends Activity {

    Uri photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PackageManager manager = getPackageManager();
        TextView texto = (TextView) findViewById(R.id.tengo);
        if(manager.hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            texto.setText("Tengo cámara");
        }
        else{
            texto.setText("No tengo cámara");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.camera) {
            Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            if (i.resolveActivity(getPackageManager()) != null){
            // Return the Activity component that should be used to handle this intent.
            // The appropriate component is determined based on the information in the intent, evaluated as follows
            // Si da null es que no hay nada que pueda ejecutar ese intent

                startActivityForResult(i, 1); // se le añade un identificador al intent (es el requestCode de onActivityResult)
            }
            return true;
        }
        if (id == R.id.cameraHD) {
            Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            if (i.resolveActivity(getPackageManager()) != null){
                File f = getPhotoFile(); //función creada por nosotros
                i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));

                startActivityForResult(i, 3);
            }
            return true;
        }
        if (id == R.id.video) {
            Intent i = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            if (i.resolveActivity(getPackageManager()) != null){
                startActivityForResult(i, 2);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private File getPhotoFile(){
        File publicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES); //Te da la ruta hacia una de las carpetas de directory del móbil
        File privateDir = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES); //Te da la ruta hacia una de las carpetas de directory en la aplicación (esto desaparece al cerrar la aplicación)

        Log.i("Pública", ""+publicDir);
        Log.i("Privada", ""+privateDir);

        File photo = null;

        try{
            photo = File.createTempFile("mifoto", ".jpg", publicDir);
        }
        catch (IOException e){
            e.printStackTrace();
        }

        return photo;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_OK){
            return;
        }

        switch (requestCode){
            case 1:
                ImageView iV = (ImageView) findViewById(R.id.imageView);
                if(data != null){
                    Bitmap image = (Bitmap) data.getExtras().get("data");
                    data.getExtras();
                    iV.setImageBitmap(image);
                    iV.bringToFront(); //Lo pone delante de todo
                }
                else{
                    iV.setImageURI(photo);
                }
                break;
            case 2:
                Uri video = data.getData();
                VideoView vV = (VideoView) findViewById(R.id.videoView);

                vV.setVideoURI(video);
                vV.start();
                break;
            case 3:
                ImageView iVHD = (ImageView) findViewById(R.id.imageView);
                if(data != null){
                    Bitmap image = (Bitmap) data.getExtras().get("data");
                    data.getExtras();
                    iVHD.setImageBitmap(image);
                    iVHD.bringToFront(); //Lo pone delante de todo
                }
                else{
                    iVHD.setImageURI(photo);
                }
                break;
        }
    }
}
