package com.example.sorteodelegado

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnPresentarse.setOnClickListener{
            guardarNombre(etNombre.text.toString())
            val intent = Intent(this,Sorteo::class.java)
            startActivity(intent)
        }

    }

    /**
     * Guarda el nombre y calcula con un random el peso de un candidato
     * y lo guarda en la BD
     */
    private fun guardarNombre(nombre: String) {
        val admin = AdminSQLite(this,"sorteodb",null,1)
        val bd = admin.writableDatabase
        val fila = ContentValues()
        fila.put("nombre",nombre)
        fila.put("peso", Random.nextInt(0,100))
        bd.insert("candidatos",null,fila)
        bd.close()
        Toast.makeText(this,"candidato agregado correctamente",Toast.LENGTH_SHORT).show()
    }
}