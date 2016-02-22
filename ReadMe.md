```
public class MyApi {

    public static final String BASE_URL = "https://api.github.com";
    //1.Create interface
    // 2.Create rest adapter
    // 3.Implemention for interface RestService
    //4.Consume APis


    //1.Create interface
    public interface RestService {

        //synchronous get method, dont run on UI thread.
        @GET("/users/{user}/repos")
        List<Repo> listRepos(@Path("user") String user);

        //Asynchronous get method, can run on UI thread.
        @GET("/users/{user}/repos")
        void listReposAsync(@Path("user") String user, Callback<List<Repo>> callback);
    }

    // 2.Create rest adapter
    private RestAdapter buildRestAdapter() {
        RestAdapter.Builder builder = new RestAdapter.Builder();
        builder.setEndpoint(BASE_URL);
        //displaying  logs in logcat with request and response
        //set NOne in producation builds
        builder.setLogLevel(RestAdapter.LogLevel.FULL);

        RestAdapter adapter = builder.build();
        adapter.create(RestService.class);
        return adapter;
    }

    //3.Implemention for interface RestService
    public RestService getRestService() {
        RestAdapter adapter = buildRestAdapter();
        RestService service = adapter.create(RestService.class);
        return service;
    }

    //4.Consume APis - Sync
    public List<Repo> getRepos(String gitUserName) {
        return getRestService().listRepos(gitUserName);
    }

    //4.Consume APis - Async
    public void getReposAsync(String gitUserName, Callback<List<Repo>> callback) {
        getRestService().listReposAsync(gitUserName, callback);
    }
}
```


