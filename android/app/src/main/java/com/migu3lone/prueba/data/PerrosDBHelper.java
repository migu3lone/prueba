
package com.migu3lone.prueba.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class PerrosDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "perros.db";
    private static final int DATABASE_VERSION = 1;

    // Constructor con Context como parámetro
    public PerrosDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear tabla si no existe
        String createTable = "CREATE TABLE perros (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT," +
                "raza TEXT," +
                "edad INTEGER," +
                "peso REAL," +
                "sexo TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS perros");
        onCreate(db);
    }

    // Método para obtener solo los nombres de los perros
    public List<String> obtenerNombresPerros() {
        List<String> nombresPerros = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Consulta SQL para obtener los nombres de los perros
        Cursor cursor = db.rawQuery("SELECT nombre FROM perros", null);

        if (cursor.moveToFirst()) {
            do {
                // Agregar los nombres de los perros a la lista
                nombresPerros.add(cursor.getString(cursor.getColumnIndex("nombre")));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return nombresPerros;
    }
}
