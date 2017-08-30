package com.est.mrluke.appsimples.Adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.est.mrluke.appsimples.Entity.Contato;
import com.est.mrluke.appsimples.R;

import java.util.ArrayList;

/**
 * Created by Mrluke on 25/08/2017.
 */

public class ContatosAdapter extends ArrayAdapter<Contato> {

    private ArrayList<Contato> contato;
    private Context context;

    public ContatosAdapter(Context context, ArrayList<Contato> lista) {
        super(context, 0, lista);
        this.contato = lista;
        this.context = context;
    }

    @Override
    public View getView(int position,View convertView, ViewGroup parent) {

        View itemView = convertView;

        if(contato != null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            itemView = inflater.inflate(R.layout.lista_contatos, parent, false);

            TextView txtNomeContatoLista = (TextView) itemView.findViewById(R.id.txtNomeContatoLista);
            TextView txtEmailContatoLista = (TextView) itemView.findViewById(R.id.txtEmailContatoLista);

            Contato contatos2 = contato.get(position);

            txtNomeContatoLista.setText(contatos2.getNome());
            txtEmailContatoLista.setText(contatos2.getEmail());
        }

        return itemView;

    }
}
