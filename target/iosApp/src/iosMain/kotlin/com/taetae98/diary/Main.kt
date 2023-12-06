package com.taetae98.diary

import androidx.compose.material3.Text
import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController

public fun compose(): UIViewController {
    return ComposeUIViewController {
        Text(text = "Hello World")
    }
}
