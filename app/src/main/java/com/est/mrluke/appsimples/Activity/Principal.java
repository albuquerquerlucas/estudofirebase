package com.est.mrluke.appsimples.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.est.mrluke.appsimples.Adapter.ContatosAdapter;
import com.est.mrluke.appsimples.Config.ConfiguracaoFirebase;
import com.est.mrluke.appsimples.Entity.Contato;
import com.est.mrluke.appsimples.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Principal extends AppCompatActivity {

    private TextView txtUsuarioPrincipal;
    private TextView txtEncerrarSessaoPrincipal;
    private Button btnAdicionarNovoContato;
    private ListView listaContatos;
    private ArrayAdapter<Contato> contatosAdapter;
    private ArrayList<Contato> listaContatosArray;
    private ValueEventListener valueEventListenerContatos;
    private FirebaseAuth usuarioFirebase;
    private DatabaseReference firebaseDataBaseReference;
    private AlertDialog caixaAlertaConfirmacao;
    private Contato contatoSelecionado;
    String usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        /*Intent intent = getIntent();
        Bundle args = intent.getExtras();
        usuario = args.getString("usuario");*/

        usuarioFirebase = ConfiguracaoFirebase.getFirebaseAutenticacao();

        txtUsuarioPrincipal = (TextView) findViewById(R.id.txtUsuarioPrincipal);
        btnAdicionarNovoContato = (Button) findViewById(R.id.btnAdicionarNovoContato);
        txtEncerrarSessaoPrincipal = (TextView) findViewById(R.id.txtEncerrarSessaoPrincipal);
        listaContatos = (ListView) findViewById(R.id.listaContatos);

        listaContatosArray = new ArrayList<>();
        contatosAdapter = new ContatosAdapter(this, listaContatosArray);
        listaContatos.setAdapter(contatosAdapter);

        firebaseDataBaseReference = ConfiguracaoFirebase.getFirebase().child("contatos");
        valueEventListenerContatos = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaContatosArray.clear();

                for(DataSnapshot dados : dataSnapshot.getChildren()){
                    Contato contatoNovo = dados.getValue(Contato.class);

                    listaContatosArray.add(contatoNovo);
                }
                contatosAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        listaContatos.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                contatoSelecionado = contatosAdapter.getItem(position);

                Intent it = new Intent(Principal.this, AtualizarCadastroContato.class);
                Bundle params = new Bundle();
                params.putString("contatoId", contatoSelecionado.getId());
                params.putString("contatoNome", contatoSelecionado.getNome());
                params.putString("contatoEmail", contatoSelecionado.getEmail());
                it.putExtras(params);
                startActivity(it);
                finish();
                //Toast.makeText(Principal.this, "Contato: " + contatoSelecionado.getNome(), Toast.LENGTH_SHORT).show();
            }
        });

        listaContatos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
                contatoSelecionado = contatosAdapter.getItem(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(Principal.this);
                builder.setTitle("Atenção");
                builder.setMessage("Deseja excluir " + contatoSelecionado.getNome() + " ?");
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        firebaseDataBaseReference = ConfiguracaoFirebase.getFirebase().child("contatos");
                        firebaseDataBaseReference.child(contatoSelecionado.getNome()).removeValue();
                        //System.out.println("ERRO ==> " + contatoSelecionado.getId());
                    }
                });

                builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                caixaAlertaConfirmacao = builder.create();
                caixaAlertaConfirmacao.show();

                return true;
            }
        });

        txtUsuarioPrincipal.setText("Bem vindo " + usuario);

        btnAdicionarNovoContato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAddNewContato();
            }
        });

        txtEncerrarSessaoPrincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                encerrarSessao();
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();

        /*usuarioFirebase = ConfiguracaoFirebase.getFirebaseAutenticacao();

        txtUsuarioPrincipal = (TextView) findViewById(R.id.txtUsuarioPrincipal);
        btnAdicionarNovoContato = (Button) findViewById(R.id.btnAdicionarNovoContato);
        txtEncerrarSessaoPrincipal = (TextView) findViewById(R.id.txtEncerrarSessaoPrincipal);
        listaContatos = (ListView) findViewById(R.id.listaContatos);

        listaContatosArray = new ArrayList<>();
        contatosAdapter = new ContatosAdapter(this, listaContatosArray);
        listaContatos.setAdapter(contatosAdapter);

        firebaseDataBaseReference = ConfiguracaoFirebase.getFirebase().child("contatos");
        valueEventListenerContatos = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaContatosArray.clear();

                for(DataSnapshot dados : dataSnapshot.getChildren()){
                    Contato contatoNovo = dados.getValue(Contato.class);

                    listaContatosArray.add(contatoNovo);
                }
                contatosAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        txtUsuarioPrincipal.setText("Bem vindo " + usuario);

        btnAdicionarNovoContato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAddNewContato();
            }
        });

        txtEncerrarSessaoPrincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                encerrarSessao();
            }
        });*/
    }

    public void goToAddNewContato(){
        Intent it = new Intent(Principal.this, CadastroContatos.class);
        startActivity(it);
        finish();
    }

    public void goToLogin(){
        Intent it = new Intent(Principal.this, MainActivity.class);
        startActivity(it);
        finish();
    }

    public void encerrarSessao(){
        usuarioFirebase.signOut();
        goToLogin();
    }

    @Override
    protected void onStart(){
        super.onStart();
        firebaseDataBaseReference.addValueEventListener(valueEventListenerContatos);
    }

    @Override
    protected void onStop(){
        super.onStop();
        firebaseDataBaseReference.removeEventListener(valueEventListenerContatos);
    }

    @Override
    public void onBackPressed() {
        encerrarSessao();
    }
}
