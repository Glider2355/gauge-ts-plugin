package com.github.glider2355.gaugetsplugin.language

import com.intellij.lang.Language

class SpecLanguage private constructor() : Language("Spec") {
    companion object {
        val INSTANCE = SpecLanguage()
    }
}