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
import com.est.mrluke.appsimples.Helper.Base64Custom;
import com.est.mrluke.appsimples.Helper.Preferencias;
import com.est.mrluke.appsimples.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class Cadastro extends AppCompatActivity {

    private EditText edtNomeCadastro;
    private EditText edtEmailCadastro;
    private EditText edtSenhaCadastro;
    private EditText edtConfirmarSenhaCadastro;
    private Button btnSalvarCadastro;
    private Button btnLimparCadastro;
    private TextView txtLogarCadastro;
    private FirebaseAuth autenticacao;
    private Usuario usuario;
    private Dialog loaderCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
    }

    @Override
    protected void onResume(){
        super.onResume();

        edtNomeCadastro = (EditText) findViewById(R.id.edtNomeCadastro);
        edtEmailCadastro = (EditText) findViewById(R.id.edtEmailCadastro);
        edtSenhaCadastro = (EditText) findViewById(R.id.edtSenhaCadastro);
        edtConfirmarSenhaCadastro = (EditText) findViewById(R.id.edtConfirmarSenhaCadastro);
        btnSalvarCadastro = (Button) findViewById(R.id.btnSalvarCadastro);
        btnLimparCadastro = (Button) findViewById(R.id.btnLimparCadastro);
        txtLogarCadastro = (TextView) findViewById(R.id.txtLogarCadastro);

        btnSalvarCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!edtNomeCadastro.getText().toString().equals("") && !edtEmailCadastro.getText().toString().equals("") &&
                        !edtSenhaCadastro.getText().toString().equals("") && !edtConfirmarSenhaCadastro.getText().toString().equals("")){
                    if(edtSenhaCadastro.getText().toString().equals(edtConfirmarSenhaCadastro.getText().toString())){
                        usuario = new Usuario();
                        usuario.setNome(edtNomeCadastro.getText().toString());
                        usuario.setEmail(edtEmailCadastro.getText().toString());
                        usuario.setSenha(edtSenhaCadastro.getText().toString());

                        cadastrarNovoUsuario();
                    }else{
                        Toast.makeText(Cadastro.this, "As senhas não correspondem", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(Cadastro.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnLimparCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cleanInputs();
            }
        });

        txtLogarCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLogin();
            }
        });
    }

    public void goToLogin(){
        Intent it = new Intent(Cadastro.this, MainActivity.class);
        startActivity(it);
        finish();
    }

    private void cadastrarNovoUsuario(){

        openWindowLoaderCadastro();

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(Cadastro.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    loaderCadastro.dismiss();
                    Toast.makeText(Cadastro.this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();

                    String identificadoUsuario = Base64Custom.codificarBase64(usuario.getEmail());
                    //FirebaseUser usuarioFirebase = task.getResult().getUser();
                    usuario.setId(identificadoUsuario);
                    usuario.cadastrarUsuario();

                    Preferencias preferencias = new Preferencias(Cadastro.this);
                    preferencias.salvarUsuarioPreferencias(identificadoUsuario, usuario.getNome());

                    goToLogin();
                }else{
                    loaderCadastro.dismiss();
                    String errorExcesao = "";

                    try {
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        errorExcesao = "A senha deve conter mínimo de 8 caracteres com letras e números";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        errorExcesao = "E-mail digitado é inválido";
                    }catch (FirebaseAuthUserCollisionException e){
                        errorExcesao = "E-mail já cadastrado no sistema";
                    }catch (Exception e){
                        errorExcesao = "Erro ao efetuar o cadastro";
                        e.printStackTrace();
                    }

                    Toast.makeText(Cadastro.this, "Erro: " + errorExcesao, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void openWindowLoaderCadastro(){

        loaderCadastro = new Dialog(this);
        loaderCadastro.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loaderCadastro.setContentView(R.layout.dialog_loader_cadastro);
        loaderCadastro.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loaderCadastro.setCancelable(false);

        final ProgressBar loader = ProgressBar.class.cast(loaderCadastro.findViewById(R.id.barrinha));
        loaderCadastro.show();
    }

    public void cleanInputs(){
        edtNomeCadastro.setText("");
        edtEmailCadastro.setText("");
        edtSenhaCadastro.setText("");
        edtConfirmarSenhaCadastro.setText("");
    }

    @Override
    public void onBackPressed() {
        goToLogin();
    }
}
