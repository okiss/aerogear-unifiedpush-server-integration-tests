/**
 * JBoss, Home of Professional Open Source
 * Copyright Red Hat, Inc., and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.aerogear.unifiedpush.test;

import java.io.File;

import com.notnoop.apns.*;
import org.jboss.aerogear.unifiedpush.utils.AndroidVariantUtils;
import org.jboss.aerogear.unifiedpush.utils.AuthenticationUtils;
import org.jboss.aerogear.unifiedpush.utils.Constants;
import org.jboss.aerogear.unifiedpush.utils.InstallationUtils;
import org.jboss.aerogear.unifiedpush.utils.PushApplicationUtils;
import org.jboss.aerogear.unifiedpush.utils.PushNotificationSenderUtils;
import org.jboss.aerogear.unifiedpush.utils.ServerSocketUtils;
import org.jboss.aerogear.unifiedpush.utils.SimplePushVariantUtils;
import org.jboss.aerogear.unifiedpush.utils.iOSVariantUtils;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.archive.importer.MavenImporter;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.notnoop.apns.internal.ApnsServiceImpl;
import com.notnoop.exceptions.NetworkIOException;

public final class Deployments {

    private Deployments() {
    }

    public static WebArchive unifiedPushServer() {

        final String unifiedPushServerPom = System.getProperty("unified.push.server.location",
                "aerogear-unifiedpush-server/pom.xml");

        WebArchive war = ShrinkWrap.create(MavenImporter.class).loadPomFromFile(unifiedPushServerPom).importBuildOutput()
                .as(WebArchive.class);

        // replace original persistence.xml with testing one
        war.delete("/WEB-INF/classes/META-INF/persistence.xml");
        // testing persistence
        war.addAsResource("META-INF/persistence.xml");
        return war;
    }

    public static WebArchive unifiedPushServerWithClasses(Class<?>... clazz) {

        final String unifiedPushServerPom = System.getProperty("unified.push.server.location",
                "aerogear-unifiedpush-server/pom.xml");

        WebArchive war = ShrinkWrap.create(MavenImporter.class).loadPomFromFile(unifiedPushServerPom).importBuildOutput()
                .as(WebArchive.class);

        war.delete("/WEB-INF/classes/META-INF/persistence.xml");
        war.addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml");

        war.addClasses(clazz);

        File[] asm = Maven.resolver().resolve("org.ow2.asm:asm:4.1").withoutTransitivity().asFile();
        war = war.addAsLibraries(asm);

        File[] libs = Maven.resolver().loadPomFromFile("pom.xml").resolve("org.mockito:mockito-core").withTransitivity()
                .asFile();
        war = war.addAsLibraries(libs);

        return war;
    }

    public static WebArchive customUnifiedPushServerWithClasses(Class<?>... clazz) {
        final String unifiedPushServerPom = System.getProperty("unified.push.server.location",
                "aerogear-unifiedpush-server/pom.xml");

        WebArchive war = ShrinkWrap.create(MavenImporter.class).loadPomFromFile(unifiedPushServerPom).importBuildOutput()
                .as(WebArchive.class);

        war.delete("/WEB-INF/lib/gcm-server-1.0.2.jar");

        war.delete("/WEB-INF/classes/META-INF/persistence.xml");
        war.addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml");

        war.addClasses(AuthenticationUtils.class, PushApplicationUtils.class, AndroidVariantUtils.class,
                SimplePushVariantUtils.class, InstallationUtils.class, iOSVariantUtils.class, PushNotificationSenderUtils.class);

        war.addClasses(clazz);

        JavaArchive jar = ShrinkWrap.create(JavaArchive.class, "gcm-server-1.0.2.jar").addClasses(Result.class, Message.class,
                MulticastResult.class, Message.class, Sender.class);
        war.addAsLibraries(jar);


        war.delete("/WEB-INF/lib/apns-0.2.3.jar");

        JavaArchive apnsJar = ShrinkWrap.create(JavaArchive.class, "apns-0.2.3.jar").addClasses(NetworkIOException.class,
                ApnsService.class, ApnsServiceImpl.class, ApnsServiceBuilder.class, PayloadBuilder.class, APNS.class,
                Constants.class, ServerSocketUtils.class, ApnsNotification.class, EnhancedApnsNotification.class);
        war.addAsLibraries(apnsJar);

        File[] libs = Maven
                .resolver()
                .loadPomFromFile("pom.xml")
                .resolve("com.jayway.restassured:rest-assured", "org.mockito:mockito-core",
                        "com.jayway.awaitility:awaitility").withTransitivity().asFile();
        war = war.addAsLibraries(libs);

        return war;
    }
}
