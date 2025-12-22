package com.github.nadimmussadaud.intellijrustplugin

import com.intellij.openapi.progress.Task
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProcessCanceledException
import com.intellij.openapi.project.Project
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class Helper {
    data class ExecChoice(val id: String, val label: String, val path: String? = null) {
        override fun toString() = label
    }

    fun discoverRustToolchains(project: Project?, onDone: (List<ExecChoice>) -> Unit) {
        object : Task.Backgroundable(project, "Discovering Rust toolchains", true) {
            private var out = mutableListOf<ExecChoice>()

            override fun run(indicator: ProgressIndicator) {
                val home = System.getProperty("user.home") ?: ""
                val pathEnv = System.getenv("PATH") ?: ""
                val sep = File.pathSeparator
                val isWindows = System.getProperty("os.name").startsWith("Windows", true)
                val exts = if (isWindows) listOf(".exe", ".bat", ".cmd") else listOf("")

                fun checkAndAdd(labelPrefix: String, p: java.nio.file.Path) {
                    if (Files.isRegularFile(p) && Files.isExecutable(p)) {
                        val pathStr = p.toAbsolutePath().toString()
                        out.add(ExecChoice(pathStr, "$labelPrefix — $pathStr", pathStr))
                    }
                }

                // Find rustc/cargo on PATH directories
                for (dir in pathEnv.split(sep)) {
                    indicator.checkCanceled()
                    if (dir.isBlank()) continue
                    try {
                        val dirPath = Paths.get(dir)
                        for (ext in exts) {
                            indicator.checkCanceled()
                            val r = dirPath.resolve("rustc$ext")
                            val c = dirPath.resolve("cargo$ext")
                            if (Files.isRegularFile(r) && Files.isExecutable(r)) checkAndAdd("RUSTC", r)
                            if (Files.isRegularFile(c) && Files.isExecutable(c)) checkAndAdd("CARGO", c)
                        }
                    } catch ( e : ProcessCanceledException) { throw e }
                }

                // Look in ~/.cargo/bin and ~/.rustup/toolchains/*/bin
                try {
                    val cargoBin = Paths.get(home, ".cargo", "bin")
                    if (Files.isDirectory(cargoBin)) {
                        for (ext in exts) checkAndAdd("RUSTC", cargoBin.resolve("rustc$ext"))
                        for (ext in exts) checkAndAdd("CARGO", cargoBin.resolve("cargo$ext"))
                    }
                } catch (_: Exception) {}

                try {
                    val rustupRoot = Paths.get(home, ".rustup", "toolchains")
                    if (Files.isDirectory(rustupRoot)) {
                        Files.newDirectoryStream(rustupRoot).use { stream ->
                            for (child in stream) {
                                indicator.checkCanceled()
                                val bin = child.resolve("bin")
                                if (Files.isDirectory(bin)) {
                                    for (ext in exts) checkAndAdd("RUSTC", bin.resolve("rustc$ext"))
                                    for (ext in exts) checkAndAdd("CARGO", bin.resolve("cargo$ext"))
                                }
                            }
                        }
                    }
                } catch ( e : ProcessCanceledException) { throw e }

                // Deduplicate by id/path
                out = out.distinctBy { it.id }.toMutableList()

                // Add the option for a custom executable
                out.add(ExecChoice("Custom", "Custom Executable", null))
            }

            override fun onSuccess() {
                onDone(out)
            }

        }.queue()
    }
}