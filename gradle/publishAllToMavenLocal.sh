#!/bin/bash

./gradlew clean \
&& ./gradlew :autotracker-upgrade-2to3-cdp:publishToMavenLocal
