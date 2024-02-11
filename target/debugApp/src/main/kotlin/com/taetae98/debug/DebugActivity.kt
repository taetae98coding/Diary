package com.taetae98.debug

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.lifecycle.lifecycleScope
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

internal class DebugActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Column {
                    TextButton(onClick = ::migration) {
                        Text(text = "데이터 마이그레이션")
                    }
                }
            }
        }
    }

    private fun migration() {
        lifecycleScope.launch {
            Firebase.firestore
                .collection("memo")
                .get()
                .addOnFailureListener { it.printStackTrace() }
                .await()
                .forEach {
                    migrationMemo(it)
                    continueMigration(it)
                }

            Toast.makeText(this@DebugActivity, "Finish", Toast.LENGTH_SHORT).show()
        }
    }

    private suspend fun migrationMemo(snapshot: DocumentSnapshot) {
        val id = snapshot.get("id") as String
        val state = snapshot.get("state") as? Long ?: return

        Firebase.firestore.collection("memo")
            .document(id)
            .update(
                mapOf(
                    "isDeleted" to (state == 2L),
                    "isFinished" to (state == 1L),
                    "state" to FieldValue.delete(),
                )
            )
            .addOnFailureListener { it.printStackTrace() }
            .await()
    }

    private suspend fun continueMigration(snapshot: DocumentSnapshot) {
        val id = snapshot.get("id") as String

        Firebase.firestore.collection("memo")
            .document(id)
            .update(mapOf("state" to FieldValue.delete()))
            .addOnFailureListener { it.printStackTrace() }
            .await()
    }
}