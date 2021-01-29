package com.example.sorteodelegado

import android.app.AlertDialog
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_sorteo.*

class Sorteo : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sorteo)

        btnSortear.setOnClickListener {
            if  (!isSorteoDone()){
                val candidatos = getCandidatos()
                val strCandid = candidToString(candidatos)
                showAlert(strCandid)
                getGreatest()
                saveSorteoDone(true)
            }else{
                Toast.makeText(this,"el sorteo ya ha sido realizado",Toast.LENGTH_SHORT).show()
            }
        }

        btnResetSorteo.setOnClickListener {
            saveSorteoDone(false)
        }

    }

    /**
     * Guarda en una shared preference si el sorteo ha sido realizado o no
     */
    private fun saveSorteoDone(done: Boolean) {
        val prefs = getSharedPreferences("sharedPrefsSorteo", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putBoolean("SORTEO_DONE_KEY",done).apply()
    }

    /**
     * @return el booleano que indica si el sorteo ha sido realizado o no
     */
    private fun isSorteoDone():Boolean {
        val prefs = getSharedPreferences("sharedPrefsSorteo", Context.MODE_PRIVATE)
        val savedBool = prefs.getBoolean("SORTEO_DONE_KEY",false)
        return savedBool
    }

    /**
     * Cambia el TextView y encuentra el candidato con mayor peso
     */
    private fun getGreatest() {
        val admin = AdminSQLite(this,"sorteodb",null,1)
        val bd = admin.writableDatabase
        val fila = bd.rawQuery("select * from candidatos where peso = (select max(peso) from candidatos)",null)
        if (fila.moveToFirst()){
            tvResultado.text = "el ganador es : " + fila.getString(0)+" -> "+fila.getString(1)
        }else{
            Toast.makeText(this, "No existe un artículo con dicha descripción",
                Toast.LENGTH_SHORT).show()
            bd.close()
        }
    }

    /**
     * @return La string de todos los candidatos
     */
    private fun candidToString(candidatos: MutableList<Candidato>): String {
        var str: String = ""
        for(candidato in candidatos){
            str = str + candidato.nombre + " : " + candidato.peso + "\n"
        }
        return str
    }

    /**
     * @return un mutableList de todos los candidatos
     */
    private fun getCandidatos():MutableList<Candidato> {
        val admin = AdminSQLite(this,"sorteodb",null,1)
        val bd = admin.readableDatabase
        var cursor: Cursor? = null

        try{
            cursor = bd.rawQuery("select * from candidatos",null)
        }catch(e: SQLiteException){
            bd.execSQL("select * from candidatos")
            return ArrayList()
        }

        var nombre: String
        var peso: String

        val candidatos = mutableListOf<Candidato>()

        if(cursor.moveToFirst()){
            do {
                nombre = cursor.getString(cursor.getColumnIndex("nombre"))
                peso = cursor.getInt(cursor.getColumnIndex("peso")).toString()
                candidatos.add(Candidato(nombre,peso))
            }while(cursor.moveToNext())
        }
        return candidatos
    }

    /**
     * Muestra una alerta con todos los candidatos y sus pesos
     */
    private fun showAlert(texto: String?) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("candidatos")
        builder.setMessage(texto)
        val dialog = builder.create()
        dialog.show()
    }
}