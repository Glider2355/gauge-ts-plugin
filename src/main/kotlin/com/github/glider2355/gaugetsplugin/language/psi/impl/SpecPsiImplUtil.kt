package com.github.glider2355.gaugetsplugin.language.psi.impl

import com.github.glider2355.gaugetsplugin.language.psi.SpecElementFactory
import com.github.glider2355.gaugetsplugin.language.psi.SpecProperty
import com.github.glider2355.gaugetsplugin.language.psi.SpecTypes
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElement
import javax.swing.Icon

object SpecPsiImplUtil {

    @JvmStatic
    fun getKey(element: SpecProperty): String? {
        val keyNode = element.node.findChildByType(SpecTypes.KEY)
        return keyNode?.text?.replace("\\\\ ".toRegex(), " ")
    }

    @JvmStatic
    fun getValue(element: SpecProperty): String? {
        val valueNode = element.node.findChildByType(SpecTypes.VALUE)
        return valueNode?.text
    }

    @JvmStatic
    fun getName(element: SpecProperty): String? {
        return getKey(element)
    }

    @JvmStatic
    fun setName(element: SpecProperty, newName: String): PsiElement {
        val keyNode = element.node.findChildByType(SpecTypes.KEY)
        if (keyNode != null) {
            val property = SpecElementFactory.createProperty(element.project, newName)
            val newKeyNode = property.firstChild.node
            element.node.replaceChild(keyNode, newKeyNode)
        }
        return element
    }

    @JvmStatic
    fun getNameIdentifier(element: SpecProperty): PsiElement? {
        val keyNode = element.node.findChildByType(SpecTypes.KEY)
        return keyNode?.psi
    }

    @JvmStatic
    fun getPresentation(element: SpecProperty): ItemPresentation {
        return object : ItemPresentation {
            override fun getPresentableText(): String? {
                return element.getKey()
            }

            override fun getLocationString(): String? {
                val containingFile = element.containingFile
                return containingFile?.name
            }

            override fun getIcon(unused: Boolean): Icon? {
                return element.getIcon(0)
            }
        }
    }
}