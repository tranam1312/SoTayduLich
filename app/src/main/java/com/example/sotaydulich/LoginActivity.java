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

public class LoginActivity extends AppCompatActivity {
    private EditText edEmail, edPass;
    private MaterialButton btnLogin;
    private AppCompatTextView signUp, forgetPassword;
    private TextInputLayout errMail, errPass;
    private MyDatabase db ;
    private String email, pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edEmail = findViewById(R.id.email);
        edPass = findViewById(R.id.pass);
        btnLogin = findViewById(R.id.btn_login);
        signUp = findViewById(R.id.tv_sign_up);
        forgetPassword = findViewById(R.id.tv_forget_password);
        errMail = findViewById(R.id.textInputLayoutEmail);
        errPass = findViewById(R.id.textInputLayoutPass);
        init();
    }
    public  void  init(){
        db = new MyDatabase(this);
        onClickLogin();
        onClickSignUp();
    }

    public  void onClickLogin(){
        btnLogin.setOnClickListener(onClickLoginListener);
    }
    public void onClickSignUp(){
        signUp.setOnClickListener(onClickSignUpListener);
    }

    private View.OnClickListener onClickLoginListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            checkInput();
        }
    };

    private View.OnClickListener onClickSignUpListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(SignUpActivity.class);

        }
    };
    // kiểm tra dữ liệu từ người dùng nhập vào
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
            }else  if (pass.equals("")){
                errPass.setHelperText("Mời bạn nhập mật khẩu");
            }
            else  if (pass.length()<8 || pass.length()>32){
                errPass.setHelperText("Mật khẩu không nhỏ hơn 8 ký tự và lớn hơn 32 ký tự");
            }
            else {
                login(email, pass);
            }
        }
    }

    public void login(String email, String pass){
        if (checkData(email,pass )){
            db.close();
            Intent intent = new Intent(this, CategoryActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.silde_out_left, R.anim.silde_in_left);
        }
        else {
            errMail.setHelperText("Email bạn nhập sai !");
            errPass.setHelperText("Mật khẩu bạn nhập sai !");
        }
    }
    // check dữ liệu từ Sqlite
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
        overridePendingTransition(R.anim.silde_out_left, R.anim.silde_in_left);


    }
}