package com.example.sotaydulich;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sotaydulich.model.ViTri;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ContentActivity extends AppCompatActivity {
    private ViTri viTri = null ;
    private Toolbar toolbar;
    private MyDatabase db;
    private AppCompatButton btnThem, btnXoa;
    private AppCompatImageView img;
    private EditText diaDiem, moTa;
    private Uri imageUri;
    private AlertDialog.Builder builder;
    private MenuItem itemEdit, itemFinish, itemDelete;
    private String DiaDiem, Mota ;
    private  Bitmap bitmap;
    private  byte []data;
    private CategoryActivity categoryActivity;
    private static int RESULT_IMG = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        overridePendingTransition(R.anim.silde_in_right, R.anim.slide_out_right);
        toolbar = findViewById(R.id.toolbar_content);
        btnThem = findViewById(R.id.btn_add_img);
        btnXoa = findViewById(R.id.btn_remove_img);
        img = findViewById(R.id.img);
        diaDiem = findViewById(R.id.ed_dia_diem);
        moTa=   findViewById(R.id.ed_mo_ta);
        setUpToolbar();
        init();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_edit, menu);
        itemEdit = menu.findItem(R.id.edit);
        itemDelete =menu.findItem(R.id.delete);
        itemFinish = menu.findItem(R.id.finish);
        if (viTri == null){
            itemFinish.setVisible(true);
            itemDelete.setVisible(false);
            itemEdit.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
                return true;
            case R.id.finish:
                xong();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_IMG && resultCode == RESULT_OK) {
            if (data.getData() != null) {
                imageUri = data.getData();
                btnThem.setVisibility(View.GONE);
                try {
                    bitmap= MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    img.setVisibility(View.VISIBLE);
                    img.setImageBitmap(bitmap);
                    btnXoa.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setUpToolbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Thêm vị trí du lịch");
    }

    public  void init(){
        db = new MyDatabase(this);
        onClickAddImg();
        onClickDeleteImg();
    }
    public void onClickDeleteImg(){
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img.setVisibility(View.GONE);
                btnXoa.setVisibility(View.GONE);
                btnThem.setVisibility(View.VISIBLE);
            }
        });
    }
    public void xong(){
        DiaDiem = diaDiem.getText().toString().trim();
        Mota = moTa.getText().toString().trim();
        data = bitMapToByteArray(bitmap);
        if (db.insertData(DiaDiem, Mota,data)){
            Intent returnIntent = new Intent();
            returnIntent.putExtra("result","Ok");
            setResult(Activity.RESULT_OK,returnIntent);
            finish();
            finish();
        }
    }

    public void onClickAddImg(){
        btnThem.setOnClickListener(onClickAddImgListener);
    }

    private View.OnClickListener onClickAddImgListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            checkPermission();
        }
    };

    public  void checkPermission(){
        PermissionX.init(this)
                .permissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request(requestCallback);
    }
    private RequestCallback requestCallback = new RequestCallback() {
        @Override
        public void onResult(boolean allGranted, @NonNull List<String> grantedList, @NonNull List<String> deniedList) {
            if (allGranted) {
                openLibrary();
            } else {
                Toast.makeText(getApplicationContext(), "These permissions are denied: $deniedList", Toast.LENGTH_LONG).show();
            }
        }
    };

    public void  openLibrary (){
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULT_IMG);
    }

    public byte[] bitMapToByteArray(Bitmap bitmap){
        ByteArrayOutputStream baos =  new ByteArrayOutputStream();;
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    public void setAlertDialog(String tile, String mess, EditActivity.onClick onClick){
        builder =new AlertDialog.Builder(this);
        builder.setTitle(tile);
        builder.setMessage(mess);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id)
            {
                onClick.okClickOk();
                dialog.cancel();
            }
        });
        builder.setNegativeButton("Hủy",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.cancel();
                    }
                });
        builder.create().show();
    }
}
