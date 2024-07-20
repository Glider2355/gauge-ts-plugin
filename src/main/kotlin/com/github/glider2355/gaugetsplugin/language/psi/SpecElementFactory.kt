package com.github.glider2355.gaugetsplugin.language.psi

import com.github.glider2355.gaugetsplugin.language.SpecFileType
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileFactory

object SpecElementFactory {

    @JvmStatic
    fun createProperty(project: Project, name: String): SpecProperty {
        val file = createFile(project, name)
        return file.firstChild as SpecProperty
    }

    @JvmStatic
    fun createFile(project: Project, text: String): SpecFile {
        val name = "dummy.spec"
        return PsiFileFactory.getInstance(project).createFileFromText(name, SpecFileType, text) as SpecFile
    }

    @JvmStatic
    fun createProperty(project: Project, name: String, value: String): SpecProperty {
        val file = createFile(project, "$name = $value")
        return file.firstChild as SpecProperty
    }

    @JvmStatic
    fun createCRLF(project: Project): PsiElement {
        val file = createFile(project, "\n")
        return file.firstChild
    }
}