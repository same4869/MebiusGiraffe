package com.pokemon.mebius.giraffe.config

class GiraffeConfig(
    var enable: Boolean = true,
    var enableLog: Boolean = true,
    @Transient var uiConfig: GiraffeUiConfig = GiraffeUiConfig(),
    var storageConfig: GiraffeStorageConfig = GiraffeStorageConfig(),
)