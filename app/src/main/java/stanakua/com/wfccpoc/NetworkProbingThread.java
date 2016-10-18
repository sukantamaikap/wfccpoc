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
import android.support.v4.app.Fragment;
import android.util.Log;

import java.io.IOException;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.util.Calendar;

/**
 * Background thread probing the network. Calls {@link ConnectionProbeFragment} to update the UI
 * when there is no connection.
 */
public class NetworkProbingThread extends Thread {
    private static final String TAG = "NetworkProbingThread";

    private volatile boolean mRunning = Boolean.TRUE;

    private Context mContext;
    private Fragment callingFragment;

    public NetworkProbingThread(final Context context, final Fragment fragment) {
        this.mContext = context;
        this.callingFragment = fragment;
    }

    @Override
    public void run() {
        Log.i(TAG, "running network probe");
        while (this.mRunning) {
            final boolean hasConnection = ConnectionProbe.hasInternetAccess(this.mContext);
            if (!hasConnection) {
                ((ConnectionProbeFragment)this.callingFragment)
                        .updateConnectionStatus("NC at : "
                                + DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime().getTime()) + "\n");
            }

            if (hasConnection && new SecureRandom().nextBoolean()) {
                float speed = 0.0f;
                try {
                    speed = ConnectionProbe.calculateBrowsingSpeed(this.mContext);
                } catch (IOException e) {
                    Log.e(TAG, "Speed calculation errored out !!!");
                }
                ((ConnectionProbeFragment)this.callingFragment).updateConnectionStatus("Speed is : " + speed + "Mbps, at " + DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime().getTime()) + "\n");
            }

            try {
                Thread.sleep(3000);
            } catch (final InterruptedException e) {
                Log.i(TAG, "Thread interrupted, the execution with stop now ... ");
                this.mRunning = Boolean.FALSE;
                return;
            }

            if (Thread.interrupted()) {
                this.mRunning = Boolean.FALSE;
                break;
            }
        }
    }
}
