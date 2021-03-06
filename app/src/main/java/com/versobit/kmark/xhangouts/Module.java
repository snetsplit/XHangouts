/*
 * Copyright (C) 2015 Kevin Mark
 *
 * This file is part of XHangouts.
 *
 * XHangouts is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * XHangouts is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with XHangouts.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.versobit.kmark.xhangouts;

import android.app.Application;
import android.content.res.XResources;

import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.IXUnhook;

import static de.robv.android.xposed.XposedHelpers.callStaticMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;

public abstract class Module {

    private static final String HANGOUTS_ES_APPLICATION = "com.google.android.apps.hangouts.phone.EsApplication";
    private static final String HANGOUTS_ES_APPLICATION_GETAPP = "b";

    private final String tag;
    protected final Config config;
    private Class EsApplication = null;

    public Module(String tag, Config config) {
        this.tag = tag;
        this.config = config;
    }

    public void init(IXposedHookZygoteInit.StartupParam startup) { }

    public IXUnhook[] hook(ClassLoader loader) {
        EsApplication = findClass(HANGOUTS_ES_APPLICATION, loader);
        return new IXUnhook[] { };
    }

    public void resources(XResources res) { }

    protected Application getApplication() {
        return (Application)callStaticMethod(EsApplication, HANGOUTS_ES_APPLICATION_GETAPP);
    }

    protected void debug(String msg) {
        debug(msg, true);
    }

    protected void debug(String msg, boolean useTag) {
        if(config.debug) {
            log(msg, useTag);
        }
    }

    protected void debug(Throwable throwable) {
        if(config.debug) {
            log(throwable);
        }
    }

    protected void log(String msg) {
        log(msg, true);
    }

    protected void log(String msg, boolean useTag) {
        XHangouts.log((useTag ? tag + ": " : "") + msg, useTag);
    }

    protected void log(Throwable throwable) {
        XposedBridge.log(throwable);
    }

}
