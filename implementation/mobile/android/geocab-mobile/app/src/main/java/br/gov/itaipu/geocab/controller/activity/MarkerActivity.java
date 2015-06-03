package br.gov.itaipu.geocab.controller.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Base64;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import br.gov.itaipu.geocab.R;
import br.gov.itaipu.geocab.controller.app.AppController;
import br.gov.itaipu.geocab.controller.delegate.AbstractDelegate;
import br.gov.itaipu.geocab.controller.delegate.FileDelegate;
import br.gov.itaipu.geocab.controller.delegate.LayerDelegate;
import br.gov.itaipu.geocab.controller.delegate.MarkerDelegate;
import br.gov.itaipu.geocab.entity.Attribute;
import br.gov.itaipu.geocab.entity.AttributeType;
import br.gov.itaipu.geocab.entity.Layer;
import br.gov.itaipu.geocab.entity.Marker;
import br.gov.itaipu.geocab.entity.MarkerAttribute;
import br.gov.itaipu.geocab.entity.MarkerStatus;
import br.gov.itaipu.geocab.util.DelegateHandler;

public class MarkerActivity extends Activity {

    /**
     *
     */
    private String wktCoordenate;
    private Button buttonSelectPhoto;
    private ImageView imgMarker;
    private LayerDelegate layerDelegate;
    private ArrayList<Attribute> layerAttributes;
    private LinearLayout linearPainel;
    private Marker marker;
    private Button removeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker);

        this.linearPainel = (LinearLayout) findViewById(R.id.linearPainel);
        this.buttonSelectPhoto = (Button) findViewById(R.id.btn_select_photo);

        this.marker = (Marker) this.getIntent().getSerializableExtra("marker");
        if ( this.marker == null ) {
            this.marker = new Marker();
        } else {
            if ( AppController.getInstance().getBitmap() != null )
                this.marker.setImage(AppController.getInstance().getBitmap());
        }

        this.wktCoordenate = this.getIntent().getStringExtra("wktCoordenate");
        this.layerDelegate = new LayerDelegate(this);

        this.loadLayersDropDown();
    }

    /**
     * Carrega as camadas disponíveis para seleção
     */
    private void loadLayersDropDown(){

        final ArrayAdapter<Layer> dataAdapter = new ArrayAdapter<Layer>(this, android.R.layout.simple_spinner_item, new ArrayList());
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner layerSpinner = (Spinner) findViewById(R.id.spinner_layers);
        layerSpinner.setAdapter(dataAdapter);

        layerDelegate.listInternalLayers(new DelegateHandler<ArrayList>() {
            @Override
            public void responseHandler(ArrayList result) {
                dataAdapter.addAll(result);
                dataAdapter.notifyDataSetChanged();
            }
        });

        layerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Layer layer = dataAdapter.getItem(position);

                // Seleciona a layer em caso de edição do marker
                if ( marker.getLayer() != null && marker.getLayer().getId() != null )
                {
                    layerSpinner.setSelection(dataAdapter.getPosition(marker.getLayer()));
                    layerSpinner.setEnabled(false);
                }

                generateAttributeFieldsByLayer(layer);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

    }

    /**
     * Gera os atributos na aplicação de acordo com a camada
     * @param layer
     */
    private void generateAttributeFieldsByLayer(Layer layer){

        layerDelegate.listAttributesByLayer(layer.getId(), new DelegateHandler<ArrayList>() {
            @Override public void responseHandler(ArrayList result) {

                layerAttributes = result;
                MarkerActivity.this.linearPainel.removeAllViews();

                for ( Attribute attribute : layerAttributes ){

                    String markerValue = "";

                    // Percore a lista de atributos do marker para bindar o valor em caso de edição
                    for ( MarkerAttribute markerAttribute : MarkerActivity.this.marker.getMarkerAttributes() ){
                        if ( markerAttribute.getAttribute().getName().equals(attribute.getName()) ){
                            markerValue = markerAttribute.getValue();
                            break;
                        }
                    }

                    TextView textView = new TextView(getApplicationContext());
                    textView.setPadding(0, 5, 0, 0);
                    textView.setText(attribute.getName().substring(0, 1).toUpperCase() + attribute.getName().substring(1));
                    textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    textView.setTextAppearance(getApplicationContext(), android.R.style.TextAppearance_Medium);
                    textView.setTextColor(Color.BLACK);
                    linearPainel.addView(textView);

                    // Verifica o tipo do atributo e cria o elemento de acordo
                    if ( attribute.getType() == AttributeType.TEXT || attribute.getType() == AttributeType.NUMBER || attribute.getType() == AttributeType.DATE ){
                        EditText editText = (EditText) getLayoutInflater().inflate(R.layout.style_edit_text, null);
                        editText.setText(markerValue);

                        // Max length
                        InputFilter[] FilterArray = new InputFilter[1];
                        FilterArray[0] = new InputFilter.LengthFilter(250);
                        editText.setFilters(FilterArray);

                        if ( attribute.getType() == AttributeType.NUMBER ){
                            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                        } else {
                            editText.setInputType(InputType.TYPE_CLASS_TEXT);
                        }

                        attribute.setViewComponent(editText);
                        linearPainel.addView(editText);

                    } else if ( attribute.getType() == AttributeType.BOOLEAN ){

                        RadioGroup radioGroup = new RadioGroup(getApplicationContext());
                        radioGroup.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        radioGroup.setOrientation(LinearLayout.HORIZONTAL);
                        attribute.setViewComponent(radioGroup);
                        linearPainel.addView(radioGroup);

                        RadioButton radioBtnSim = (RadioButton) getLayoutInflater().inflate(R.layout.style_radio_button, null);
                        radioBtnSim.setText("Sim");
                        radioBtnSim.setChecked(markerValue.equals("Yes") ? true : false);
                        radioBtnSim.setPadding(0, 0, 7, 0);
                        radioBtnSim.setId(1);
                        radioGroup.addView(radioBtnSim);

                        RadioButton radioBtnNao = (RadioButton) getLayoutInflater().inflate(R.layout.style_radio_button, null);
                        radioBtnNao.setText("Não");
                        radioBtnNao.setChecked(markerValue.equals("No") ? true : false);
                        radioBtnNao.setId(0);
                        radioGroup.addView(radioBtnNao);

                    } else if ( attribute.getType() == AttributeType.DATE ){

                        //IMPLEMENTAR

                    }
                }

                // Em caso de edicao mostra a imagem
                if ( MarkerActivity.this.marker.getImage() != null )
                {
                    addImageView(MarkerActivity.this.marker.getImage());
                }

            }
        });

    }

    private void removeMarkerImage(){

        this.marker.setImage(null);
        this.marker.setFile(null);
        this.marker.setImageToDelete(true);

        if ( this.imgMarker != null )
            linearPainel.removeView(this.imgMarker);

        if ( this.removeImage != null )
            linearPainel.removeView(this.removeImage);

    }

    private void addImageView(Bitmap image) {

        if ( this.removeImage != null )
            linearPainel.removeView(this.removeImage);

        // Botao para remover a imagem
        this.removeImage = new Button(this);
        this.removeImage.setText("Remover Imagem");
        this.removeImage.setTextColor(Color.GRAY);
        this.removeImage.setBackground(null);
        this.removeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeMarkerImage();
            }
        });

        // Adiciona o campo a view dinamica
        linearPainel.addView(this.removeImage);

        // Imagem
        if ( this.imgMarker != null )
            linearPainel.removeView(this.imgMarker);

        this.imgMarker = new ImageView(this);
        this.imgMarker.setImageBitmap(image);
        this.imgMarker.setAdjustViewBounds(true);
        this.imgMarker.setPadding(0, 0, 0, 20);
        linearPainel.addView(this.imgMarker);

    }

    private boolean validateForm(){

        // Percorre a lista de atributos da camada
        for (Attribute attribute : this.layerAttributes ) {

            String fieldValue = attribute.getViewComponentValue();

            if ( attribute.getRequired() && (fieldValue == null || fieldValue.equals("")) ) {

                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Dados incompletos");
                alert.setMessage("Preencha o campo " + attribute.getName());
                alert.setPositiveButton("OK",null);
                alert.show();

                return false;
            }
        }

        return true;

    }

    /**
     * Abre o dialog para selecionar a fonte da imagem
     * @param v
     */
    public void selectMarkerImage(View v) {

        final CharSequence[] options = { "Galeria", "Tirar foto", "Cancelar" };

        AlertDialog.Builder builder = new AlertDialog.Builder(MarkerActivity.this);
        builder.setTitle("Selecionar");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Tirar foto"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "geocab-temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                }
                else if (options[item].equals("Galeria"))
                {
                    Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
                else if (options[item].equals("Cancelar")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    /**
     * Ação para salvar o marker preenchido
     * @param v
     */
    public void saveMarker(View v){

        if ( this.validateForm() ){
            this.insertOrUpdate();
        }

    }

    public void insertOrUpdate(){

        final Layer layer = (Layer) ( (Spinner) findViewById(R.id.spinner_layers) ).getSelectedItem();
        this.marker.setLayer(layer);

        for (Attribute attribute : MarkerActivity.this.layerAttributes) {

            // Percore a lista de atributos do marker para bindar o valor em caso de edição
            boolean find = false;
            for (MarkerAttribute markerAttr : MarkerActivity.this.marker.getMarkerAttributes()) {
                if (markerAttr.getAttribute().getName().equals(attribute.getName())) {
                    markerAttr.setValue(attribute.getViewComponentValue());
                    find = true;
                    break;
                }
            }

            if ( !find )
            {
                MarkerAttribute markerAttribute = new MarkerAttribute();
                markerAttribute.setAttribute(attribute);
                markerAttribute.setValue(attribute.getViewComponentValue());
                this.marker.getMarkerAttributes().add(markerAttribute);
            }
        }

        final MarkerDelegate markerDelegate = new MarkerDelegate(this);
        final FileDelegate fileDelegate = new FileDelegate(MarkerActivity.this);
        markerDelegate.showLoadingDialog();

        // Chamada ao serviço para persistir o marker
        if ( this.marker.getId() == null || this.marker.getId() == 0 )
        {
            this.marker.setStatus(MarkerStatus.PENDING);
            this.marker.setWktCoordenate(this.wktCoordenate);

            markerDelegate.insertMarker(marker, new DelegateHandler<Marker>() {
                @Override
                public void responseHandler(Marker markerResponse) {
                    markerResponse.setLayer(layer);
                    AppController.getInstance().getWebViewMap().loadUrl("javascript:geocabapp.addMarker('" + new GsonBuilder().create().toJson(markerResponse) + "')");
                    AppController.getInstance().getWebViewMap().loadUrl("javascript:geocabapp.changeToActionState()");

                    // Faz upload da imagem do marker
                    if ( marker.getFile() != null ) {

                        fileDelegate.uploadMarkerPicture(markerResponse.getId(), marker.getFile(), new DelegateHandler() {
                            @Override
                            public void responseHandler(Object result) {
                                markerDelegate.hideLoadingDialog();
                                finish();
                            }
                        });
                    } else {
                        markerDelegate.hideLoadingDialog();
                        finish();
                    }
                }
            });
        }
        // Chamada ao serviço para atualizar o marker
        else
        {
            markerDelegate.updateMarker(marker, new DelegateHandler<Marker>() {
                @Override
                public void responseHandler(final Marker markerResponse) {

                    // Faz upload da imagem do marker
                    if (marker.getFile() != null) {

                        fileDelegate.uploadMarkerPicture(marker.getId(), marker.getFile(), new DelegateHandler() {
                            @Override
                            public void responseHandler(Object result) {
                                listAttributes(markerDelegate, fileDelegate);
                            }
                        });

                    } else {

                        listAttributes(markerDelegate, fileDelegate);

                    }

                }
            });
        }

    }

    private void listAttributes(final MarkerDelegate markerDelegate, final FileDelegate fileDelegate){

        markerDelegate.listMarkerAttributesByMarker(marker, new DelegateHandler<String>() {
            @Override
            public void responseHandler(final String markerJson) {

                // Faz uma nova requisição para mostrar a imagem do marker
                fileDelegate.downloadMarkerPicture(marker, new DelegateHandler<Bitmap>() {
                    @Override
                    public void responseHandler(Bitmap bitmap) {

                        // Convert bitmap to Base64 encoded image for web
                        String imageBase64 = "";
                        if (bitmap != null) {
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                            byte[] byteArray = byteArrayOutputStream.toByteArray();
                            String image = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            imageBase64 = "data:image/jpeg;base64," + image;
                            imageBase64 = imageBase64.replaceAll("\n", "");
                        }

                        // Mostra o painel com informações do marker
                        String loggedUserJson = new GsonBuilder().create().toJson(AbstractDelegate.loggedUser);
                        AppController.getInstance().getWebViewMap().loadUrl("javascript:geocabapp.marker.loadAttributes('" + markerJson + "', '" + imageBase64 + "')");

                        markerDelegate.hideLoadingDialog();
                        finish();

                    };

                });

            };
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_marker, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("geocab-temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);

                    this.addImageView(bitmap);
                    MarkerActivity.this.marker.setFile(f);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));

                this.addImageView(thumbnail);
                MarkerActivity.this.marker.setFile( new File(picturePath) );
            }
        }
    }

}
