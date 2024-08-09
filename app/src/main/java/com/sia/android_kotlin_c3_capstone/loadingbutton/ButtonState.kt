package com.sia.android_kotlin_c3_capstone.loadingbutton


sealed class ButtonState {
    object Clicked : ButtonState()
    object Loading : ButtonState()
    object Completed : ButtonState()
}