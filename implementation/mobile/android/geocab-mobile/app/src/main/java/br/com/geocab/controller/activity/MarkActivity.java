package br.com.geocab.controller.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import br.com.geocab.R;
import br.com.geocab.controller.adapter.ListLayerAdapter;
import br.com.geocab.controller.delegate.LayerMarkerDelegate;

public class MarkActivity extends Activity
{

    private EditText selectLayerEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark);

        selectLayerEditText = (EditText) findViewById(R.id.edit_text_select_layer);

        selectLayerEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_UP == event.getAction()) {
                    Intent i = new Intent(MarkActivity.this, LayerActivity.class);
                    startActivityForResult(i, 10);
                }
                return true; // return is important...
            }
        });

        Button buttonOpenDialog = (Button) findViewById(R.id.btn_select_image_marker);
        buttonOpenDialog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                final Dialog dialog = new Dialog(v.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_image);
                dialog.setCanceledOnTouchOutside(true);

                Button buttonCamera = (Button) dialog.findViewById(R.id.button_camera);
                buttonCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(takePicture, 0);
                        dialog.dismiss();
                    }
                });

                Button buttonGallery = (Button) dialog.findViewById(R.id.button_gallery);
                buttonGallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto, 1);
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }

        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView imageView = (ImageView) findViewById(R.id.image_view_marker);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    imageView.setImageBitmap(photo);
                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };

                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

                }
            case 10:
                if(resultCode == RESULT_OK){
                    String layerName = data.getStringExtra("name");
                    String layerTitle = data.getStringExtra("title");
                    int layerId = data.getIntExtra("id", 0);

                    this.selectLayerEditText.setText(layerTitle);
                }
                if (resultCode == RESULT_CANCELED) {
                    //Write your code if there's no result
                }
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mark, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
