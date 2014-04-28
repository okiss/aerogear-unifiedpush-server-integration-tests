/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.aerogear.test.api.sender;

import com.google.android.gcm.server.Message;

import java.util.List;

public class SenderStatistics {
    public static final String TOKEN_INVALIDATION_PREFIX = String.format("%08X-", 0xcafedead).toLowerCase();

    public List<String> deviceTokens;
    public Message gcmMessage;
    public String apnsAlert;
    public int apnsBadge;
    public String apnsSound;
    public String apnsCustomFields;
    public String gcmForChromeAlert;
}