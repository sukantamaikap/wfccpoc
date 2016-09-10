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

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by smaikap on 6/9/16.
 */
public class ConnectionProbeFragment extends Fragment {

    private static final String TAG = "ConnectionProbeFragment";

    private Button mStartButton;
    private Button mStopButton;
//    private ProgressBar mProgressBar;
    private EditText mConnectionStatus;


    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(Boolean.TRUE);
    }

    public static ConnectionProbeFragment newInstance() {
        return new ConnectionProbeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater,
                             @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_connectivity, container, false);

//        this.mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar_probe_progress);
        this.mConnectionStatus = (EditText) view.findViewById(R.id.connection_log);
        this.mStartButton = (Button) view.findViewById(R.id.button_start);

        this.mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectionProbeFragment.this.startProbe();
            }
        });

        this.mStopButton = (Button) view.findViewById(R.id.button_stop);
        this.mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ConnectionProbeFragment.this.getActivity(), "Stopped !!", Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }

    private void startProbe() {
        Log.d(TAG, "Starting probe");

        final Handler connectionProbeHandler = new Handler();
        final String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        ConnectionProbeFragment.this.mConnectionStatus.append("Probe started at : " + currentDateTimeString);
        final Runnable probeThread = new Runnable() {
            @Override
            public void run() {
                final boolean hasConnection = ConnectionProbe.hasInternetAccess(ConnectionProbeFragment.this.getActivity());
                if (!hasConnection) {
                    ConnectionProbeFragment.this.mConnectionStatus.append("No net connection at : " + currentDateTimeString);
                }
                else {
                    Log.d(TAG, "Device has connection to internet");
                }
            }
        };
        connectionProbeHandler.postDelayed(probeThread, 1000);
    }
}
