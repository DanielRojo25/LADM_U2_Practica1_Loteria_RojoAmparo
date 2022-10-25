package com.example.ladm_u2_practica1_loteria_rojoamparo

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.ladm_u2_practica1_loteria_rojoamparo.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var cartas : LoteriaCartas
    lateinit var mp : MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cartas  = LoteriaCartas(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var cartasFaltantes = cartas.nombresCartas

        val corrutina = CoroutineScope(Job() + Dispatchers.Main)
        var faltantes = corrutina.launch(EmptyCoroutineContext, CoroutineStart.LAZY){

            for (i in cartasFaltantes){
                delay(2000)
                runOnUiThread {
                    binding.faltantes.text = i
                }
            }
        }

        binding.iniciar.setOnClickListener{
            try{
                cartas.start()

            }catch (e:Exception){
                AlertDialog.Builder(this)
                    .setTitle("Importante")
                    .setMessage("¡El hilo ya se ejecuto!")
                    .setPositiveButton("OK"){d,i->}
                    .show()
            }

        }

        binding.terminar.setOnClickListener {
            cartas.terminarLoteria()
            binding.txt.text = "¡FIN DEL JUEGO!"
        }

        binding.pausa.setOnClickListener {
            cartas.pausar()
            binding.txt.text = "¡Juego Pausado!"

            AlertDialog.Builder(this)
                .setTitle("Importante")
                .setMessage("Para reanudar el juego presione de nuevo pausa")
                .setPositiveButton("OK"){d,i->
                    binding.txt.text = ""
                }
                .show()
        }

        binding.falta.setOnClickListener {
            faltantes.start()
        }

    }

}

class LoteriaCartas(puntero: MainActivity): Thread(){

    var p = puntero

    var iniciar = true
    var pausar  = false

    var cartasLoteria = arrayListOf(R.drawable.gallo,R.drawable.diablo,R.drawable.dama,R.drawable.catrin,R.drawable.paraguas,
        R.drawable.sirena,R.drawable.escalera,R.drawable.botella,R.drawable.barril,R.drawable.arbol,R.drawable.melon,
        R.drawable.valiente,R.drawable.gorrito,R.drawable.muerte,R.drawable.pera,R.drawable.bandera,R.drawable.bandolon,
        R.drawable.violoncello,R.drawable.garza,R.drawable.pajaro,R.drawable.mano,R.drawable.bota,R.drawable.luna,
        R.drawable.cotorro,R.drawable.borracho,R.drawable.negrito,R.drawable.corazon,R.drawable.sandia,R.drawable.tambor,
        R.drawable.camaron,R.drawable.jaras,R.drawable.musico,R.drawable.arana,R.drawable.soldado,R.drawable.estrella,
        R.drawable.cazo,R.drawable.mundo,R.drawable.apache,R.drawable.nopal,R.drawable.alacran,R.drawable.rosa,
        R.drawable.calavera,R.drawable.campana,R.drawable.cantarito,R.drawable.venado,R.drawable.sol,R.drawable.corona,
        R.drawable.chalupa,R.drawable.pino,R.drawable.pescado,R.drawable.palma,R.drawable.maceta,R.drawable.arpa,
        R.drawable.rana)

    var nombresCartas = arrayListOf("GALLO","DIABLO","DAMA","CATRIN","PARAGUAS","SIRENA","ESCALERA","BOTELLA",
        "BARRIL","ARBOL","MELON","VALINTE","GORRITO","MUERTE","PERA","BANDERA","BANDOLON","VIOLONCELLO","GARZAR",
        "PAJARO","MANO","BOTA","LUNA","COTORRO","BORRACHO","NEGRO","CORAZON","SANDIA","TAMBOR","CAMARON","JARAS",
        "MUSICO","ARAÑA","SOLDADO","ESTRELLA","CAZO","MUNDO","APACHE","NOPaL","ALACRAN","ROSA","CALAVERA","CAMPANA",
        "CANTARITO","VENADO","SOL","CORONA","CHALUPA","PINO","PESCADO","PALMA","MACETA","ARPA","RANA")

    var sonidoCartas = arrayListOf(R.raw.carta1, R.raw.carta2, R.raw.carta3, R.raw.carta4,
        R.raw.carta5, R.raw.carta6, R.raw.carta7, R.raw.carta8, R.raw.carta9,
        R.raw.carta10, R.raw.carta11, R.raw.carta12, R.raw.carta13, R.raw.carta14,
        R.raw.carta15, R.raw.carta16, R.raw.carta17, R.raw.carta18, R.raw.carta19,
        R.raw.carta20, R.raw.carta21, R.raw.carta22, R.raw.carta23, R.raw.carta24,
        R.raw.carta25, R.raw.carta26, R.raw.carta27, R.raw.carta28, R.raw.carta29,
        R.raw.carta30, R.raw.carta31, R.raw.carta32, R.raw.carta33, R.raw.carta34,
        R.raw.carta35, R.raw.carta36, R.raw.carta37, R.raw.carta38, R.raw.carta39,
        R.raw.carta40, R.raw.carta41, R.raw.carta42, R.raw.carta43, R.raw.carta44,
        R.raw.carta45, R.raw.carta46, R.raw.carta47, R.raw.carta48, R.raw.carta49,
        R.raw.carta50, R.raw.carta51, R.raw.carta52, R.raw.carta53, R.raw.carta54)

    fun loteriaBarajear(){

        val aleatorio = Random.nextInt(cartasLoteria.size)

        p.binding.cartaLoteria.setImageResource(cartasLoteria[aleatorio])

        p.mp = MediaPlayer.create(p,sonidoCartas[aleatorio])
        p.mp.start()

        cartasLoteria.removeAt(aleatorio)
        nombresCartas.removeAt(aleatorio)
        sonidoCartas.removeAt(aleatorio)

        sleep(1000)
    }

    fun terminarLoteria(){
        iniciar = false
        p.mp.stop()
        p.binding.cartaLoteria.setImageResource(R.drawable.gallo)
    }

    fun pausar(){
        pausar = pausar == false
    }

    override fun run() {

        super.run()

        while (iniciar){

            if(!pausar){

                if(cartasLoteria.size>0) {
                    p.runOnUiThread{
                        loteriaBarajear()
                    }
                }

                if(cartasLoteria.size==0){
                    p.runOnUiThread{
                        p.binding.txt.text="SE TERMINARON LAS CARTAS"
                    }
                }

                sleep(2000)

            }

        }

    }

}
