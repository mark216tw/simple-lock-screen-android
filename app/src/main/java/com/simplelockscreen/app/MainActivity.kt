package com.simplelockscreen.app

import android.app.Activity
import android.app.AlertDialog
import android.app.StatusBarManager
import android.content.ComponentName
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.WindowInsets
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {
    private lateinit var statusIcon: ImageView
    private lateinit var statusTitle: TextView
    private lateinit var statusDescription: TextView
    private lateinit var permissionButton: Button
    private lateinit var testButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        applySystemBarInsets(findViewById(R.id.root))

        statusIcon = findViewById(R.id.statusIcon)
        statusTitle = findViewById(R.id.statusTitle)
        statusDescription = findViewById(R.id.statusDescription)
        permissionButton = findViewById(R.id.permissionButton)
        testButton = findViewById(R.id.testButton)

        permissionButton.setOnClickListener { openAccessibilitySettings() }
        testButton.setOnClickListener { testLockScreen() }
        findViewById<Button>(R.id.tileButton).setOnClickListener { addQuickSettingsTile() }
        findViewById<TextView>(R.id.privacyButton).setOnClickListener { showPrivacyNotice() }

        if (intent.getBooleanExtra(EXTRA_SERVICE_UNAVAILABLE, false)) {
            Toast.makeText(this, R.string.service_unavailable, Toast.LENGTH_LONG).show()
            intent.removeExtra(EXTRA_SERVICE_UNAVAILABLE)
        }
    }

    private fun applySystemBarInsets(root: View) {
        root.setOnApplyWindowInsetsListener { view, insets ->
            val top: Int
            val bottom: Int
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val systemBars = insets.getInsets(WindowInsets.Type.systemBars())
                top = systemBars.top
                bottom = systemBars.bottom
            } else {
                @Suppress("DEPRECATION")
                top = insets.systemWindowInsetTop
                @Suppress("DEPRECATION")
                bottom = insets.systemWindowInsetBottom
            }
            view.setPadding(0, top, 0, bottom)
            insets
        }
    }

    override fun onResume() {
        super.onResume()
        updateStatus()
    }

    private fun updateStatus() {
        val enabled = AccessibilityStatus.isEnabled(this)
        statusIcon.setImageResource(if (enabled) R.drawable.ic_check else R.drawable.ic_warning)
        statusTitle.setText(if (enabled) R.string.status_enabled else R.string.status_disabled)
        statusDescription.setText(
            if (enabled) R.string.status_enabled_description else R.string.status_disabled_description
        )
        permissionButton.setText(
            if (enabled) R.string.manage_permission else R.string.enable_permission
        )
        testButton.isEnabled = enabled
        testButton.alpha = if (enabled) 1f else 0.45f
    }

    private fun testLockScreen() {
        if (!LockAccessibilityService.lockScreen()) {
            Toast.makeText(this, R.string.service_unavailable, Toast.LENGTH_LONG).show()
            updateStatus()
        }
    }

    private fun openAccessibilitySettings() {
        startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
    }

    private fun addQuickSettingsTile() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val statusBarManager = getSystemService(StatusBarManager::class.java)
            statusBarManager.requestAddTileService(
                ComponentName(this, LockTileService::class.java),
                getString(R.string.tile_label),
                Icon.createWithResource(this, R.drawable.ic_lock),
                mainExecutor
            ) { result ->
                val message = if (
                    result == StatusBarManager.TILE_ADD_REQUEST_RESULT_TILE_ADDED ||
                    result == StatusBarManager.TILE_ADD_REQUEST_RESULT_TILE_ALREADY_ADDED
                ) {
                    R.string.tile_added
                } else {
                    R.string.tile_not_added
                }
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        } else {
            AlertDialog.Builder(this)
                .setTitle(R.string.tile_dialog_title)
                .setMessage(R.string.tile_dialog_message)
                .setPositiveButton(android.R.string.ok, null)
                .show()
        }
    }

    private fun showPrivacyNotice() {
        AlertDialog.Builder(this)
            .setTitle(R.string.privacy_title)
            .setMessage(R.string.privacy_message)
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }

    companion object {
        const val EXTRA_SERVICE_UNAVAILABLE = "service_unavailable"
    }
}
