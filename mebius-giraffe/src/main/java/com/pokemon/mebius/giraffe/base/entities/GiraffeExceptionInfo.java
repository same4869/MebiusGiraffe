package com.pokemon.mebius.giraffe.base.entities;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.jetbrains.annotations.NotNull;

@Entity
public class GiraffeExceptionInfo implements GiraffeInfoProtocol{
    @Id(autoincrement = true)
    public Long id;
    public String crashTraceStr;
    public String simpleMessage;
    public String threadName;
    public String exceptionName;
    public Long time;
    public String pageName;

    @Generated(hash = 1830249241)
    public GiraffeExceptionInfo(Long id, String crashTraceStr, String simpleMessage, String threadName,
            String exceptionName, Long time, String pageName) {
        this.id = id;
        this.crashTraceStr = crashTraceStr;
        this.simpleMessage = simpleMessage;
        this.threadName = threadName;
        this.exceptionName = exceptionName;
        this.time = time;
        this.pageName = pageName;
    }

    @Generated(hash = 222474797)
    public GiraffeExceptionInfo() {
    }

    @Override
    public Long getTime() {
        return this.time;
    }

    @Override
    public String getPageName() {
        return this.pageName;
    }

    @NotNull
    public boolean isvalid() {
        return !crashTraceStr.isEmpty();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCrashTraceStr() {
        return this.crashTraceStr;
    }

    public void setCrashTraceStr(String crashTraceStr) {
        this.crashTraceStr = crashTraceStr;
    }

    public String getSimpleMessage() {
        return this.simpleMessage;
    }

    public void setSimpleMessage(String simpleMessage) {
        this.simpleMessage = simpleMessage;
    }

    public String getThreadName() {
        return this.threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public String getExceptionName() {
        return this.exceptionName;
    }

    public void setExceptionName(String exceptionName) {
        this.exceptionName = exceptionName;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }
}
