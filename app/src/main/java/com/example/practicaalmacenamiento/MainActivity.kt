package com.example.practicaalmacenamiento

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    val WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 101
    lateinit var btnGuardar : Button
    lateinit var editData : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        escrituraAlmacenamientoInterno(this, "archivo_interno.txt", "Este es un contenido de almacenamiento INTERNO")
        escrituraAlmacenamientoExterno(this, "archivo_externo.txt", "Este es un contenido de almacenamiento EXTERNO")

        btnGuardar = findViewById(R.id.btn_guardar)
        editData = findViewById(R.id.txt_data)

        btnGuardar.setOnClickListener{
            escrituraAlmacenamientoInterno(this, "texto_ingresado.txt", editData.text.toString())
        }
    }

    fun escrituraAlmacenamientoInterno(context: Context, filename: String, content: String) {
        val filePath = context.filesDir.absolutePath+"/$filename"
        val file = File(filePath)

        try {
            file.writeText(content)
            Log.v("Escritura en almacenamiento INTERNO", "RUTA: '${filePath}'")
        }catch (e: Exception){
            e.printStackTrace();
        }
    }


    fun escrituraAlmacenamientoExterno(context: Context, filename: String, content: String) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            val filePath = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)!!.absolutePath+"/$filename"
            val file = File(filePath)

            try {
                file.writeText(content)
                Log.v("Escritura Archivos", "Ruta: '${filePath}'");
            }catch (e: Exception){
                e.printStackTrace();
            }
        }else {
            val filepath = context.getExternalFilesDir(null)!!.absolutePath+"/$filename"
            val file = File(filepath)

            if(ContextCompat.checkSelfPermission(context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                try {
                    file.writeText(content)
                    Log.v("Escritura Archivos", "Ruta: '${filepath}'");
                }catch (e: Exception){
                    e.printStackTrace();
                }
            }else {
                ActivityCompat.requestPermissions((context as Activity), arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), WRITE_EXTERNAL_STORAGE_REQUEST_CODE)
            }
        }
    }
}