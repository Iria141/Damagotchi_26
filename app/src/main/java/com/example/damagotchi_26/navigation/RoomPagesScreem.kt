package com.example.damagotchi_26.navigation

import android.media.MediaPlayer
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.damagotchi_26.R
import com.example.damagotchi_26.ui.Color.Color.PinkBg
import com.example.damagotchi_26.ui.Color.Color.PurpleBtn
import com.example.damagotchi_26.ui.components.EcografiaDialog
import com.example.damagotchi_26.ui.components.TrimestreDialog
import com.example.damagotchi_26.ui.evaluacion.PerdidaDefinitivaScreen
import com.example.damagotchi_26.ui.evaluacion.PerdidaEmbarazoScreen
import com.example.damagotchi_26.ui.rooms.BathRoom
import com.example.damagotchi_26.ui.rooms.BedRoom
import com.example.damagotchi_26.ui.rooms.EvaluacionFinalScreen
import com.example.damagotchi_26.ui.rooms.Kitchen
import com.example.damagotchi_26.ui.rooms.LivingRoom
import com.example.damagotchi_26.ui.rooms.Park
import com.example.damagotchi_26.viewmodel.PetViewModel
import com.example.damagotchi_26.viewmodel.TransicionViewModel
import com.example.damagotchi_26.data.AvisoTrimestre
import com.example.damagotchi_26.data.EventoEspecial
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomsPagerScreen(
    transicionViewModel: TransicionViewModel,
    petViewModel: PetViewModel,
    rol: String,
    onVolverMenu: () -> Unit = {}
) {
    val petEstado         by petViewModel.pet.collectAsState(initial = null)
    val perdidaEmbarazo   by transicionViewModel.perdidaEmbarazo.collectAsState()
    val perdidaDefinitiva by transicionViewModel.perdidaDefinitiva.collectAsState()
    val eventoEcografia: EventoEspecial? by transicionViewModel.eventoConImagen.collectAsState()
    val avisoTrimestre: AvisoTrimestre? by transicionViewModel.avisoTrimestre.collectAsState()

    var mostrarEvaluacion        by remember { mutableStateOf(false) }
    var mostrarPerdidaDefinitiva by remember { mutableStateOf(false) }

    var musicaActivada   by remember { mutableStateOf(true) }
    var sonidosActivados by remember { mutableStateOf(true) }

    val uid = FirebaseAuth.getInstance().currentUser?.uid
    LaunchedEffect(uid) {
        if (uid == null) return@LaunchedEffect
        FirebaseFirestore.getInstance().collection("users").document(uid).get()
            .addOnSuccessListener { doc ->
                musicaActivada   = doc.getBoolean("musica_activada")   ?: true
                sonidosActivados = doc.getBoolean("sonidos_activados") ?: true
            }
    }

    if (petEstado == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val pet   = petEstado!!
    val rooms = listOf("Salón", "Cocina", "Dormitorio", "Baño", "Parque")
    val pagerState = rememberPagerState(pageCount = { rooms.size })

    LaunchedEffect(pet.semanaEmbarazo) {
        transicionViewModel.comprobarAvisoTrimestre(pet.semanaEmbarazo, rol = rol)
        transicionViewModel.comprobarEventoEspecial(pet.semanaEmbarazo, rol = rol)
        if (pet.semanaEmbarazo >= 40 && !mostrarEvaluacion) {
            mostrarEvaluacion = true
        }
    }

    LaunchedEffect(perdidaDefinitiva) {
        if (perdidaDefinitiva && !mostrarPerdidaDefinitiva && !mostrarEvaluacion) {
            mostrarPerdidaDefinitiva = true
        }
    }


    LaunchedEffect(pet.diaEmbarazo) {
        transicionViewModel.sincronizarDia(pet.diaEmbarazo)
        transicionViewModel.comprobarPerdida(
            energia   = pet.energia,
            hambre    = pet.hambre,
            sed       = pet.sed,
            limpieza  = pet.limpieza,
            actividad = pet.actividad,
            descanso  = pet.descanso
        )
    }

    val context = LocalContext.current
    val mediaPlayer = remember {
        MediaPlayer.create(context, R.raw.sergequadrado).apply { isLooping = true }
    }

    LaunchedEffect(musicaActivada) {
        if (musicaActivada) { if (!mediaPlayer.isPlaying) mediaPlayer.start() }
        else                { if (mediaPlayer.isPlaying)  mediaPlayer.pause()  }
    }

    DisposableEffect(Unit) {
        onDispose { mediaPlayer.stop(); mediaPlayer.release() }
    }

    Scaffold(containerColor = PinkBg) { padding ->
        Column(
            modifier = Modifier
                .padding(top = 50.dp)
                .fillMaxSize()
        ) {
            val trimestre = when (pet.semanaEmbarazo) {
                in 1..12  -> "Primer trimestre"
                in 13..27 -> "Segundo trimestre"
                else      -> "Tercer trimestre"
            }
            Text(
                text       = "Semana ${pet.semanaEmbarazo} · $trimestre",
                modifier   = Modifier
                    .padding(bottom = 12.dp)
                    .fillMaxWidth(),
                textAlign  = androidx.compose.ui.text.style.TextAlign.Center,
                fontSize   = 18.sp,
                color      = PurpleBtn,
                fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(4.dp))

            RoomDots(
                total    = rooms.size,
                current  = pagerState.currentPage,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            HorizontalPager(
                state    = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> LivingRoom(
                        pet              = pet,
                        sonidosActivados = sonidosActivados,
                        tocarPiano       = { petViewModel.tocarPiano() },
                        pintar           = { petViewModel.pintar() }
                    )
                    1 -> Kitchen(
                        pet              = pet,
                        sonidosActivados = sonidosActivados,
                        comer            = { petViewModel.alimentar() },
                        beber            = { petViewModel.hidratar() },
                        prepararComida   = { petViewModel.prepararComida() }
                    )
                    2 -> BedRoom(
                        pet              = pet,
                        sonidosActivados = sonidosActivados,
                        dormir           = { petViewModel.dormir() },
                        siesta           = { petViewModel.siesta() }
                    )
                    3 -> BathRoom(
                        pet              = pet,
                        sonidosActivados = sonidosActivados,
                        bano             = { petViewModel.bano() },
                        ducharse         = { petViewModel.ducharse() },
                        lavarDientes     = { petViewModel.lavarDientes() },
                        cuidarPiel       = { petViewModel.cuidarPiel() }
                    )
                    4 -> Park(
                        pet              = pet,
                        sonidosActivados = sonidosActivados,
                        caminar          = { petViewModel.caminar() },
                        estirar          = { petViewModel.estirar() },
                        jugarPelota      = { petViewModel.jugarPelota() }
                    )
                }
            }
        }
    }

    if (avisoTrimestre != null
        && eventoEcografia == null
        && !perdidaEmbarazo
        && !mostrarPerdidaDefinitiva
        && !mostrarEvaluacion
    ) {
        TrimestreDialog(
            titulo    = avisoTrimestre!!.titulo,
            mensaje   = avisoTrimestre!!.mensaje,
            semana    = avisoTrimestre!!.semana,
            onDismiss = { transicionViewModel.descartarAvisoTrimestre() }
        )
    }

    if (eventoEcografia != null
        && !perdidaEmbarazo
        && !mostrarPerdidaDefinitiva
        && !mostrarEvaluacion
    ) {
        EcografiaDialog(
            evento    = eventoEcografia!!,
            rol       = rol,
            onDismiss = {
                transicionViewModel.descartarEventoImagen(eventoEcografia!!.semana)
            }
        )
    }

    if (perdidaEmbarazo && !perdidaDefinitiva) {
        PerdidaEmbarazoScreen(
            onReintentar = {
                petViewModel.resetearMedidores()
                transicionViewModel.resetearPerdida()
            },
            onVolverMenu = { transicionViewModel.resetearPerdida() }
        )
    }

    if (mostrarPerdidaDefinitiva && !mostrarEvaluacion) {
        PerdidaDefinitivaScreen(
            onVerEvaluacion = {
                mostrarPerdidaDefinitiva = false
                mostrarEvaluacion        = true
            }
        )
    }

    if (mostrarEvaluacion) {
        EvaluacionFinalScreen(
            pet               = pet,
            perdidaDefinitiva = perdidaDefinitiva,
            onVolverMenu      = onVolverMenu,
            onReiniciarJuego  = {
                petViewModel.resetearJuego()
                transicionViewModel.resetearTodo()
            }
        )
    }
}

@Composable
private fun RoomDots(total: Int, current: Int, modifier: Modifier = Modifier) {
    Row(
        modifier              = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        repeat(total) { index ->
            val selected = index == current
            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(if (selected) 10.dp else 8.dp)
            ) {
                Surface(
                    shape    = MaterialTheme.shapes.small,
                    color    = if (selected) PurpleBtn else Color.White,
                    modifier = Modifier.fillMaxSize()
                ) {}
            }
        }
    }
}