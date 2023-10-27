package com.pokemon.mebius.giraffe.config

class GiraffeStorageConfig (
    //配置存储的数据， 基于green dao
    @Transient val greenDaoProvider: ArrayList<GiraffeDaoProviderConfig> = ArrayList(),
    //在一次应用打开期间存活的数据
    val storageInOneSessionData: ArrayList<String> = ArrayList(),
    //限制每种类型的数据最多存多少个
    var dataMaxSaveCountLimit: HashMap<String, Int> = HashMap() // 各个类型数据量限制
)