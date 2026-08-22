package com.simplelockscreen.app

import android.app.Activity
import android.content.Intent
import android.os.Bundle

class LockActivity : Activity() {
    private var handled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleLaunch()
    }

    private fun handleLaunch() {
        if (handled) return
        handled = true

        if (!AccessibilityStatus.isEnabled(this)) {
            openSettings()
            return
        }

        if (LockAccessibilityService.lockScreen()) {
            finish()
            return
        }

        // The system may still be reconnecting the service just after boot.
        window.decorView.postDelayed({
            if (LockAccessibilityService.lockScreen()) {
                finish()
            } else {
                openSettings(MainActivity.EXTRA_SERVICE_UNAVAILABLE)
            }
        }, SERVICE_RETRY_DELAY_MS)
    }

    private fun openSettings(extra: String? = null) {
        val intent = Intent(this, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        extra?.let { intent.putExtra(it, true) }
        startActivity(intent)
        finish()
    }

    companion object {
        private const val SERVICE_RETRY_DELAY_MS = 300L
    }
}
