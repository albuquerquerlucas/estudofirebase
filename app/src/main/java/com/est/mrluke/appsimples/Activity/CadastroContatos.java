package com.est.mrluke.appsimples.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.est.mrluke.appsimples.Config.ConfiguracaoFirebase;
import com.est.mrluke.appsimples.Entity.Contato;
import com.est.mrluke.appsimples.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.UUID;

public class CadastroContatos extends AppCompatActivity {

    private EditText edtNomeContato;
    private EditText edtEmailContato;
    private Button btnSalvarContato;
    private Button btnCancelarCadastroContato;
    private Contato contato;
    private DatabaseReference firebaseDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_contatos);

    }

    @Override
    protected void onResume(){
        super.onResume();


        edtNomeContato = (EditText) findViewById(R.id.edtNomeContato);
        edtEmailContato = (EditText) findViewById(R.id.edtEmailContato);
        btnSalvarContato = (Button) findViewById(R.id.btnSalvarContato);
        btnCancelarCadastroContato = (Button) findViewById(R.id.btnCancelarCadastroContato);

        btnSalvarContato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!edtNomeContato.getText().toString().equals("") && !edtEmailContato.getText().toString().equals("")){
                    contato = new Contato();
                    contato.setId(UUID.randomUUID().toString());
                    contato.setNome(edtNomeContato.getText().toString());
                    contato.setEmail(edtEmailContato.getText().toString());
                    cadastrarNovoContato(contato);
                }else{
                    Toast.makeText(CadastroContatos.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancelarCadastroContato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToPrincipalFromCadastro();
            }
        });
    }

    public void goToPrincipalFromCadastro(){
        Intent it = new Intent(CadastroContatos.this, Principal.class);
        startActivity(it);
        finish();
    }

    private boolean cadastrarNovoContato(Contato contato){

        try{
            firebaseDataBase = ConfiguracaoFirebase.getFirebase().child("contatos");
            firebaseDataBase.child(contato.getNome()).setValue(contato);
            cleanInputsForms();
            Toast.makeText(CadastroContatos.this, "Salvo com sucesso!", Toast.LENGTH_SHORT).show();

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        //Toast.makeText(CadastroContatos.this, "Implements...", Toast.LENGTH_SHORT).show();
    }

    public void cleanInputsForms(){
        edtNomeContato.setText("");
        edtEmailContato.setText("");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CadastroContatos.this, Principal.class);
        startActivity(intent);
        finish();
    }
}
