package com.github.nadimmussadaud.intellijrustplugin

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.icons.AllIcons
import org.jetbrains.annotations.Nls
import org.jetbrains.annotations.NonNls
import javax.swing.Icon

class CustomExecutableConfigurationType : ConfigurationType {
    override fun getDisplayName(): @Nls(capitalization = Nls.Capitalization.Title) String {
        return "Custom Executable"
    }

    override fun getConfigurationTypeDescription(): @Nls(capitalization = Nls.Capitalization.Sentence) String {
        return "RUST,CARGO & OTHER Custom Executables"
    }

    override fun getIcon(): Icon {
        return AllIcons.Actions.Execute
    }

    override fun getId(): @NonNls String {
        return "CUSTOM_EXECUTABLE";
    }

    override fun getConfigurationFactories(): Array<ConfigurationFactory> {
        return arrayOf(CustomExecutableConfigFactory(this))
    }
}