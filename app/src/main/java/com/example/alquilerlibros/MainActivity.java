package com.example.alquilerlibros;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.alquilerlibros.adapter.LibroAdapter;
import com.example.alquilerlibros.dialogo.DialogoAgregarLibros;
import com.example.alquilerlibros.modelo.Libro;
import com.example.alquilerlibros.modelo.ServicioLibros;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements
        DialogoAgregarLibros.OnAgregarLibroListener,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private RecyclerView listaLibros;
    private LibroAdapter adapter;
    private final int REQUEST_CODE = 1;
    private final String[] PERMISOS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listaLibros = findViewById(R.id.lista_libros);
        listaLibros.setHasFixedSize(true);

        int escribir = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int leer = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (escribir == PackageManager.PERMISSION_GRANTED && leer == PackageManager.PERMISSION_GRANTED) {
            try {
                adapter = new LibroAdapter(ServicioLibros.getInstance(this).cargarDatos(),this,
                        new LibroAdapter.OnLibroAdapterListener() {
                            @Override
                            public void onPrestarLibro(int position) {
                                confirmacionPrestamo(position);
                            }

                            @Override
                            public void onEliminarLibro(int position) {
                                confirmacionEliminar(position);
                            }
                        });
                listaLibros.setAdapter(adapter);
                listaLibros.setLayoutManager(new LinearLayoutManager(this));
            } catch (IOException e) {
                Toast.makeText(this, "Error al cargar el archivo", Toast.LENGTH_SHORT).show();
            } catch (ClassNotFoundException e) {
                Toast.makeText(this, "Error al cargar la lista", Toast.LENGTH_SHORT).show();
            }
        } else {
            ActivityCompat.requestPermissions(this, PERMISOS, REQUEST_CODE);
        }

    }

    private void confirmacionEliminar(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("¿Está seguro de que desea ELIMINAR el libro?");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    ServicioLibros.getInstance(MainActivity.this).eliminar(position);
                } catch (IOException e) {
                    Toast.makeText(MainActivity.this, "Error al actualizar el archivo", Toast.LENGTH_SHORT).show();
                } catch (ClassNotFoundException e) {
                    Toast.makeText(MainActivity.this, "Error al eliminar el elemento", Toast.LENGTH_SHORT).show();
                }
                adapter.notifyDataSetChanged();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).create().show();
    }

    private void confirmacionPrestamo(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        try {
            if (ServicioLibros.getInstance(this).prestado(position))
                builder.setMessage("¿Está seguro de que desea DEVOLVER el libro?");
            else builder.setMessage("¿Está seguro de que desea PRESTAR el libro?");

            builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        ServicioLibros.getInstance(MainActivity.this).prestar(position);
                    } catch (IOException e) {
                        Toast.makeText(MainActivity.this, "Error al actualizar el archivo", Toast.LENGTH_SHORT).show();
                    } catch (ClassNotFoundException e) {
                        Toast.makeText(MainActivity.this, "Error al eliminar el elemento", Toast.LENGTH_SHORT).show();
                    }
                    adapter.notifyDataSetChanged();
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            }).create().show();
        } catch (ClassNotFoundException | IOException e) {
            Toast.makeText(this, "Error al verificar el estado del libro", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.accion_ayuda:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Ayuda")
                        .setMessage("1. Click: Prestar / Devolver libro.\n" +
                                "2. Click sostenido: Eliminar libro. ")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .create().show();
                break;
            case R.id.accion_agregar:
                DialogoAgregarLibros dialogo = new DialogoAgregarLibros();
                dialogo.show(getSupportFragmentManager(), DialogoAgregarLibros.TAG);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAgregarLibro(Libro libro) {
        try {
            ServicioLibros.getInstance(this).guardar(libro);
            adapter.notifyDataSetChanged();
        } catch (ClassNotFoundException e) {
            Toast.makeText(this, "Error al guardar el producto", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Error al guardar la lista", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            adapter = new LibroAdapter(ServicioLibros.getInstance(this).cargarDatos(),this,
                    new LibroAdapter.OnLibroAdapterListener() {
                        @Override
                        public void onPrestarLibro(int position) {
                            confirmacionPrestamo(position);
                        }

                        @Override
                        public void onEliminarLibro(int position) {
                            confirmacionEliminar(position);
                        }
                    });
            listaLibros.setAdapter(adapter);
            listaLibros.setLayoutManager(new LinearLayoutManager(this));
        } catch (IOException e) {
            Toast.makeText(this, "Error al cargar el archivo", Toast.LENGTH_SHORT).show();
        } catch (ClassNotFoundException e) {
            Toast.makeText(this, "Error al cargar la lista", Toast.LENGTH_SHORT).show();
        }
    }
}
