/*
 * Copyright (C) 2020 Beijing Yishu Technology Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.growingio.android.sdk.collection;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.growingio.android.sdk.autotrack.GrowingAutotracker;
import com.growingio.android.sdk.autotrack.hybrid.event.HybridPageEvent;
import com.growingio.android.sdk.interfaces.IGrowingIO;
import com.growingio.android.sdk.track.TrackMainThread;
import com.growingio.android.sdk.track.providers.DeviceInfoProvider;
import com.growingio.android.sdk.track.utils.JsonUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @deprecated {@link GrowingAutotracker )}
 */
@Deprecated
public class GrowingIO implements IGrowingIO {
    private static final String TAG = "GrowingIO";

    private static class SingleInstance {
        private static final IGrowingIO INSTANCE = new GrowingIO();
    }

    private GrowingIO() {

    }

    /**
     * @deprecated {@link GrowingAutotracker#get()}
     */
    @Deprecated
    public static IGrowingIO getInstance() {
        return SingleInstance.INSTANCE;
    }


    @Override
    public IGrowingIO setUserAttributes(Map<String, ?> attributes) {
        HashMap<String, String> stringHashMap = new HashMap<>();
        for (String key : attributes.keySet()) {
            stringHashMap.put(key, String.valueOf(attributes.get(key)));
        }
        GrowingAutotracker.get().setLoginUserAttributes(stringHashMap);
        return this;
    }

    @Override
    public IGrowingIO setUserAttributes(JSONObject jsonObject) {
        GrowingAutotracker.get().setLoginUserAttributes(JsonUtil.copyToMap(jsonObject));
        return this;
    }

    @Override
    public IGrowingIO disableDataCollect() {
        GrowingAutotracker.get().setDataCollectionEnabled(false);
        return this;
    }

    @Override
    public IGrowingIO enableDataCollect() {
        GrowingAutotracker.get().setDataCollectionEnabled(true);
        return this;
    }

    @Override
    public String getDeviceId() {
        return DeviceInfoProvider.get().getDeviceId();
    }

    @Override
    public String getVisitUserId() {
        return getDeviceId();
    }

    @Override
    public IGrowingIO setGeoLocation(double latitude, double longitude) {
        GrowingAutotracker.get().setLocation(latitude, longitude);
        return this;
    }

    @Override
    public IGrowingIO clearGeoLocation() {
        GrowingAutotracker.get().cleanLocation();
        return this;
    }

    @Override
    public IGrowingIO setUserId(String userId) {
        GrowingAutotracker.get().setLoginUserId(userId);
        return this;
    }

    @Override
    public IGrowingIO clearUserId() {
        GrowingAutotracker.get().cleanLoginUserId();
        return this;
    }

    @Override
    public IGrowingIO onNewIntent(Activity activity, Intent intent) {
        return this;
    }

    @Override
    public IGrowingIO track(String eventName) {
        GrowingAutotracker.get().trackCustomEvent(eventName);
        return this;
    }

    @Override
    public IGrowingIO track(String eventName, JSONObject var) {
        GrowingAutotracker.get().trackCustomEvent(eventName, JsonUtil.copyToMap(var));
        return this;
    }

    @Override
    public IGrowingIO track(String eventName, JSONObject var, String itemId, String itemKey) {
        GrowingAutotracker.get().trackCustomEvent(eventName, JsonUtil.copyToMap(var), itemKey, itemId);
        return this;
    }

    @Override
    public IGrowingIO trackPage(String pageName) {
        return trackPage(pageName, null);
    }

    @Override
    public IGrowingIO trackPage(String pageName, JSONObject var) {
        if (TextUtils.isEmpty(pageName)) {
            Log.e(TAG, "trackPage: pageName is NULL");
            return this;
        }

        if (!pageName.startsWith("/")) {
            pageName = "/" + pageName;
        }
        StringBuilder query = new StringBuilder();
        if (var != null) {
            for (Iterator<String> iterator = var.keys(); iterator.hasNext();) {
                String key = iterator.next();
                Object value = var.opt(key);
                query.append(key).append("=").append(value).append("&");
            }
            query.deleteCharAt(query.length() - 1);
        }

        TrackMainThread.trackMain().postEventToTrackMain(
                new HybridPageEvent.Builder()
                        .setPageName(pageName)
                        .setTitle("")
                        .setQuery(query.toString())
                        .setTimestamp(System.currentTimeMillis()));
        return this;
    }

    @Override
    public IGrowingIO setImeiEnable(boolean imeiEnable) {
        return this;
    }

    @Override
    public IGrowingIO setAndroidIdEnable(boolean androidIdEnable) {
        return this;
    }

    @Override
    public IGrowingIO bridgeForWebView(Object webView) {
        return this;
    }

    @Override
    public IGrowingIO bridgeForX5WebView(Object x5WebView) {
        return this;
    }
}
