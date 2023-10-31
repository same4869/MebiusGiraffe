package com.pokemon.mebius.giraffe.config

import com.pokemon.mebius.giraffe.base.config.GiraffeMonitorConfig

class GiraffeConfig(
    var enable: Boolean = true,
    var enableLog: Boolean = true,
    @Transient var uiConfig: GiraffeUiConfig = GiraffeUiConfig(),
    var storageConfig: GiraffeStorageConfig = GiraffeStorageConfig(),
    var monitorConfig: GiraffeMonitorConfig = GiraffeMonitorConfig()
)