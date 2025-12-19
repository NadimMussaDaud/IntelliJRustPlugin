package com.github.nadimmussadaud.intellijrustplugin

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent
import javax.swing.JPanel

class CustomExecutableSettingsUI : SettingsEditor<CustomExecutableRunConfig>() {
    private val executableTypes = ComboBox(ExecutableType.entries.toTypedArray())
    private val customExecutableField = TextFieldWithBrowseButton()
    private val argumentsField = JBTextField()

    private val panel : JPanel

    init {
        customExecutableField.addBrowseFolderListener(
            "Select Executable",
            "Choose the executable file to run",
            null,
            FileChooserDescriptorFactory.createSingleFileDescriptor()
        )

        panel = FormBuilder.createFormBuilder()
            .addLabeledComponent(
                JBLabel("Executable type:"),
                executableTypes,
                1,
                false
            )
            .addLabeledComponent(
                JBLabel("Custom Executable:"),
                customExecutableField,
                1,
                false
            )
            .addLabeledComponent(
            JBLabel("Arguments:"),
            argumentsField,
            1,
            false
            )
            .panel
    }

    override fun resetEditorFrom(p0: CustomExecutableRunConfig) {
        TODO("Not yet implemented")
    }

    override fun applyEditorTo(p0: CustomExecutableRunConfig) {
        TODO("Not yet implemented")
    }

    override fun createEditor(): JComponent {
        return panel
    }
}