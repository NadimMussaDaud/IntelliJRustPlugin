package com.github.nadimmussadaud.intellijrustplugin

import com.intellij.execution.ExecutionResult
import com.intellij.execution.configurations.RunConfigurationBase
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.ProgramRunner

class CustomExecutableRunState(executor : ExecutionEnvironment, configuration : RunConfigurationBase<CustomExecutableRunConfig>) : RunProfileState {
    override fun execute(
        p0: com.intellij.execution.Executor?,
        p1: ProgramRunner<*>
    ): ExecutionResult? {
        TODO("Not yet implemented")
    }
}