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
        }catch (IOException e){
            this.context = context;
            //libros iniciales
            guardar(new Libro("0001","1984","George Orwell","Editorial Lumen","Ciencia Ficción",450));
            guardar(new Libro("0002","Harry Portter y la Piedra Filosofal","J.K Rowling","Editorial Emecé","Aventura",550));
            guardar(new Libro("0003","¿Sueñan los androides con ovejas eléctricas?","Philip K. Dick","Editorial DeBolsillo","Ciencia Ficción",330));
            guardar(new Libro("0004","El amor en los tiempos del cólera","Gabriel García Marquez","Editorial Alfred Knopf","Romántica",570));
            guardar(new Libro("0005","La ciudad y los perros","Mario Vargas Llosa","Editorial Seix barral","Épica",614));
            guardar(new Libro("0006","La vuelta al mundo en 80 días","Julio Verne","Editorial Punto y lectura","Aventura",722));
            guardar(new Libro("0007","Fahrenheit 451","Ray Bradbury","Editorial DeBolsillo","Ciencia Ficción",479));
            guardar(new Libro("0008","Pedro Páramo","Juan Rulfo","Editorial Punto de Lectura","Realismo Mágico",378));
            guardar(new Libro("0009","Rayuela","Julio Cortázar","Editorial Buenos Aires","Ciencia Ficción",549));
            guardar(new Libro("0010","El retrato de Dorian Gray","Oscar Wilde","Editorial Lippincott's Monthly Magazine","Filosófica",537));
        }
    }

    public static ServicioLibros getInstance(Context context) throws ClassNotFoundException, IOException{
        if (instaces == null){
            instaces = new ServicioLibros(context);
        }
        return instaces;
    }

    public void guardar(Libro libro) throws IOException{
        libros.add(libro);
        File archivo = new File(context.getExternalFilesDir(null),nombreArchivo);
        ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(archivo));
        output.writeObject(libro);
        output.close();
    }

    public ArrayList<Libro> cargarDatos() throws IOException, ClassNotFoundException{
        File archivo = new File(context.getExternalFilesDir(null),nombreArchivo);
        ObjectInputStream input = new ObjectInputStream(new FileInputStream(archivo));
        libros = (ArrayList<Libro>)input.readObject();
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
