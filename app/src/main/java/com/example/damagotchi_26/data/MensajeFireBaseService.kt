package com.example.damagotchi_26.data

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.damagotchi_26.MainActivity
import com.example.damagotchi_26.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MensajeFirebaseService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        guardarTokenEnFirebase(token)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val titulo = message.notification?.title ?: return
        val cuerpo = message.notification?.body ?: return

        mostrarNotificacion(titulo, cuerpo)
    }

    private fun guardarTokenEnFirebase(token: String) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        FirebaseFirestore.getInstance()
            .collection("users")
            .document(uid)
            .update("fcmToken", token)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun mostrarNotificacion(titulo: String, cuerpo: String) {
        val canalId = "comunidad_canal"
        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val canal = NotificationChannel(
            canalId,
            "Comunidad",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Notificaciones de la comunidad"
        }
        notificationManager.createNotificationChannel(canal)

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificacion = NotificationCompat.Builder(this, canalId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(titulo)
            .setContentText(cuerpo)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notificacion)
    }
}