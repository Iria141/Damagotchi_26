package com.example.damagotchi_26.navigation

sealed class Route(val path: String) {

    data object CrearAnuncioAdmin : Route("crear_anuncio_admin")
    data object Menu : Route("menu")
    data object Community : Route("community")
    data object SeguimientoScreem : Route("seguimiento")
    data object CreatePost : Route("crear_post")
    data object PostDetail : Route("detalle_post/{postId}")

    data object Login : Route("login")
    data object Register : Route("newUser")
    data object AccountCreated : Route("account_created")
    data object ResetPassword : Route("reset_password")
    data object PasswordResetDone : Route("password_reset_done")
    data object Welcome : Route("welcome")

    data object Rooms : Route("rooms")


    object BathRoom : Route("baño")
    object BedRoom : Route("dormitorio")
    object Clinic : Route("consulta_medica")
    object Kitchen : Route("cocina")
    object LivingRoom : Route("salon")
    object Park : Route("parque")
}