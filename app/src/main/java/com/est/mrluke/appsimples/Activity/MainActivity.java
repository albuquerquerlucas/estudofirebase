package com.est.mrluke.appsimples.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.est.mrluke.appsimples.Config.ConfiguracaoFirebase;
import com.est.mrluke.appsimples.Entity.Usuario;
import com.est.mrluke.appsimples.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText edtEmailLogin;
    private EditText edtSenhaLogin;
    private Button btnAutenticarLogin;
    private TextView txtCadastrarLogin;
    private FirebaseAuth autenticacao;
    private Usuario usuario;
    private Dialog loaderLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume(){
        super.onResume();

        edtEmailLogin = (EditText) findViewById(R.id.edtEmailLogin);
        edtSenhaLogin = (EditText) findViewById(R.id.edtSenhaLogin);
        btnAutenticarLogin = (Button) findViewById(R.id.btnAutenticarLogin);
        txtCadastrarLogin = (TextView) findViewById(R.id.txtCadastrarLogin) ;

        btnAutenticarLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!edtEmailLogin.getText().toString().equals("") && !edtSenhaLogin.getText().toString().equals("")){
                    usuario = new Usuario();
                    usuario.setEmail(edtEmailLogin.getText().toString());
                    usuario.setSenha(edtSenhaLogin.getText().toString());

                    validarLoginWithFirebase();
                }else{
                    Toast.makeText(MainActivity.this, "Preencha os campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        txtCadastrarLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToRegister();
            }
        });
    }

    public void goToRegister(){
        Intent it = new Intent(MainActivity.this, Cadastro.class);
        startActivity(it);
        finish();
    }

    public void goToPrincial(){

        Intent it = new Intent(MainActivity.this, Principal.class);
        Bundle params = new Bundle();
        params.putString("usuario", edtEmailLogin.getText().toString());
        it.putExtras(params);
        startActivity(it);
        finish();
    }

    private void validarLoginWithFirebase(){

        openWindowLoaderLogin();

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    loaderLogin.dismiss();
                    goToPrincial();
                    //Toast.makeText(MainActivity.this, "Login Efetuado com Sucesso!", Toast.LENGTH_SHORT).show();
                }else{
                    loaderLogin.dismiss();
                    Toast.makeText(MainActivity.this, "Usuário ou senha inválidos!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void openWindowLoaderLogin(){

        loaderLogin = new Dialog(this);
        loaderLogin.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loaderLogin.setContentView(R.layout.dialog_loader);
        loaderLogin.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loaderLogin.setCancelable(false);

        final ProgressBar loader = ProgressBar.class.cast(loaderLogin.findViewById(R.id.barrinha));
        loaderLogin.show();
    }
}
