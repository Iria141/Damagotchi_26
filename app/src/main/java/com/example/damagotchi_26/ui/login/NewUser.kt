package com.example.damagotchi_26.ui.login


import android.util.Patterns
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.damagotchi_26.ui.components.AuthBackground
import com.example.damagotchi_26.ui.components.AuthCard
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import com.example.damagotchi_26.ui.components.AuthTextField
import kotlinx.coroutines.launch
import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.ui.platform.LocalContext
import java.util.Calendar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp


fun register(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
    val auth = FirebaseAuth.getInstance()

    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) onResult(true, null)
            else onResult(false, task.exception?.message)
        }
}

@Composable
fun NewUser(
    onCreate: (
        nombre: String,
        fecha: String,
        rol: String,
        semana: String,
        sexo: String,
        email: String,
        pass: String
    ) -> Unit,
    onBack: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var rol by remember { mutableStateOf("") }
    var semana by remember { mutableStateOf("") }
    var sexo by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var pass2 by remember { mutableStateOf("") }
    var nombreError by remember { mutableStateOf(false) }
    var fechaError by remember { mutableStateOf(false) }
    var rolError by remember { mutableStateOf(false) }
    var semanaError by remember { mutableStateOf(false) }
    var sexoError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var passError by remember { mutableStateOf(false) }
    var pass2Error by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()


    //Selector de fecha
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDay ->
            fecha = "$selectedDay/${selectedMonth + 1}/$selectedYear"
        },
        year,
        month,
        day
    ).apply {
        datePicker.maxDate = calendar.timeInMillis //limita las fecha hasta dia de hoy
    }

    //desplegable rol y sexo
    var expandedrol by remember { mutableStateOf(false) }
    var expandedsexo by remember { mutableStateOf(false) }




    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        AuthBackground {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                AuthTextField(
                    value = nombre,
                    onValueChange = {
                        nombre = it
                        nombreError = false
                    },
                    label = "Nombre",
                    isError = nombreError,
                    errorMessage = "Introduce tu nombre"
                )

                AuthTextField(
                    value = fecha,
                    onValueChange = {},
                    label = "Fecha de nacimiento",
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.CalendarMonth,
                            contentDescription = "Seleccionar fecha",
                            tint = Color.DarkGray,
                            modifier = Modifier
                                .size(30.dp)
                                .clickable {
                                    datePickerDialog.show()
                                }
                        )
                    },
                    isError = fechaError,
                    errorMessage = "Introduce tu fecha de nacimiento",

                    )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    AuthTextField(
                        value = rol,
                        onValueChange = {},
                        label = "Rol",
                        readOnly = true,
                        isError = rolError,
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Filled.ArrowDropDown,
                                contentDescription = "Desplegar",
                                tint = Color.DarkGray,
                                modifier = Modifier.clickable { expandedrol = !expandedrol }
                            )
                        },
                        modifier = Modifier
                            .clickable { expandedrol = true }
                    )

                    DropdownMenu(
                        expanded = expandedrol,
                        onDismissRequest = { expandedrol = false }
                    ) {
                        listOf("Mamá", "Papá", "Otro").forEach { opcion ->
                            DropdownMenuItem(
                                text = { Text(opcion) },
                                onClick = {
                                    rol = opcion
                                    rolError = false
                                    expandedrol = false
                                }
                            )
                        }
                    }
                }

                if (rolError) {
                    Text(
                        text = "Selecciona una opción",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 20.dp, top = 4.dp)
                    )
                }


                AuthTextField(
                    value = semana,
                    onValueChange = {
                        semana = it.filter { c -> c.isDigit() } //solo deja introducir números
                        semanaError = false
                    },
                    label = "Semana de gestación",
                    isError = semanaError,
                    errorMessage = "Introduce la semana de gestación",
                    keyboardType = KeyboardType.Number //teclado numerico
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    AuthTextField(
                        value = sexo,
                        onValueChange = {},
                        label = "Sexo del bebé",
                        readOnly = true,
                        isError = sexoError,
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Filled.ArrowDropDown,
                                contentDescription = "Desplegar",
                                tint = Color.DarkGray,
                                modifier = Modifier.clickable { expandedsexo = !expandedsexo }
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expandedsexo = !expandedsexo },
                    )

                    DropdownMenu(
                        expanded = expandedsexo,
                        onDismissRequest = { expandedsexo = false }
                    ) {
                        listOf("Niño", "Niña", "Desconocido aún").forEach { opcion ->
                            DropdownMenuItem(
                                text = { Text(opcion) },
                                onClick = {
                                    sexo = opcion
                                    sexoError = false
                                    expandedsexo = false
                                }
                            )
                        }
                    }
                }

                if (sexoError) {
                    Text(
                        text = "Selecciona una opción",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 20.dp, top = 4.dp)
                    )
                }

                AuthTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = false
                    },
                    label = "Email",
                    isError = emailError,
                    errorMessage = "Email no válido"
                )

                AuthTextField(
                    value = pass,
                    onValueChange = {
                        pass = it
                        passError = false
                    },
                    label = "Contraseña",
                    isPassword = true,
                    isError = passError,
                    errorMessage = "Mínimo 6 caracteres"
                )

                AuthTextField(
                    value = pass2,
                    onValueChange = {
                        pass2 = it
                        pass2Error = false
                    },
                    label = "Confirmar contraseña",
                    isPassword = true,
                    isError = pass2Error,
                    errorMessage = "Las contraseñas no coinciden"
                )


                Spacer(Modifier.height(14.dp))

                Button(
                    onClick = {
                        scope.launch {
                            nombreError = false
                            fechaError = false
                            rolError = false
                            semanaError = false
                            sexoError = false
                            emailError = false
                            passError = false

                            when {
                                nombre.isBlank() -> {
                                    nombreError = true
                                }

                                fecha.isBlank() -> {
                                    fechaError = true
                                }

                                rol.isBlank() -> {
                                    rolError = true
                                }

                                semana.isBlank() -> {
                                    semanaError = true
                                }

                                sexo.isBlank() -> {
                                    sexoError = true
                                }

                                email.isBlank() -> {
                                    emailError = true
                                }

                                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                                    emailError = true
                                }

                                pass.isBlank() -> {
                                    passError = true
                                }

                                pass.length < 6 -> {
                                    passError = true
                                }

                                pass != pass2 -> {
                                    pass2Error = true
                                }

                                else ->
                                    onCreate(nombre, fecha, rol, semana, sexo, email, pass)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 100.dp)
                ) {
                    Text("Crear cuenta")
                }

                TextButton(
                    onClick = onBack,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text("Volver")
                }
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NewUserPreview() {
    MaterialTheme {
        Surface {
            NewUser(
                onCreate = { _, _, _, _, _, _, _ -> },
                onBack = { }

            )
        }
    }
}
