package com.example.alquilerlibros.modelo;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ServicioLibros {

    private ArrayList<Libro> libros;
    private static ServicioLibros instaces;
    private final String nombreArchivo = "libros.obj";
    private Context context;

    private ServicioLibros(Context context) throws ClassNotFoundException, IOException{
        try {
            this.context = context;
            this.libros = new ArrayList<>();
            cargarDatos();
        }catch (IOException e) {
            this.context = context;
            guardar(new Libro("0001","Harry Potter y la Piedra Filosofal","J.K Rowling","Editorial Emecé","Ciencia Ficción",450));
        }
    }

    public static ServicioLibros getInstance(Context context) throws ClassNotFoundException, IOException{
        if (instaces == null)
            instaces = new ServicioLibros(context);
        return instaces;
    }

    public void guardar(Libro libro) throws IOException{
        libros.add(libro);
        File archivo = new File(context.getExternalFilesDir(null),nombreArchivo);
        ObjectOutputStream output =
                new ObjectOutputStream(new FileOutputStream(archivo));
        output.writeObject(libros);
        output.close();
    }

    public ArrayList<Libro> cargarDatos() throws IOException, ClassNotFoundException {
        File archivo = new File(context.getExternalFilesDir(null),nombreArchivo);
        ObjectInputStream input = new ObjectInputStream(new FileInputStream(archivo));
        libros = (ArrayList<Libro>) input.readObject();
        input.close();
        return libros;
    }

    public void eliminar(int position) throws IOException{
        libros.remove(position);
        File archivo = new File(context.getExternalFilesDir(null),nombreArchivo);
        ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(archivo));
        output.writeObject(libros);
        output.close();
    }

    public void prestar(int posicion) throws IOException{
        if (libros.get(posicion).isPrestamo()) libros.get(posicion).setPrestamo(false);
        else libros.get(posicion).setPrestamo(true);
        File archivo = new File(context.getExternalFilesDir(null),nombreArchivo);
        ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(archivo));
        output.writeObject(libros);
        output.close();
    }

    public boolean prestado (int posicion){
        if (libros.get(posicion).isPrestamo()) return true;
        else return false;
    }
}
