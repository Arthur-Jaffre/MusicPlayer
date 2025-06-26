package fr.arthur.musicplayer.helpers

import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import fr.arthur.musicplayer.R

class StorageDialog(
    private val activity: Activity,
    private val onContinue: () -> Unit
) {
    fun show() {
        val dialogView: View = LayoutInflater.from(activity)
            .inflate(R.layout.dialog_pick_folder_info, null)

        val dialog = AlertDialog.Builder(activity)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialogView.findViewById<View>(R.id.btn_continue).setOnClickListener {
            dialog.dismiss()
            onContinue()
        }

        dialog.show()
    }
}