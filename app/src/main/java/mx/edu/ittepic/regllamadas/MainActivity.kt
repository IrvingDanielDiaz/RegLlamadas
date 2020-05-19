package mx.edu.ittepic.regllamadas

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CallLog
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {
    val siLecturaContactos = 18
    val siLecturaLlamadas = 17
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_CALL_LOG)!= PackageManager.PERMISSION_GRANTED){
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_CALL_LOG,android.Manifest.permission.READ_CONTACTS),siLecturaLlamadas)
    }

        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_CALL_LOG,android.Manifest.permission.READ_CONTACTS),siLecturaLlamadas)
        }

     button.setOnClickListener {
         leerRegistro()
     }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == siLecturaLlamadas){
            setTitle("Permiso Otorgado")
        }
        if(requestCode == siLecturaContactos){
            setTitle("Permiso Otorgado")
        }
    }

    private fun leerRegistro() {
        var resultado = ""
        val cursorLlamadas =
            if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED){
                contentResolver.query(CallLog.Calls.CONTENT_URI,null,null,null,null)
            }else{
                return
            }

        //INDICES
        val numero = cursorLlamadas!!.getColumnIndex(CallLog.Calls.NUMBER)
        val tipo = cursorLlamadas!!.getColumnIndex(CallLog.Calls.TYPE)
        val fecha = cursorLlamadas!!.getColumnIndex(CallLog.Calls.DATE)
        val duracion = cursorLlamadas!!.getColumnIndex(CallLog.Calls.DURATION)

        while (cursorLlamadas.moveToNext()){
            var numeroTelefono = cursorLlamadas.getString(numero)
            var tipoLlamada = cursorLlamadas.getString(tipo)
            var tipoCodigo = tipoLlamada.toInt()
            var tipoLlamadaTexto = ""
            when(tipoCodigo){
                CallLog.Calls.OUTGOING_TYPE -> tipoLlamadaTexto = "Llamada Saliente"
                CallLog.Calls.INCOMING_TYPE -> tipoLlamadaTexto = "Llamada Entrante"
                CallLog.Calls.MISSED_TYPE -> tipoLlamadaTexto = "Llamada Perdida"
            }
            resultado += "-------------\n"+
                    "Número: "+ numeroTelefono + "\n"+
                    "Tipo Llamada: "+ tipoLlamadaTexto+"\n"

        }
        salida.setText("Registro de Llamadas:\n"+resultado)

    }


}
