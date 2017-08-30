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
import com.google.firebase.database.DatabaseReference;

import java.util.UUID;

public class AtualizarCadastroContato extends AppCompatActivity {

    private EditText edtNomeAtualizarContato;
    private EditText edtEmailAtualizarContato;
    private Button btnSalvarAtualizarContato;
    private Button btnCancelarCadastroAtualizarContato;
    private Contato contato;
    private DatabaseReference firebaseDataBase;
    String contatoId, contatoNome, contatoEmail;
    Bundle args;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atualizar_cadastro_contato);
    }

    @Override
    protected void onResume(){
        super.onResume();

        Intent intent = getIntent();
        args = intent.getExtras();
        contatoId = args.getString("contatoId");
        contatoNome = args.getString("contatoNome");
        contatoEmail = args.getString("contatoEmail");

        edtNomeAtualizarContato = (EditText) findViewById(R.id.edtNomeAtualizarContato);
        edtEmailAtualizarContato = (EditText) findViewById(R.id.edtEmailAtualizarContato);
        btnSalvarAtualizarContato = (Button) findViewById(R.id.btnSalvarAtualizarContato);
        btnCancelarCadastroAtualizarContato = (Button) findViewById(R.id.btnCancelarCadastroAtualizarContato);

        edtNomeAtualizarContato.setText(contatoNome);
        edtEmailAtualizarContato.setText(contatoEmail);

        btnSalvarAtualizarContato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!edtNomeAtualizarContato.getText().toString().equals("") && !edtEmailAtualizarContato.getText().toString().equals("")){
                    contato = new Contato();
                    contato.setId(contatoId);
                    contato.setNome(edtNomeAtualizarContato.getText().toString());
                    contato.setEmail(edtEmailAtualizarContato.getText().toString());
                    atualizarContato(contato, contatoNome);
                }else{
                    Toast.makeText(AtualizarCadastroContato.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancelarCadastroAtualizarContato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToPrincipalFromCadastro();
            }
        });
    }

    public void goToPrincipalFromCadastro(){
        Intent it = new Intent(AtualizarCadastroContato.this, Principal.class);
        startActivity(it);
        finish();
    }

    private boolean atualizarContato(Contato contato, String nomeAntigo){

        try{
            firebaseDataBase = ConfiguracaoFirebase.getFirebase().child("contatos");
            firebaseDataBase.child(contato.getNome()).setValue(contato);
            cleanInputsForms();
            Toast.makeText(AtualizarCadastroContato.this, "Atualizado com sucesso!", Toast.LENGTH_SHORT).show();
            returnToPrincipal();

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        //Toast.makeText(CadastroContatos.this, "Implements...", Toast.LENGTH_SHORT).show();
    }

    public void cleanInputsForms(){
        edtNomeAtualizarContato.setText("");
        edtEmailAtualizarContato.setText("");
    }

    @Override
    public void onBackPressed() {
        returnToPrincipal();
    }

    public void returnToPrincipal(){
        Intent intent = new Intent(AtualizarCadastroContato.this, Principal.class);
        startActivity(intent);
        finish();
    }
}
