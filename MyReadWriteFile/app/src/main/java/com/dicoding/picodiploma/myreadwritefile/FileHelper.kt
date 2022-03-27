package com.dicoding.picodiploma.myreadwritefile

import android.content.Context

internal object FileHelper { /* <- sifatnya yang static */

    /*  menyimpan data yang bertipekan string ke dalam sebuah berkas pada internal storage
    *       metode openFileOutput() untuk membuka berkas sesuai dengan namanya.
    *       Jika berkas belum ada, maka berkas tersebut akan secara otomatis dibuatkan.*/
    fun writeToFile(fileModel: FileModel, context: Context) {
        context.openFileOutput(fileModel.filename, Context.MODE_PRIVATE).use {
            it.write(fileModel.data?.toByteArray())
        }
    }

    /* menggunakan komponen FileInputStream dengan metode openFileInput
    *       Data pada berkas akan dibaca menggunakan stream
    *       data pada tiap baris dalam berkas akan mampu diperoleh dengan menggunakan bufferedReader. */
    fun readFromFile(context: Context, filename: String): FileModel {
        val fileModel = FileModel()
        fileModel.filename = filename
        fileModel.data = context.openFileInput(filename).bufferedReader().useLines { lines ->
            lines.fold("") { some, text ->
                "$some\n$text"
            }
        }
        return fileModel
    }

}
