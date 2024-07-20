package com.github.glider2355.gaugetsplugin.language.psi

import com.github.glider2355.gaugetsplugin.language.SpecLanguage
import com.github.glider2355.gaugetsplugin.language.SpecFileType
import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider

class SpecFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, SpecLanguage.INSTANCE) {

    override fun getFileType(): FileType {
        return SpecFileType
    }

    override fun toString(): String {
        return "Spec File"
    }
}