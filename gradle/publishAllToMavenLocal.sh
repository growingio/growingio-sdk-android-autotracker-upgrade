#!/bin/bash

./gradlew clean \
&& ./gradlew :tracker-upgrade-2to3-cdp:publishToMavenLocal \
&& ./gradlew :autotracker-upgrade-2to3-cdp:publishToMavenLocal
