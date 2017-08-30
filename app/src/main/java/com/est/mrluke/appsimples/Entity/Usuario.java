package com.est.mrluke.appsimples.Entity;

import com.est.mrluke.appsimples.Config.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mrluke on 24/08/2017.
 */

public class Usuario {

    private String id;
    private String nome;
    private String email;
    private String senha;

    public Usuario() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void cadastrarUsuario(){

        DatabaseReference referenciaFirebase = ConfiguracaoFirebase.getFirebase();
        referenciaFirebase.child("usuario").child(String.valueOf(getId())).setValue(this);
    }

    /*@Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> hashMapUauario = new HashMap<>();

        hashMapUauario.put("id", getId());
        hashMapUauario.put("nome", getNome());
        hashMapUauario.put("email", getEmail());
        hashMapUauario.put("senha", getSenha());

        return hashMapUauario;
    }*/
}
