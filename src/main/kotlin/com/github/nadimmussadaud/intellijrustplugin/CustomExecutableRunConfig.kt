package com.github.nadimmussadaud.intellijrustplugin

import com.intellij.execution.Executor
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NlsSafe
import javax.swing.Icon

class CustomExecutableRunConfig : RunConfiguration {
    override fun getFactory(): ConfigurationFactory? {
        TODO("Not yet implemented")
    }

    override fun setName(p0: @NlsSafe String?) {
        TODO("Not yet implemented")
    }

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration?> {
        TODO("Not yet implemented")
    }

    override fun getProject(): Project? {
        TODO("Not yet implemented")
    }

    override fun clone(): RunConfiguration {
        TODO("Not yet implemented")
    }

    override fun getState(
        p0: Executor,
        p1: ExecutionEnvironment
    ): RunProfileState? {
        TODO("Not yet implemented")
    }

    override fun getName(): @NlsSafe String {
        TODO("Not yet implemented")
    }

    override fun getIcon(): Icon? {
        TODO("Not yet implemented")
    }
}