package com.venkanna.retrofitdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Repo> repoList = new MyApi().getRepos("venkanna");
                for (Repo r : repoList) {
                    Log.e("Mainactivity", "Sync Method :" + r.toString());
                }


            }
        }).start();

        new MyApi().getReposAsync("venkanna", new Callback<List<Repo>>() {
            @Override
            public void success(List<Repo> repos, Response response) {
                for (Repo r : repos) {
                    Log.e("Mainactivity", "Async Call Method :" + r.toString());
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

        // Retrofit + AsyncTask
        new OurRestService().fetch(new MyApi().getRestService(), new OurRestService.GetResult<List<Repo>, MyApi.RestService>() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public List<Repo> go(MyApi.RestService mService) {
                return mService.listRepos("venkanna");
            }

            @Override
            public void onSuccess(List<Repo> repos) {

            }

            @Override
            public void onError(ErrorResponse errorResponse) {

            }

            @Override
            public boolean handleUnAutherizedError() {
                //TODO: Call refresh token service
                return true;
            }
        }, null);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
