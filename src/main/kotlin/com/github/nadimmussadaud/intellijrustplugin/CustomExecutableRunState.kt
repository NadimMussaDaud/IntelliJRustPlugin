package com.github.nadimmussadaud.intellijrustplugin

import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.KillableProcessHandler
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.util.execution.ParametersListUtil

class CustomExecutableRunState(
    executor : ExecutionEnvironment,
    private val configuration : CustomExecutableRunConfig
) : CommandLineState(executor) {

    override fun startProcess(): ProcessHandler {
        val params = ParametersListUtil.parse(configuration.arguments ?: "")
        val exePath = resolveExecutable()
        val cmd = GeneralCommandLine(exePath)
            .withWorkDirectory(environment.project.basePath)
        cmd.addParameters(params)

        return KillableProcessHandler(cmd)
    }

    private fun resolveExecutable(): String {
        return if(configuration.executableType == "Custom Executable") {
            configuration.customExecutablePath.toString()
        } else {
            configuration.executableType.split(" ").last()
        }
    }
}