package com.migu3lone.prueba.ui.home;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.Manifest;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.migu3lone.prueba.R;
import com.migu3lone.prueba.data.PerrosDBHelper;
import com.migu3lone.prueba.databinding.FragmentHomeBinding;
import com.migu3lone.prueba.ui.adapter.PerrosAdapter;

public class HomeFragment extends Fragment {

    private Spinner spinnerPerros;
    private TextView textViewRaza, textViewEdad, textViewPeso, textViewSexo;
    private VideoView videoView;
    //private PerrosDBHelper perrosDBHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflar el layout para este fragmento
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Inicializar el Spinner, la base de datos y los TextViews
        spinnerPerros = view.findViewById(R.id.spinner_perros); // Usamos View en lugar de findViewById directamente
        //perrosDBHelper = new PerrosDBHelper(getContext());  // Usamos getContext() en lugar de 'this'
        textViewRaza = view.findViewById(R.id.textViewRaza);
        textViewEdad = view.findViewById(R.id.textViewEdad);
        textViewPeso = view.findViewById(R.id.textViewPeso);
        textViewSexo = view.findViewById(R.id.textViewSexo);

        // Cargar los datos de los perros en el Spinner
        cargarDatos();

        // Inicializar el VideoView
        videoView = view.findViewById(R.id.videoView);
        cargarVideo();

        // Configurar el listener para el Spinner
        spinnerPerros.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parentView, View view, int position, long id) {
                // Obtener el ID del perro seleccionado
                Integer idPerroSeleccionado = obtenerPerrosDesdeDB().get(position).second;
                // Mostrar la información del perro
                mostrarInformacionPerro(idPerroSeleccionado);
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parentView) {
                // Acción si no se selecciona nada
            }
        });

        return view;
    }

    private void cargarDatos() {
        // Obtener nombres de perros desde la base de datos
        List<Pair<String, Integer>> perros = obtenerPerrosDesdeDB();

        if (perros.isEmpty()) {
            Toast.makeText(requireContext(), "No se encontraron datos en la base de datos.", Toast.LENGTH_SHORT).show();
        } else {
            // Crear una lista de nombres para el Spinner
            List<String> nombresPerros = new ArrayList<>();
            for (Pair<String, Integer> perro : perros) {
                nombresPerros.add(perro.first);  // Agregar solo el nombre
            }

            // Configurar el adaptador para el Spinner
            PerrosAdapter adapter = new PerrosAdapter(requireContext(), R.layout.item_spinner, nombresPerros);

            spinnerPerros.setAdapter(adapter);

        }
    }

    private List<Pair<String, Integer>> obtenerPerrosDesdeDB() {
        List<Pair<String, Integer>> perros = new ArrayList<>();

        // Ruta al archivo de la base de datos
        File dirBaseDatos = new File(requireContext().getExternalFilesDir(null), "Databases");
        File dbFile = new File(dirBaseDatos, "perros.db");

        if (dbFile.exists()) {
            // Abrir la base de datos en modo lectura
            SQLiteDatabase database = SQLiteDatabase.openDatabase(dbFile.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);

            // Consultar los nombres de los perros
            Cursor cursor = database.rawQuery("SELECT id, nombre FROM perros ORDER BY nombre ASC", null);

            if (cursor.moveToFirst()) {
                do {
                    String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                    perros.add(new Pair<>(nombre, id));
                } while (cursor.moveToNext());
            }

            cursor.close();
            database.close();
        } else {
            Toast.makeText(requireContext(), "El archivo perros.db no se encontró en la ruta esperada.", Toast.LENGTH_SHORT).show();
        }

        return perros;
    }

    //private void mostrarInformacionPerro(String nombrePerro) {
    private void mostrarInformacionPerro(int idPerro) {
        // Consultar la base de datos para obtener detalles del perro
        File dirBaseDatos = new File(requireContext().getExternalFilesDir(null), "Databases");
        File dbFile = new File(dirBaseDatos, "perros.db");

        if (dbFile.exists()) {
            SQLiteDatabase database = SQLiteDatabase.openDatabase(dbFile.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
            Cursor cursor = database.rawQuery("SELECT raza, edad, peso, sexo FROM perros WHERE id = ?", new String[]{String.valueOf(idPerro)});

            if (cursor.moveToFirst()) {
                // Obtener los datos del perro
                String raza = cursor.getString(cursor.getColumnIndexOrThrow("raza"));
                String edad = cursor.getString(cursor.getColumnIndexOrThrow("edad"));
                String peso = cursor.getString(cursor.getColumnIndexOrThrow("peso"));
                String sexo = cursor.getString(cursor.getColumnIndexOrThrow("sexo"));

                // Convertir sexo (M = Macho, H = Hembra)
                if ("M".equals(sexo)) {
                    sexo = "Macho";
                } else if ("H".equals(sexo)) {
                    sexo = "Hembra";
                }

                // Mostrar los datos en los TextViews
                textViewRaza.setText("Raza: " + raza);
                textViewEdad.setText("Edad: " + edad + " años");
                textViewPeso.setText("Peso: " + peso + " Kg.");
                textViewSexo.setText("Sexo: " + sexo);
            } else {
                // Si no se encuentra el perro, mostrar mensaje
                Toast.makeText(requireContext(), "No se encontró el perro en la base de datos.", Toast.LENGTH_SHORT).show();
            }

            cursor.close();
            database.close();
        } else {
            Toast.makeText(requireContext(), "El archivo perros.db no se encontró en la ruta esperada.", Toast.LENGTH_SHORT).show();
        }
    }

    private void cargarVideo() {
        // Configurar la ruta del video (puedes usar un archivo local o una URL)
        String videoPath = "https://github.com/migu3lone/prueba/blob/main/resource/video.mp4?raw=true"; // URL del video
        //String videoPath = "android.resource://" + requireContext().getPackageName() + "/" + R.raw.video;
        Uri videoUri = Uri.parse(videoPath);

        // Asignar el video al VideoView
        videoView.setVideoURI(videoUri);

        // Agregar controles de reproducción
        MediaController mediaController = new MediaController(requireContext());
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

        // Reproducir automáticamente
        videoView.setOnPreparedListener(mp -> {
            mp.setLooping(true); //bucle
            videoView.start();
        });
    }

/*
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }*/
}


