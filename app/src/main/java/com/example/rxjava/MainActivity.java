package com.example.rxjava;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.strictmode.LeakedClosableViolation;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.jakewharton.rxbinding2.view.RxView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableOperator;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.logging.HttpLoggingInterceptor;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity >>>>>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] strs = {"ajk", "lajdl", "aljl", "qiyi", "zfzygh"};
        Observable.fromArray(strs).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, s);
            }
        });

        // ==========================华丽丽的分割线==========================

        final ImageView imageView = findViewById(R.id.iv);
        Observable.create(new ObservableOnSubscribe<Drawable>() {

            @Override
            public void subscribe(ObservableEmitter<Drawable> emitter) throws Exception {
                Drawable drawable = ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_launcher_background);
                emitter.onNext(drawable);
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Drawable>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Drawable drawable) {
                        imageView.setImageDrawable(drawable);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        // ==========================华丽丽的分割线==========================

        Observable.just(1, 2, 3, 4)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "number:" + integer);
                    }
                });

        // ==========================华丽丽的分割线==========================

        Observable.just("images/logo.png")
                .map(new Function<String, Bitmap>() {
                    @Override
                    public Bitmap apply(String s) throws Exception {
                        return getBitmapFromPath(s);
                    }
                })
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        showBitmap(bitmap);
                    }
                });

        // ==========================华丽丽的分割线==========================

        List<Student.Course> list1 = new ArrayList<>();
        list1.add(new Student.Course("语文"));
        List<Student.Course> list2 = new ArrayList<>();
        list2.add(new Student.Course("语文"));
        list2.add(new Student.Course("数学"));
        List<Student.Course> list3 = new ArrayList<>();
        list3.add(new Student.Course("语文"));
        list3.add(new Student.Course("数学"));
        list3.add(new Student.Course("英语"));
        Student[] students = {new Student("jkdsa", 18, list1),
                new Student("ahdiq", 16, list2),
                new Student("阿珂话费卡", 9, list3)};
        Observable.fromArray(students).map(new Function<Student, String>() {
            @Override
            public String apply(Student student) throws Exception {
                return student.getName();
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String name) throws Exception {
                Log.d(TAG, name);
            }
        });

        // ==========================华丽丽的分割线==========================

        Observable.fromArray(students)
                .flatMap(new Function<Student, ObservableSource<List<Student.Course>>>() {
                    @Override
                    public ObservableSource<List<Student.Course>> apply(Student student) throws Exception {
                        return Observable.fromArray(student.getCourses());
                    }
                }).subscribe(new Consumer<List<Student.Course>>() {
            @Override
            public void accept(List<Student.Course> courses) throws Exception {
                Log.d(TAG, courses.toString());
            }
        });

        // ==========================华丽丽的分割线==========================

        Observer<Student.Course> observer = new Observer<Student.Course>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "flatMap" + d.toString());
            }

            @Override
            public void onNext(Student.Course course) {
                Log.d(TAG, course.getName());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

        Observable.fromArray(students).flatMap(new Function<Student, ObservableSource<Student.Course>>() {
            @Override
            public ObservableSource<Student.Course> apply(Student student) throws Exception {
                return Observable.fromArray(student.getCourses().toArray(new Student.Course[student.getCourses().size()]));
            }
        }).subscribe(observer);

        // ==========================华丽丽的分割线==========================

        Observable.just(80808).lift(new ObservableOperator<String, Integer>() {
            @Override
            public Observer<? super Integer> apply(final Observer<? super String> observer) throws Exception {
                return new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        observer.onSubscribe(d);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        observer.onNext(integer.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                };
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, s);
            }
        });

        // ==========================华丽丽的分割线==========================

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "线程切换 subscribe>>" + Thread.currentThread().getName());
            }
        })
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        Log.d(TAG, "线程切换 doOnSubscribe>>" + Thread.currentThread().getName());
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread()) // 指定主线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "线程切换 onSubscribe>>" + Thread.currentThread().getName());
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "线程切换 onNext>>" + Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "线程切换 onError>>" + Thread.currentThread().getName());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "线程切换 onComplete>>" + Thread.currentThread().getName());
                    }
                });

        // ==========================华丽丽的分割线==========================

        RxView.clicks(imageView).throttleFirst(3000L, TimeUnit.MILLISECONDS).subscribe(new Observer<Object>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "RxView onSubscribe>>");
            }

            @Override
            public void onNext(Object o) {
                Log.d(TAG, "RxView onNext>>");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "RxView onError>>");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "RxView onComplete>>");
            }
        });

        // ==========================华丽丽的分割线==========================

        Disposable disposable = Observable.just(555).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {

            }
        });

        disposable.dispose();

        // ==========================华丽丽的分割线==========================

        Observable.create(new ObservableOnSubscribe<Response>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Response> e) throws Exception {
                Request.Builder builder = new Request.Builder()
                        .url("http://api.avatardata.cn/MobilePlace/LookUp?key=ec47b85086be4dc8b5d941f5abd37a4e&mobileNumber=13021671512")
                        .get();
                Request request = builder.build();
                OkHttpClient client = new OkHttpClient.Builder()
//                        .addInterceptor(mLoggingInterceptor)
                        .addNetworkInterceptor(mLoggingInterceptor)
//                        .addInterceptor(new MyInterceptor())
//                        .addNetworkInterceptor(new MyInterceptor())
//                        .addInterceptor(new MyInterceptor1())
//                        .addNetworkInterceptor(new MyInterceptor1())
//                        .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
//                        .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                        .build();
                Call call = client.newCall(request);
                Response response = call.execute();
                e.onNext(response);
            }
        }).map(new Function<Response, MobileAddress>() {
            @Override
            public MobileAddress apply(@NonNull Response response) throws Exception {
                if (response.isSuccessful()) {
                    ResponseBody body = response.body();
                    if (body != null) {
                        Log.e(TAG, "map:转换前:" + response.body());
                        return new Gson().fromJson(body.string(), MobileAddress.class);
                    }
                }
                return null;
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<MobileAddress>() {
                    @Override
                    public void accept(@NonNull MobileAddress s) throws Exception {
                        Log.e(TAG, "doOnNext: 保存成功：" + s.toString() + "\n");
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MobileAddress>() {
                    @Override
                    public void accept(@NonNull MobileAddress data) throws Exception {
                        Log.e(TAG, "成功:" + data.toString() + "\n");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "失败：" + throwable.getMessage() + "\n");
                    }
                });
    }

    private void showBitmap(Bitmap bitmap) {
        Log.d(TAG, "showBitmap 展示图片");
    }

    private Bitmap getBitmapFromPath(String s) {
        return Bitmap.createBitmap(200, 200, Bitmap.Config.RGB_565);
    }

    public class MyInterceptor implements Interceptor {

        @NotNull
        @Override
        public Response intercept(@NotNull Chain chain) throws IOException {
            // 拦截请求，获取到该次请求的request
            Request request = chain.request();
            // 执行本次网络请求操作，返回response信息
            Response response = chain.proceed(request);
            if (true) {
                for (String key : request.headers().toMultimap().keySet()) {
                    Log.e("MyInterceptor", "header: {" + key + " : " + request.headers().toMultimap().get(key) + "}");
                }
                Log.e("MyInterceptor", "url: " + request.url().uri().toString());
                ResponseBody responseBody = response.body();

                if (HttpHeaders.promisesBody(response) && responseBody != null) {
                    BufferedReader bufferedReader = new BufferedReader(new
                            InputStreamReader(responseBody.byteStream(), "utf-8"));
                    String result;
                    while ((result = bufferedReader.readLine()) != null) {
                        Log.e("MyInterceptor", "response: " + result);
                    }
                    // 测试代码
                    responseBody.string();
                }
            }
            // 注意，这样写，等于重新创建Request，获取新的Response，避免在执行以上代码时，
            // 调用了responseBody.string()而不能在返回体中再次调用。
            return response.newBuilder().build();
        }
    }

    public class MyInterceptor1 implements Interceptor {

        @NotNull
        @Override
        public Response intercept(@NotNull Chain chain) throws IOException {
            Request request = chain.request();

            long t1 = System.nanoTime();
            Log.i("MyInterceptor1",String.format("Sending request %s on %s%n%s",
                    request.url(), chain.connection(), request.headers()));

            Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            Log.i("MyInterceptor1",String.format("Received response for %s in %.1fms%n%s%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers(),response.body().string()));

            return response.newBuilder().build();
        }
    }

    /**
     * 日志拦截器
     */
    private static final Interceptor mLoggingInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            long t1 = System.nanoTime();//请求发起的时间
            Log.e("Interceptor", String.format("发送请求 %s on %s%n%s", request.url(), chain.connection(), request.headers()));
            Response response = chain.proceed(request);
            long t2 = System.nanoTime();//收到响应的时间
            //这里不能直接使用response.body().string()的方式输出日志
            //因为response.body().string()之后，response中的流会被关闭，程序会报错，我们需要创建出一个新的response给应用层处理
            ResponseBody responseBody = response.peekBody(1024 * 1024);
            printLog("接受响应", responseBody.string());
            return response;
        }
    };

    /**
     *   三个参数 Tag就是log中的tag，msg为具体信息
     */
    public static void printLog(String tag, String msg) {
        String message = null;
        try {//需判断json是什么格式
            if (msg.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(msg);
                message = jsonObject.toString(4);//最重要的方法，就一行，返回格式化的json字符串，其中的数字4是缩进字符数
            } else if (msg.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(msg);
                message = jsonArray.toString(4);
            } else {
                message = msg;
            }
        } catch (JSONException e) {
            message = msg;
        }
        String[] lines = message.split("\n");
        //此处也可以画分割线
        //log.e(tag,"----------------------------------------------")
        for (String line : lines) {
            Log.d(tag, "|" + line);
        }
        //log.e(tag,"----------------------------------------------")
    }

}
