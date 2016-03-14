package com.venkanna.retrofitdemo;

import android.os.AsyncTask;
import android.util.Log;

import retrofit.RetrofitError;

public class OurRestService<ReturnType, RestService> {

    private static final String TAG = OurRestService.class.getSimpleName();
    private static final int MAX_RETRIES = 2;

    public static abstract class GetResult<ReturnResult, ServiceClass> {
        public abstract ReturnResult go(ServiceClass mService);

        public abstract void onSuccess(ReturnResult returnResult);

        public abstract void onError(ErrorResponse errorResponse);

        public void onStart() {
            //
        }

        public boolean handleUnAutherizedError() {
            return false;
        }


    }

    public AsyncTask fetch(
            final RestService service,
            final GetResult<ReturnType, RestService> getResult,
            final ErrorResponse errorResponse
    ) {
        return new AsyncTask<Void, Void, ReturnType>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                getResult.onStart();
            }

            @Override
            protected ReturnType doInBackground(Void... params) {
                ReturnType res = null;
                for (int i = 0; i < MAX_RETRIES; i++) {
                    try {
                        res = getResult.go(service);
                        return res;
                    } catch (RetrofitError e) {
                        if (e.getResponse().getStatus() == 401 && getResult.handleUnAutherizedError()) {
                            continue;
                        }
                        errorResponse.fill(e.getResponse().getStatus(),
                                e.getResponse().getReason(),
                                e.getResponse().getUrl(),
                                e.isNetworkError());
                        return null;
                    } catch (Exception e) {
                        Log.e(TAG, "Unknown error", e);
                        return null;
                    }
                }
                if (isCancelled()) {
                    res = null;
                }
                return res;
            }

            @Override
            protected void onPostExecute(ReturnType res) {
                if (res != null) {
                    getResult.onSuccess(res);
                } else if (errorResponse != null) {
                    getResult.onError(errorResponse);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);
    }

}