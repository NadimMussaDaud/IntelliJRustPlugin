package com.github.nadimmussadaud.intellijrustplugin

import com.intellij.openapi.progress.Task
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.application.ApplicationManager
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class Helper {
    data class SdkChoice(val id: String, val label: String, val path: String? = null) {
        override fun toString() = label // ComboBox shows label
    }

    fun discoverRustToolchains(project: com.intellij.openapi.project.Project?, onDone: (List<SdkChoice>) -> Unit) {
        object : Task.Backgroundable(project, "Discovering Rust toolchains", false) {
            private val out = mutableListOf<SdkChoice>()

            override fun run(indicator: ProgressIndicator) {
                val home = System.getProperty("user.home") ?: ""
                val pathEnv = System.getenv("PATH") ?: ""
                val sep = File.pathSeparator
                val isWindows = System.getProperty("os.name").startsWith("Windows", true)
                val exts = if (isWindows) listOf(".exe", ".bat", ".cmd") else listOf("")

                fun checkAndAdd(labelPrefix: String, p: java.nio.file.Path) {
                    if (Files.isRegularFile(p) && Files.isExecutable(p)) {
                        val pathStr = p.toAbsolutePath().toString()
                        out.add(SdkChoice(pathStr, "$labelPrefix — $pathStr", pathStr))
                    }
                }

                // 2) Find rustc/cargo on PATH directories
                for (dir in pathEnv.split(sep)) {
                    if (dir.isBlank()) continue
                    try {
                        val dirPath = Paths.get(dir)
                        for (ext in exts) {
                            val r = dirPath.resolve("rustc$ext")
                            val c = dirPath.resolve("cargo$ext")
                            if (Files.isRegularFile(r) && Files.isExecutable(r)) checkAndAdd("rustc", r)
                            if (Files.isRegularFile(c) && Files.isExecutable(c)) checkAndAdd("cargo", c)
                        }
                    } catch (_: Exception) { /* ignore invalid PATH entries */ }
                }

                // 3) Look in ~/.cargo/bin and ~/.rustup/toolchains/*/bin
                try {
                    val cargoBin = Paths.get(home, ".cargo", "bin")
                    if (Files.isDirectory(cargoBin)) {
                        for (ext in exts) checkAndAdd("rustc", cargoBin.resolve("rustc$ext"))
                        for (ext in exts) checkAndAdd("cargo", cargoBin.resolve("cargo$ext"))
                    }
                } catch (_: Exception) {}

                try {
                    val rustupRoot = Paths.get(home, ".rustup", "toolchains")
                    if (Files.isDirectory(rustupRoot)) {
                        Files.newDirectoryStream(rustupRoot).use { stream ->
                            for (child in stream) {
                                val bin = child.resolve("bin")
                                if (Files.isDirectory(bin)) {
                                    for (ext in exts) checkAndAdd("rustc", bin.resolve("rustc$ext"))
                                    for (ext in exts) checkAndAdd("cargo", bin.resolve("cargo$ext"))
                                }
                            }
                        }
                    }
                } catch (_: Exception) {}

                // Deduplicate by id/path
                val deduped = out.distinctBy { it.id }

                // return on EDT
                ApplicationManager.getApplication().invokeLater {
                    onDone(deduped)
                }
            }
        }.queue()
    }

}