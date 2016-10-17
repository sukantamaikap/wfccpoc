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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;

/**
 * Helper class to check
 */
public class ConnectionProbe {

    private static String TAG = ConnectionProbe.class.getSimpleName();
    private static List<String> mBrowsingTestUrl = Arrays.asList("https://500px.com/","https://www.google.co.in/", "https://in.yahoo.com/", "https://www.reddit.com/", "");

    /**
     * This is a thin client. Only concentrates on ensuring that it can connect to :
     * @param context
     * @return
     */
    public static boolean hasInternetAccess(final Context context) {
        if (isNetworkAvailable(context)) {
            try {
                final long startTime = System.currentTimeMillis();
                final HttpURLConnection urlConnection = (HttpURLConnection) (new URL("http://clients3.google.com/generate_204").openConnection());
                urlConnection.setRequestProperty("User-Agent", "Android");
                urlConnection.setRequestProperty("Connection", "close");
                urlConnection.setConnectTimeout(1500);
                urlConnection.connect();
                final long endTime = System.currentTimeMillis();
                Log.v(TAG, "Thin access operation finished in mS : " + (endTime - startTime));
                return (urlConnection.getResponseCode() == 204 && urlConnection.getContentLength() == 0);
            } catch (IOException e) {
                Log.d(TAG, "Error establishing connection, internet connection unavailable!");
                return Boolean.FALSE;
            }
        } else {
            Log.d(TAG, "Wifi not available, no network connection available");
            return Boolean.FALSE;
        }
    }

    /**
     * Use this method to calculate browsing speed. Ideal implementation would hit different feature reach web pages at random
     * and calculate the time required to fetch the entire content of the page. This can be denoted as browsing speed.
     * @param context
     */
    public static float calculateBrowsingSpeed(final Context context) {
        ByteArrayOutputStream outputStream = null;
        InputStream inputStream = null;
        if (isNetworkAvailable(context)) {
            SecureRandom random = new SecureRandom();
            final int index = random.nextInt(mBrowsingTestUrl.size() + 1);
            try {
                outputStream = new ByteArrayOutputStream();
                final long startTime = System.currentTimeMillis();
                HttpURLConnection urlc = (HttpURLConnection) (new URL("https://www.reddit.com/").openConnection());
                if (urlc.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    throw new IOException(urlc.getResponseMessage() + " : with ");
                }
                inputStream = urlc.getInputStream();
                int byteRead = 0;
                final byte[] buffer = new byte[1024];
                while ((byteRead = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, byteRead);
                }
                final long endTime = System.currentTimeMillis();
                final float speed = (outputStream.toByteArray().length * 1000f * 8f/ (1024f * 1024f)) / ((endTime - startTime));
                Log.i(TAG, "Calculated speed in Mbps " + speed);
                return speed;
            } catch (MalformedURLException e) {
                Log.e(TAG, "Exception occurred : ", e);
            } catch (IOException e) {
                Log.e(TAG, "Exception occurred : ", e);
            }finally {
//                outputStream.close();
//                inputStream.close();
            }
        }
        return 0.0000000001f;
    }

    /**
     * Use this method to calculate download speed. Ideally this should try to download a known file
     * from a server and calculate the speed from the operation.
     * @return speed in Mbps
     */
    public static long calculateDownloadSpeed(final Context context) {
        if (isNetworkAvailable(context)) {

        }
        return 1L;
    }

    /**
     * Use this method to calculate upload speed. Ideally this should try to upload a file to a known server and
     * calculate the speed from the operation .
     * @return speed in Mbps
     */
    public static long calculateUploadSpeed(final Context context) {
        if (isNetworkAvailable(context)) {

        }
        return 1L;
    }

    private static boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
