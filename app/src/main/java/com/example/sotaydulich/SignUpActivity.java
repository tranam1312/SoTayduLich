package com.example.sotaydulich;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class SignUpActivity extends AppCompatActivity {
    private EditText edEmail, edPass;
    private MaterialButton btnSignUp;
    private AppCompatTextView login;
    private String email, pass;
    private TextInputLayout errMail, errPass;
    private MyDatabase db ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        edEmail = findViewById(R.id.ed_email);
        edPass= findViewById(R.id.ed_pass);
        btnSignUp = findViewById(R.id.btn_sign_up);
        login = findViewById(R.id.tv_login);
        errMail = findViewById(R.id.textInputLayoutEmail);
        errPass = findViewById(R.id.textInputLayoutPass);
        init();
    }
    //    hàm tô
    public  void  init(){
        db = new MyDatabase(this);
        onClickSignUp();
        onClickBack();
    }
    //    hàm xử lý khi nhấn đăng ký
    public void  onClickSignUp(){
        btnSignUp.setOnClickListener(onClickSignUpListener);
    }
    //    hàm quay về màn login khi nhấn
    public void onClickBack(){
        login.setOnClickListener(onClickLoginListener);
    }

    private View.OnClickListener onClickSignUpListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            checkInput();
        }
    };

    private View.OnClickListener onClickLoginListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            overridePendingTransition(R.anim.silde_in_right, R.anim.slide_out_right);
            finish();
        }
    };
    //    kiểm tra dữ liệu người dùng nhập vào
    public  void checkInput(){
        email = edEmail.getText().toString().trim();
        pass = edPass.getText().toString().trim();
        if(email.equals("")){
            errMail.setHelperText("Mời bạn nhập email dăng nhập");
            if (pass.equals("")){
                errPass.setHelperText("Mời bạn nhập mật khẩu");
            }
            else  if (pass.length()<8 || pass.length()>32){
                errPass.setHelperText("Mật khẩu không nhỏ hơn 8 ký tự và lớn hơn 32 ký tự");
            }

        }else if (!email.equals("")){
            if (!email.contains("@gmail.com")){
                errMail.setHelperText("Bạn chưa nhập đúng định dang email");
                if (pass.equals("")){
                    errPass.setHelperText("Mời bạn nhập mật khẩu");
                }
                else if (pass.length()<8 || pass.length()>32){
                    errPass.setHelperText("Mật khẩu không nhỏ hơn 8 ký tự và lớn hơn 32 ký tự");
                }
            }else if (pass.equals("")){
                errPass.setHelperText("Mời bạn nhập mật khẩu");
            }
            else  if (pass.length()<8 || pass.length()>32){
                errPass.setHelperText("Mật khẩu không nhỏ hơn 8 ký tự và lớn hơn 32 ký tự");
            }
            else {
                signUp();
            }
        }
    }

    //    kiểm tra và check tài khoản từ Sqlite
    public void signUp(){
        if (!checkData(email, pass)){
            if (db.insertDataUser(email, pass)){
                db.close();
                startActivity(CategoryActivity.class);
            }
        }else {
            errMail.setHelperText("Email đã tồn tại vui lòng nhập email khác !");
            errPass.setHelperText("");
        }
    }
    //    lấy dữ liệu và kiểm tra tài khoản đã tôn tại hay không từ  dữ liệu sqlite
    public Boolean checkData(String edEmail, String edPass){
        Boolean check = false;
        Cursor cursor = db.getAllDataUser();
        while (cursor.moveToNext()) {
            String email = cursor.getString(1);
            String pass = cursor.getString(2);
            if (email.equals(edEmail)  && pass.equals(edPass)) {
                check = true;
                break;
            }
        }
        return check;
    }

    public void  startActivity(Class intentClass){
        Intent intent = new Intent(this, intentClass);
        startActivity(intent);
        overridePendingTransition(R.anim.silde_in_left, R.anim.silde_out_left);
    }
    // hàm  xử lý back về trên phím ảo
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.silde_in_right, R.anim.slide_out_right);


    }
}