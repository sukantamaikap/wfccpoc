/*
 * Copyright 2016 Sukanta Maikap
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package stanakua.com.wfccpoc;

import android.content.Context;
import android.util.Log;

/**
 * Background thread probing the network.
 */
public class NetworkProbingThread extends Thread {
    private static final String TAG = "NetworkProbingThread";

    private volatile boolean mRunning = Boolean.TRUE;

    private Context mContext;

    public NetworkProbingThread(final Context context) {
        this.mContext = context;
    }

    public void setProbeRunning(boolean running) {
        Log.i(TAG, "Request to change thread status, running : " + running);
        this.mRunning = running;
    }

    @Override
    public void run() {
        Log.i(TAG, "running network probe");
        while (this.mRunning) {
            final boolean hasConnection = ConnectionProbe.hasInternetAccess(mContext);
            if (!hasConnection) {
//                ConnectionProbeFragment.getUiUpdaterHandler().post(new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                });
            }
        }
    }
}
