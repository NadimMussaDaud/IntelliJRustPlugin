package com.github.nadimmussadaud.intellijrustplugin

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import org.jetbrains.annotations.Nls
import org.jetbrains.annotations.NonNls
import javax.swing.Icon

class CustomExecutableConfigurationType : ConfigurationType {
    override fun getDisplayName(): @Nls(capitalization = Nls.Capitalization.Title) String {
        TODO("Not yet implemented")
    }

    override fun getConfigurationTypeDescription(): @Nls(capitalization = Nls.Capitalization.Sentence) String? {
        TODO("Not yet implemented")
    }

    override fun getIcon(): Icon? {
        TODO("Not yet implemented")
    }

    override fun getId(): @NonNls String {
        TODO("Not yet implemented")
    }

    override fun getConfigurationFactories(): Array<out ConfigurationFactory?>? {
        TODO("Not yet implemented")
    }
}