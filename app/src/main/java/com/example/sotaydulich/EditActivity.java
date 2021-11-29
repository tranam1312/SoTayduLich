package com.example.sotaydulich;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sotaydulich.model.ViTri;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class EditActivity extends AppCompatActivity {
    private ViTri viTri = null ;
    private Toolbar toolbar;
    private MyDatabase db;
    private AppCompatButton btnEditImg;
    private AppCompatImageView img;
    private EditText diaDiem, moTa;
    private AlertDialog.Builder builder;
    private Button btnEdit, btnHuy, btnRemove, btnSave;
    private Bitmap bitmap;
    private static int RESULT_IMG = 200;
    private Boolean checkEdit = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        toolbar = findViewById(R.id.toolbar_content);
        btnEditImg = findViewById(R.id.btn_edit_img);
        img = findViewById(R.id.img);
        diaDiem = findViewById(R.id.ed_dia_diem);
        moTa=   findViewById(R.id.ed_mo_ta);
        btnEdit = findViewById(R.id.btn_edit);
        btnHuy= findViewById(R.id.btn_huy);
        btnSave = findViewById(R.id.btn_save);
        btnRemove = findViewById(R.id.btn_remove);
        setToolbar();
        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        if ( bundle.getSerializable("vitri") != null){
            viTri = (ViTri) bundle.getSerializable("vitri");
            setData(viTri);
        }
        init();
    }
    public void setToolbar (){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if (checkEdit){
                    cancelRepair();
                }else{
                    finish();
                }
                return true;
            default:  return super.onOptionsItemSelected(item);
        }
    }
    public void init(){
        db = new MyDatabase(this);
        btnEditImg.setOnClickListener(onClickAddImgListener);
        btnSave.setOnClickListener(onClickSaveListener);
        btnEdit.setOnClickListener(onClickEditListener);
        btnRemove.setOnClickListener(onClickRemoveListener);

    }
    private View.OnClickListener onClickEditListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            edit();
        }
    };
    private View.OnClickListener onClickRemoveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            delete();
        }
    };

    public  void  edit(){
        checkEdit = true;
        btnRemove.setVisibility(View.GONE);
        btnEdit.setVisibility(View.GONE);
        diaDiem.setFocusable(true);
        moTa.setFocusable(true);
    }
    private View.OnClickListener onClickSaveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_IMG && resultCode == RESULT_OK) {
            if (data.getData() != null) {
                Uri imageUri = data.getData();
                try {
                    bitmap= MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    img.setVisibility(View.VISIBLE);
                    img.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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
    public void setData(ViTri viTri) {
        diaDiem.setFocusable(false);
        moTa.setFocusable(false);
        diaDiem.setText(viTri.getDiaDiem());
        moTa.setText(viTri.getMoTa());
        bitmap = BitmapFactory.decodeByteArray(viTri.getUriImg(), 0, viTri.getUriImg().length);
        img.setImageBitmap(bitmap);
    }
    public void save(){
        db = new MyDatabase(this);
        db.updateData(viTri.getId(),diaDiem.getText().toString(), moTa.getText().toString(),bitMapToByteArray(bitmap));
    }

    public void cancelRepair(){
        diaDiem.setFocusable(false);
        moTa.setFocusable(false);
        diaDiem.setFocusable(false);
        moTa.setFocusable(false);
        btnEdit.setVisibility(View.VISIBLE);
        btnRemove.setVisibility(View.VISIBLE);
        btnHuy.setVisibility(View.GONE);
        btnSave.setVisibility(View.GONE);
        diaDiem.setText(viTri.getDiaDiem());
        moTa.setText(viTri.getMoTa());
        bitmap = BitmapFactory.decodeByteArray(viTri.getUriImg(), 0, viTri.getUriImg().length);
        img.setImageBitmap(bitmap);
    }

    public  void delete(){
        setAlertDialog("Xóa", "Bạn có chắc muốn xóa không ?", new onClick() {
            @Override
            public void okClickOk() {
                db.deleteData(viTri.getId());
                finish();
            }
        });
    }

    public void setAlertDialog(String tile, String mess,onClick onClick){
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

    public byte[] bitMapToByteArray(Bitmap bitmap){
        ByteArrayOutputStream baos =  new ByteArrayOutputStream();;
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    interface onClick {
        void  okClickOk();
    }
}