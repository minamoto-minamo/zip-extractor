plugins {
    application
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        // 使いたい言語バージョンに合わせて変更可（8/11/17など）
        languageVersion.set(org.gradle.jvm.toolchain.JavaLanguageVersion.of(8))
    }
}

application {
    mainClass.set("com.kunieda.zipx.App")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.kunieda.zipx.App"
        attributes["Implementation-Title"] = "zip-extractor"
        attributes["Implementation-Version"] = project.version
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
