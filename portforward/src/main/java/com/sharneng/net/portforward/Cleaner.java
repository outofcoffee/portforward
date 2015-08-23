/*
 * Copyright 2002-2013 the original author or authors.
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
package com.sharneng.net.portforward;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author Kenneth Xu
 * 
 */
class Cleaner implements Runnable {
    private static final int CLEAN_INTERVAL = 3000;

    private static final Log log = LogFactory.getLog(Cleaner.class);

    private final List<Cleanable> list = new ArrayList<Cleanable>();

    @Override
    public void run() {
        while (true) {
            cleanup();
            try {
                Thread.sleep(CLEAN_INTERVAL);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private synchronized void cleanup() {
        for (Iterator<Cleanable> itr = list.iterator(); itr.hasNext();) {
            Cleanable p = itr.next();
            if (p.isCompleted()) {
                p.close();
                itr.remove();
            }
        }
    }

    public synchronized void add(Cleanable p) {
        list.add(p);
    }

}
