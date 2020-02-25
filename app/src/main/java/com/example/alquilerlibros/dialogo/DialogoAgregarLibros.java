package com.example.alquilerlibros.dialogo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.alquilerlibros.R;
import com.example.alquilerlibros.modelo.Libro;

public class DialogoAgregarLibros extends DialogFragment {
    public static final String TAG = "agregar_libro";

    public interface OnAgregarLibroListener{
        void onAgregarLibro(Libro libro);
    }

    private OnAgregarLibroListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialogo_agregar_libro,null);

        final EditText editTitulo = view.findViewById(R.id.edit_titulo);
        final EditText editISBN = view.findViewById(R.id.edit_isbn);
        final EditText editAutor = view.findViewById(R.id.edit_autor);
        final EditText editGenero = view.findViewById(R.id.edit_genero);
        final EditText editEditorial = view.findViewById(R.id.edit_editorial);
        final EditText editPaginas = view.findViewById(R.id.edit_paginas);

        Button btnCancelar = view.findViewById(R.id.btn_cancelar);
        Button btnAgregar = view.findViewById(R.id.btn_agregar_producto);

        builder.setTitle("Agregar producto")
                .setView(view);

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String isbn = editISBN.getText().toString();
                String titulo = editTitulo.getText().toString();
                String autori = editAutor.getText().toString();
                String genero = editGenero.getText().toString();
                String editorial = editEditorial.getText().toString();
                int paginas = Integer.parseInt(editPaginas.getText().toString());
                if (!isbn.equals("")&& !titulo.equals("")&&!autori.equals("")
                        &&!genero.equals("")&&!editorial.equals("")){
                    Libro libro = new Libro(isbn,titulo,autori,editorial,genero,paginas);
                    listener.onAgregarLibro(libro);
                    dismiss();
                }else {
                    Toast.makeText(getContext(), "Rellene todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return builder.create();
    }
}
