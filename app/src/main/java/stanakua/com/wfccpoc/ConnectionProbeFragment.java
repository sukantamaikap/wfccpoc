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

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class ConnectionProbeFragment extends Fragment {

    private static final String TAG = "ConnectionProbeFragment";

    private Handler mUiUpdaterHandler;

    private Button mStartButton;
    private Button mStopButton;
    private ProgressBar mProgressBar;
    private EditText mConnectionStatus;
    private NetworkProbingThread mNetworkProbingThread;

    public static ConnectionProbeFragment newInstance() {
        return new ConnectionProbeFragment();
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(Boolean.TRUE);
        this.mUiUpdaterHandler = new Handler(this.getContext().getMainLooper());
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater,
                             @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_connectivity, container, false);

        this.mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar_probe_progress);
        this.mConnectionStatus = (EditText) view.findViewById(R.id.connection_log);
        this.mStartButton = (Button) view.findViewById(R.id.button_start);

        this.mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectionProbeFragment.this.mStartButton.setEnabled(Boolean.FALSE);
                ConnectionProbeFragment.this.startProbe();
            }
        });

        this.mStopButton = (Button) view.findViewById(R.id.button_stop);
        this.mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectionProbeFragment.this.mNetworkProbingThread.interrupt();
                Toast.makeText(ConnectionProbeFragment.this.getActivity(), "Stopped !!", Toast.LENGTH_LONG).show();
                ConnectionProbeFragment.this.mStartButton.setEnabled(Boolean.TRUE);
            }
        });
        return view;
    }

    /**
     * To be used by network thread to update the {@link this.mConnectionStatus} only when there
     * is no connection.
     * @param status : String with time stamp for no connection occurrence.
     */
    public void updateConnectionStatus(final String status) {
        this.mUiUpdaterHandler.post(new Runnable() {
            @Override
            public void run() {
                ConnectionProbeFragment.this.mConnectionStatus.append(status);
            }
        });
    }

    private void startProbe() {
        Log.d(TAG, "Starting probe");
        this.mNetworkProbingThread = new NetworkProbingThread(this.getActivity(), this);
        this.mNetworkProbingThread.start();
    }
}
