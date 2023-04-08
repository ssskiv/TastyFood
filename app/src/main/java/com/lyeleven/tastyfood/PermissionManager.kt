package com.lyeleven.tastyfood

import android.Manifest
import android.R
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class PermissionManager {
    private val REQUEST_PERMISSION = 1


    public fun showPermission(appCompatActivity: AppCompatActivity, permissionName: String) {
        val permissionCheck = ContextCompat.checkSelfPermission(
            appCompatActivity, permissionName
        )
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    appCompatActivity,
                    permissionName
                )
            ) {
                if (permissionName == Manifest.permission.ACCESS_BACKGROUND_LOCATION) {
                    showExplanation(
                        "Требуется доступ к местоположению в фоне",
                        "Это приложение создано для того, чтобы отправлять местоположение курьера заказчику. Ваше местоположение не будет транслироваться, когда у вас нет заказов.",
                        permissionName,
                        REQUEST_PERMISSION,
                        appCompatActivity
                    )
                } else {
                    showExplanation(
                        "Permission Needed",
                        "Rationale",
                        permissionName,
                        REQUEST_PERMISSION,
                        appCompatActivity
                    )
                }
            } else {
                requestPermission(
                    permissionName,
                    REQUEST_PERMISSION,
                    appCompatActivity
                )
            }
        } else {
            /* Toast.makeText(appCompatActivity, "Permission (already) Granted!", Toast.LENGTH_SHORT)
                 .show()*/
        }
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>?,
        grantResults: IntArray,
        appCompatActivity: AppCompatActivity
    ) {
        when (requestCode) {
            REQUEST_PERMISSION -> if (grantResults.size > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(appCompatActivity, "Permission Granted!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(appCompatActivity, "Permission Denied!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showExplanation(
        title: String,
        message: String,
        permission: String,
        permissionRequestCode: Int,
        appCompatActivity: AppCompatActivity
    ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(appCompatActivity)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton(
                R.string.ok,
                DialogInterface.OnClickListener { dialog, id ->
                    requestPermission(
                        permission,
                        permissionRequestCode,
                        appCompatActivity
                    )
                })
        builder.create().show()
    }

    private fun requestPermission(
        permissionName: String,
        permissionRequestCode: Int,
        appCompatActivity: AppCompatActivity
    ) {
        ActivityCompat.requestPermissions(
            appCompatActivity,
            arrayOf(permissionName),
            permissionRequestCode
        )
    }

    public fun showLocationPermissions(appCompatActivity: AppCompatActivity) {
        showPermission(appCompatActivity, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        showPermission(appCompatActivity, Manifest.permission.ACCESS_FINE_LOCATION)
        showPermission(appCompatActivity, Manifest.permission.ACCESS_COARSE_LOCATION)
        showPermission(appCompatActivity,Manifest.permission.MANAGE_EXTERNAL_STORAGE)
    }
}