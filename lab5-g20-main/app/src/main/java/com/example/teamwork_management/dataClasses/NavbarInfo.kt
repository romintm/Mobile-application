package com.example.teamwork_management.dataClasses

import androidx.compose.ui.graphics.vector.ImageVector

data class NavbarInfo(
    var title: String,
    var icon: ImageVector?,
    var content: String,
    var bfun: () -> Unit
)
