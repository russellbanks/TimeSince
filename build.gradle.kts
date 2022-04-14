plugins {
    kotlin("multiplatform") version "1.6.20"
}

group = "com.russellbanks"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
}

val os = org.gradle.internal.os.OperatingSystem.current()!!

kotlin {
    when {
        os.isWindows -> mingwX64("libui")
        os.isMacOsX -> macosX64("libui")
        os.isLinux -> linuxX64("libui")
        else -> throw Error("Unknown host")
    }.binaries.executable {
        if (os.isWindows) {
            windowsResources("hello.rc")
            linkerOpts("-mwindows")
        }
    }
    val libuiMain by sourceSets.getting {
        dependencies {
            implementation("com.github.msink:libui:0.1.8")

            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.3.2")
        }
    }
}

fun org.jetbrains.kotlin.gradle.plugin.mpp.Executable.windowsResources(rcFileName: String) {
    val taskName = linkTaskName.replaceFirst("link", "windres")
    val inFile = File(rcFileName)
    val outFile = buildDir.resolve("processedResources/$taskName.res")

    val windresTask = tasks.create<Exec>(taskName) {
        val mingwRoot = File(System.getenv("MSYS2_ROOT") ?: "C:/msys64/")
        val mingwBin = when (target.konanTarget.architecture.bitness) {
            32 -> mingwRoot.resolve("mingw32/bin")
            64 -> mingwRoot.resolve("mingw64/bin")
            else -> error("Unsupported architecture")
        }

        inputs.file(inFile)
        outputs.file(outFile)
        commandLine("$mingwBin/windres", inFile, "-D_${buildType.name}", "-O", "coff", "-o", outFile)
        environment("PATH", "$mingwBin;${System.getenv("PATH")}")

        dependsOn(compilation.compileKotlinTask)
    }

    linkTask.dependsOn(windresTask)
    linkerOpts(outFile.toString())
}
