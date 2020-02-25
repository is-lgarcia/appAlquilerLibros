package com.example.alquilerlibros.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alquilerlibros.R;
import com.example.alquilerlibros.modelo.Libro;

import java.util.ArrayList;

public class LibroAdapter extends RecyclerView.Adapter<LibroAdapter.LibroViewHolder> {

    private ArrayList<Libro> libros;
    private Activity activity;

    public interface OnLibroAdapterListener {
        void onPrestarLibro(int posicion);

        void onEliminarLibro(int posicion);
    }

    private OnLibroAdapterListener listener;

    public LibroAdapter(ArrayList<Libro> libros, Activity activity, OnLibroAdapterListener listener) {
        this.libros = libros;
        this.activity = activity;
        this.listener = listener;
    }

    public class LibroViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
            , View.OnLongClickListener {

        private TextView txtTitulo, txtIsbn, txtAutor, txtEditorial, txtGeneros, txtPaginas, txtPrestamo;

        public LibroViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitulo = itemView.findViewById(R.id.txt_titulo);
            txtIsbn = itemView.findViewById(R.id.txt_isbn);
            txtAutor = itemView.findViewById(R.id.txt_autor);
            txtEditorial = itemView.findViewById(R.id.txt_editorial);
            txtGeneros = itemView.findViewById(R.id.txt_generos);
            txtPaginas = itemView.findViewById(R.id.txt_paginas);
            txtPrestamo = itemView.findViewById(R.id.txt_prestamo);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void bindProducto(Libro libro) {
            txtTitulo.setText(libro.getTitulo());
            txtIsbn.setText(String.format("#%s", libro.getIsbn()));
            txtAutor.setText(libro.getAutor());
            txtEditorial.setText(libro.getEditorial());
            txtGeneros.setText(libro.getGeneros());
            txtPaginas.setText(String.format("%s páginas", String.valueOf(libro.getPaginas())));
            if (libro.isPrestamo()) txtPrestamo.setText("Prestado");
            else txtPrestamo.setText("Disponible");
        }

        @Override
        public void onClick(View v) {
            listener.onPrestarLibro(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            listener.onEliminarLibro(getAdapterPosition());
            return true;
        }
    }

    @NonNull
    @Override
    public LibroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.plantilla_libro, parent, false);
        return new LibroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LibroViewHolder holder, int position) {
        holder.bindProducto(libros.get(position));
    }

    @Override
    public int getItemCount() {
        return libros.size();
    }

    public ArrayList<Libro> getProductos() {
        return libros;
    }
}