/*

public class HomeFragment extends Fragment {

    //private FragmentHomeBinding binding;
    private Spinner spinnerPerros;
    private TextView textViewRaza, textViewEdad, textViewPeso, textViewSexo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflar el layout para este fragmento
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Inicializar el Spinner, la base de datos y los TextViews
        spinnerPerros = view.findViewById(R.id.spinner_perros); // Usamos View en lugar de findViewById directamente
        //perrosDBHelper = new PerrosDBHelper(getContext());  // Usamos getContext() en lugar de 'this'
        textViewRaza = view.findViewById(R.id.textViewRaza);
        textViewEdad = view.findViewById(R.id.textViewEdad);
        textViewPeso = view.findViewById(R.id.textViewPeso);
        textViewSexo = view.findViewById(R.id.textViewSexo);

        // Cargar los datos de los perros en el Spinner
        cargarDatos();

        // Configurar el listener para el Spinner
        spinnerPerros.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parentView, View view, int position, long id) {
                // Obtener el nombre del perro seleccionado
                String nombrePerroSeleccionado = (String) parentView.getItemAtPosition(position);
                // Mostrar la información del perro
                mostrarInformacionPerro(nombrePerroSeleccionado);
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parentView) {
                // Acción si no se selecciona nada
            }
        });

        return view;
    }

    private void cargarDatos() {
        // Obtener nombres de perros desde la base de datos
        List<String> nombresPerros = obtenerNombresPerrosDesdeDB();

        if (nombresPerros.isEmpty()) {
            Toast.makeText(requireContext(), "No se encontraron datos en la base de datos.", Toast.LENGTH_SHORT).show();
        } else {
            // Configurar el adaptador para el Spinner
            PerrosAdapter adapter = new PerrosAdapter(requireContext(), R.layout.item_spinner, nombresPerros);

            spinnerPerros.setAdapter(adapter);
        }
    }

    private List<String> obtenerNombresPerrosDesdeDB() {
        List<String> nombres = new ArrayList<>();

        // Ruta al archivo de la base de datos
        File dirBaseDatos = new File(requireContext().getExternalFilesDir(null), "Databases");
        File dbFile = new File(dirBaseDatos, "perros.db");

        if (dbFile.exists()) {
            // Abrir la base de datos en modo lectura
            SQLiteDatabase database = SQLiteDatabase.openDatabase(dbFile.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);

            // Consultar los nombres de los perros
            Cursor cursor = database.rawQuery("SELECT id, nombre FROM perros ORDER BY nombre ASC", null);

            if (cursor.moveToFirst()) {
                do {
                    nombres.add(cursor.getString(cursor.getColumnIndexOrThrow("nombre")));
                } while (cursor.moveToNext());
            }

            cursor.close();
            database.close();
        } else {
            Toast.makeText(requireContext(), "El archivo perros.db no se encontró en la ruta esperada.", Toast.LENGTH_SHORT).show();
        }

        return nombres;
    }

    private void mostrarInformacionPerro(String nombrePerro) {
        // Consultar la base de datos para obtener detalles del perro
        File dirBaseDatos = new File(requireContext().getExternalFilesDir(null), "Databases");
        File dbFile = new File(dirBaseDatos, "perros.db");

        if (dbFile.exists()) {
            SQLiteDatabase database = SQLiteDatabase.openDatabase(dbFile.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
            Cursor cursor = database.rawQuery("SELECT raza, edad, peso, sexo FROM perros WHERE id = ?", new String[]{nombrePerro});


            if (cursor.moveToFirst()) {
                // Obtener los datos del perro
                String raza = cursor.getString(cursor.getColumnIndexOrThrow("raza"));
                String edad = cursor.getString(cursor.getColumnIndexOrThrow("edad"));
                String peso = cursor.getString(cursor.getColumnIndexOrThrow("peso"));
                String sexo = cursor.getString(cursor.getColumnIndexOrThrow("sexo"));

                // Mostrar los datos en los TextViews
                textViewRaza.setText("Raza: " + raza);
                textViewEdad.setText("Edad: " + edad);
                textViewPeso.setText("Peso: " + peso);
                textViewSexo.setText("Sexo: " + sexo);
            } else {
                // Si no se encuentra el perro, mostrar mensaje
                Toast.makeText(requireContext(), "No se encontró el perro en la base de datos.", Toast.LENGTH_SHORT).show();
            }

            cursor.close();
            database.close();
        } else {
            Toast.makeText(requireContext(), "El archivo perros.db no se encontró en la ruta esperada.", Toast.LENGTH_SHORT).show();
        }
    }

}

 */

