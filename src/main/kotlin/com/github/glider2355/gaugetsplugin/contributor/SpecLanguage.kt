package com.github.glider2355.gaugetsplugin.contributor

import com.intellij.lang.Language

class SpecLanguage private constructor() : Language("Spec") {
    companion object {
        val INSTANCE = SpecLanguage()
    }
}