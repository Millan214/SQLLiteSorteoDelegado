package com.example.sorteodelegado

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Clase para crear la tabla y administrarla
 */
class AdminSQLite(context: Context,
                            name: String,
                            factory: SQLiteDatabase.CursorFactory?,
                            version: Int)
    : SQLiteOpenHelper(context,name,factory,version)
{
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table candidatos(nombre text primary key, peso int)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

}