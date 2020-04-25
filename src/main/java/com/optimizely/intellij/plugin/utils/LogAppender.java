/****************************************************************************
 * Copyright 2020, Optimizely, Inc. and contributors                        *
 *                                                                          *
 * Licensed under the Apache License, Version 2.0 (the "License");          *
 * you may not use this file except in compliance with the License.         *
 * You may obtain a copy of the License at                                  *
 *                                                                          *
 *    http://www.apache.org/licenses/LICENSE-2.0                            *
 *                                                                          *
 * Unless required by applicable law or agreed to in writing, software      *
 * distributed under the License is distributed on an "AS IS" BASIS,        *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. *
 * See the License for the specific language governing permissions and      *
 * limitations under the License.                                           *
 ***************************************************************************/
package com.optimizely.intellij.plugin.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;


public class LogAppender extends AppenderBase<ILoggingEvent> {
    int counter = 0;
    public static AtomicBoolean captureLogging = new AtomicBoolean(false);
    public static List<String> logs = Collections.synchronizedList(new ArrayList<String>());

    PatternLayoutEncoder encoder;

    @Override
    public void start() {
        if (this.encoder == null) {
            addError("No encoder set for the appender named ["+ name +"].");
            return;
        }

        try {
            encoder.init(System.out);
        } catch (IOException e) {
        }
        super.start();
    }

    public static void clearLogs() {
        synchronized (logs) {
            logs.clear();
        }
    }

    public void append(ILoggingEvent event) {
        // output the events as formatted by our layout
        try {
            this.encoder.doEncode(event);
            if (captureLogging.get()) {
                synchronized (logs) {
                    logs.add(event.getFormattedMessage());
                }
            }
        } catch (IOException e) {
        }

        // prepare for next event
        counter++;
    }

    public PatternLayoutEncoder getEncoder() {
        return encoder;
    }

    public void setEncoder(PatternLayoutEncoder encoder) {
        this.encoder = encoder;
    }
}