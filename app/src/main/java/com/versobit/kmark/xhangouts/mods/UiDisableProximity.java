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

package com.versobit.kmark.xhangouts.mods;

import android.hardware.Sensor;
import android.hardware.SensorManager;

import com.versobit.kmark.xhangouts.Config;
import com.versobit.kmark.xhangouts.Module;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.IXUnhook;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public final class UiDisableProximity extends Module {

    private static final String ANDROID_HARDWARE_SENSORMANAGER_DEFAULT = "getDefaultSensor";

    public UiDisableProximity(Config config) {
        super(UiDisableProximity.class.getSimpleName(), config);
    }

    @Override
    public IXUnhook[] hook(ClassLoader loader) {
        return new IXUnhook[] {
                findAndHookMethod(SensorManager.class, ANDROID_HARDWARE_SENSORMANAGER_DEFAULT,
                        int.class, getDefaultSensor)
        };
    }

    private final XC_MethodHook getDefaultSensor = new XC_MethodHook() {
        @Override
        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            if(!config.modEnabled) {
                return;
            }

            debug(String.valueOf(config.disableProximity));

            if(!config.disableProximity) {
                return;
            }

            if((int)param.args[0] == Sensor.TYPE_PROXIMITY) {
                param.setResult(null);
            }
        }
    };

}
