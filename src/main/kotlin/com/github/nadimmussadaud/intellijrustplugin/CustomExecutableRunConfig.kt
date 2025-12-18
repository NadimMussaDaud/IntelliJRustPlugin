package com.github.nadimmussadaud.intellijrustplugin

import com.intellij.execution.Executor
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.configurations.RunConfigurationBase
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.configurations.RuntimeConfigurationException
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import java.io.File

enum class ExecutableType {
    RUST(),
    CARGO(),
    CUSTOM()
}

class CustomExecutableRunConfig(
    project: Project,
    factory: ConfigurationFactory,
    name: String
) : RunConfigurationBase<CustomExecutableRunConfig>(project, factory, name) {

    var executableType: ExecutableType = ExecutableType.RUST
    var customExecutablePath: String? = null
    var arguments: String? = null

    override fun checkConfiguration() {
        when (executableType) {
            ExecutableType.RUST -> {
                if(!hasExecutableInPath("rustc"))
                    throw RuntimeConfigurationException("Rust cannot be specified for this executable")
            }
            ExecutableType.CARGO -> {
                if(!hasExecutableInPath("cargo"))
                    throw RuntimeConfigurationException("Cargo cannot be specified for this executable")
            }
            ExecutableType.CUSTOM -> {
                if(customExecutablePath == null || customExecutablePath!!.isBlank()) {
                    throw RuntimeConfigurationException("Please specify a custom executable path")
                }
                val file = File(customExecutablePath!!)
                if(!file.exists()) {
                    throw RuntimeConfigurationException("File specified as custom executable doesn't exist")
                }
                if(!file.canExecute()) {
                    throw RuntimeConfigurationException("File specified as custom executable is not executable.")
                }
            }
        }
    }

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration?> {
        return CustomExecutableSettingsUI()
    }

    override fun getState(
        p0: Executor,
        executionEnv: ExecutionEnvironment
    ): RunProfileState {
        return CustomExecutableRunState(executionEnv, this);
    }

    private fun hasExecutableInPath(compiler: String): Boolean {
        return when (System.getProperty("os.name").lowercase()) {
            "windows" -> Runtime.getRuntime().exec(arrayOf("where", compiler)).waitFor() == 0
            "macos" -> Runtime.getRuntime().exec(arrayOf("which", compiler)).waitFor() == 0
            else -> false
        }
    }
}