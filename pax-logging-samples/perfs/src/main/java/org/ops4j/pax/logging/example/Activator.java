/*
 * Copyright 2005 Niclas Hedhman.
 *
 * Licensed  under the  Apache License,  Version 2.0  (the "License");
 * you may not use  this file  except in  compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under the  License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS  OF ANY KIND, either  express  or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ops4j.pax.logging.example;

import java.util.concurrent.CountDownLatch;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator implements BundleActivator {

    @Override
    public void start(BundleContext context) throws Exception {
        System.out.println("Warming up");
        final Logger logger = LoggerFactory.getLogger(getClass());
        for (int i = 0; i < 10000; i++) {
            if (logger.isInfoEnabled()) {
                logger.info("Hello {} !", "world");
            }
        }
        int[] threads = { 1, 2, 4, 8, 16, 32, 64 };
        for (int nbThreads : threads) {
            System.out.println("Running perfs for " + nbThreads + " threads");
            long t0 = System.currentTimeMillis();
            final CountDownLatch latch = new CountDownLatch(nbThreads);
            for (int i = 0; i < nbThreads; i++) {
                new Thread() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 10000; i++) {
                            if (logger.isInfoEnabled()) {
                                logger.info("Hello {} !", "world");
                            }
                        }
                        latch.countDown();
                    }
                }.start();
            }
            latch.await();
            long t1 = System.currentTimeMillis();
            System.out.println("Result: " + (t1 - t0) + " ms");
        }
    }

    @Override
    public void stop(BundleContext context) throws Exception {

    }
}
