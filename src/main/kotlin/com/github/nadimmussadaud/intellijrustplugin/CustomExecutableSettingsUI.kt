package com.github.nadimmussadaud.intellijrustplugin

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.ComponentWithBrowseButton
import com.intellij.openapi.ui.TextComponentAccessor
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JTextField


class CustomExecutableSettingsUI : SettingsEditor<CustomExecutableRunConfig>() {
    private val executableTypesDropdown = ComboBox(ExecutableType.entries.toTypedArray())
    private val customExecutableField = TextFieldWithBrowseButton()
    private val argumentsField = JBTextField()

    private val panel : JPanel

    init {
        val descriptor =
            FileChooserDescriptorFactory.createSingleFolderDescriptor()
        descriptor.title = "Select executable..."
        descriptor.description = "Choose path to your custom executable."

        customExecutableField.addActionListener(
            ComponentWithBrowseButton.BrowseFolderActionListener<JTextField>(
                customExecutableField,
                null, //Project context not necessary for the task
                descriptor,
                TextComponentAccessor.TEXT_FIELD_WHOLE_TEXT
            )
        )

        panel = FormBuilder.createFormBuilder()
            .addLabeledComponent(
                JBLabel("Executable type:"),
                executableTypesDropdown,
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

    override fun resetEditorFrom(config: CustomExecutableRunConfig) {
        executableTypesDropdown.selectedItem = config.executableType
        argumentsField.text = config.arguments
        customExecutableField.text = config.customExecutablePath!!
    }

    override fun applyEditorTo(config: CustomExecutableRunConfig) {
        config.executableType = executableTypesDropdown.selectedItem as ExecutableType
        config.arguments = argumentsField.text
        config.customExecutablePath = customExecutableField.text
    }

    override fun createEditor(): JComponent {
        return panel
    }
}