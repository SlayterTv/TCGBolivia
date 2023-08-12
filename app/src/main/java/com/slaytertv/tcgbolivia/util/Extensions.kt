package com.slaytertv.tcgbolivia.util

//noinspection SuspiciousImport
import android.R
import android.app.AlertDialog
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment

class Extensions {
}
fun Fragment.toast(msg:String?){ Toast.makeText(requireContext(),msg, Toast.LENGTH_LONG).show() }
fun Fragment.dialogx(msg:String?){
    val builder = AlertDialog.Builder(requireContext(), R.style.Theme_Dialog)
    builder.setTitle("Mensaje")
    builder.setMessage(msg)
    builder.show()
}

//verifica q el campo email no este vacio
fun String.isValidEmail() =
    isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()

//habilitar o desabilitar boton
fun View.clickdisable(){ isClickable = false }
fun View.clickenable(){ isClickable = true }

//extensiones para mostrar y ocultar vistas como progressbar
fun View.hide(){ visibility = View.GONE }
fun View.show(){ visibility = View.VISIBLE }