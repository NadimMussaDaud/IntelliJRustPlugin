package com.github.nadimmussadaud.intellijrustplugin

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.ComponentWithBrowseButton
import com.intellij.openapi.ui.TextComponentAccessor
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.ui.util.preferredWidth
import com.intellij.util.ui.FormBuilder
import com.intellij.util.ui.JBUI
import java.awt.event.ItemEvent
import javax.swing.DefaultComboBoxModel
import javax.swing.JComponent
import javax.swing.JPanel


class CustomExecutableSettingsUI(project : Project) : SettingsEditor<CustomExecutableRunConfig>() {
    private val executableTypesDropdown = ComboBox<String>()
    private val customExecutableField = TextFieldWithBrowseButton()
    private val argumentsField = JBTextField()

    private val panel : JPanel

    init {
        val descriptor =
            FileChooserDescriptorFactory.createSingleFileDescriptor()
        descriptor.title = "Select Executable File"
        descriptor.description = "Choose path to your custom executable."

        customExecutableField.addActionListener(
            ComponentWithBrowseButton.BrowseFolderActionListener(
                customExecutableField,
                null, //Project context not necessary for the task
                descriptor,
                TextComponentAccessor.TEXT_FIELD_WHOLE_TEXT
            )
        )

        customExecutableField.addActionListener {
            fireEditorStateChanged()
        }

        executableTypesDropdown.addItemListener { e ->
            if(e.stateChange == ItemEvent.SELECTED) {
                val isCustom = executableTypesDropdown.selectedItem.toString() != "Custom Executable"
                customExecutableField.isEnabled = isCustom
                customExecutableField.isEditable = isCustom
                fireEditorStateChanged()
            }
        }

        executableTypesDropdown.isEditable = false
        executableTypesDropdown.setPrototypeDisplayValue("rustc — /home/user/.rustup/toolchains/stable-.../bin/rustc")

        // Set minimal width/columns for fields going with IntelliJ Guidelines
        executableTypesDropdown.preferredWidth = JBUI.scale(480)

        // Setting accessibility for fields
        executableTypesDropdown.accessibleContext.accessibleName = "Executable type"
        customExecutableField.accessibleContext.accessibleName = "Executable file"
        argumentsField.accessibleContext.accessibleName = "Arguments"

        // Tips for filling the camps
        executableTypesDropdown.toolTipText = "Choose rustc/cargo from PATH"
        customExecutableField.toolTipText = "Executable file to run"
        argumentsField.toolTipText = "Command-line arguments"

        // Temporary placement for when looking for
        val loadingModel = DefaultComboBoxModel(arrayOf("Looking for rustc/cargo..."))
        executableTypesDropdown.model = loadingModel

        Helper().discoverRustToolchains(project) { choices ->
            executableTypesDropdown.model = DefaultComboBoxModel(choices.map { it.toString() }.toTypedArray())
            fireEditorStateChanged()
        }

        // Setting labels for accessibility
        val execTypeLabel = JBLabel("Executable type:")
        execTypeLabel.labelFor = executableTypesDropdown
        val execFileLabel = JBLabel("Executable file:")
        execFileLabel.labelFor = customExecutableField
        val argsLabel = JBLabel("Arguments:")
        argsLabel.labelFor = argumentsField

        panel = FormBuilder.createFormBuilder()
            .addLabeledComponent(
                execTypeLabel,
                executableTypesDropdown
            )
            .addLabeledComponent(
                execFileLabel,
                customExecutableField
            )
            .addLabeledComponent(
                argsLabel,
                argumentsField
            )
            .panel
    }

    override fun resetEditorFrom(config: CustomExecutableRunConfig) {
        executableTypesDropdown.selectedItem = config.executableType
        argumentsField.text = config.arguments
        customExecutableField.text = config.customExecutablePath ?: ""
    }

    override fun applyEditorTo(config: CustomExecutableRunConfig) {
        config.executableType = executableTypesDropdown.selectedItem.toString()
        config.arguments = argumentsField.text
        config.customExecutablePath = customExecutableField.text
    }

    override fun createEditor(): JComponent {
        installWatcher(panel)
        return panel
    }

}