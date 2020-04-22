package com.example.rxjava;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Network;
import android.os.Bundle;
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
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
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
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity >>>>>";

    @SuppressLint("CheckResult")
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
                        Log.e(TAG, "map:转换前:" + response.body() + ">><<" + Thread.currentThread().getName());
                        return new Gson().fromJson(body.string(), MobileAddress.class);
                    }
                }
                return null;
            }
        }).observeOn(AndroidSchedulers.mainThread())
                // 总结:
                // 1、通过源码可以看到, onOnNext()的accept()方法仅仅只是在Observer的onXXX()方法被调用之前调用, 方且没有与Observer的调用之间没有任何关系;
                // 2、所以doOnNext()这个方法可以用来在观察者Observer:onXXX()方法被调用之前进行一些初始化操作;
                .doOnNext(new Consumer<MobileAddress>() {
                    @Override
                    public void accept(@NonNull MobileAddress s) throws Exception {
                        Log.e(TAG, "doOnNext: 保存成功：" + s.toString() + ">><<" + Thread.currentThread().getName());
                    }
                }).subscribeOn(Schedulers.io())
                .subscribe(new Consumer<MobileAddress>() {
                    @Override
                    public void accept(@NonNull MobileAddress data) throws Exception {
                        Log.e(TAG, "成功:" + data.toString() + ">><<" + Thread.currentThread().getName());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "失败：" + throwable.getMessage() + "\n");
                    }
                });

            // ==========================华丽丽的分割线==========================

//            Observable.concat()

            // ==========================华丽丽的分割线==========================

//        Observable.just(1,2,2,3,4,4,5).distinct() // 去重

        // ==========================华丽丽的分割线==========================

//        Observable.just(1, 20, 65, -5, 7, 19)
//                .filter(new Predicate<Integer>() {
//                    @Override
//                    public boolean test(@NonNull Integer integer) throws Exception {
//                        return integer >= 10;
//                    }
//                }) // 过滤

        // ==========================华丽丽的分割线==========================

//        Observable.just(1, 2, 3, 4, 5).buffer(3, 2)
        // 我们 buffer 的第一个参数是 count，代表最大取值，在事件足够的时候，一般都是取 count 个值，然后每次跳过 skip 个事件

        // ==========================华丽丽的分割线==========================

//        Observable.timer(2, TimeUnit.SECONDS) // 定时任务，相当于第一次事件和第二次事件间隔两秒

        // ==========================华丽丽的分割线==========================

//        Observable.interval(3,2, TimeUnit.SECONDS) // 定时任务，首次延时3秒，后每隔两秒发一次

        // ==========================华丽丽的分割线==========================

//        Observable.just(1,2,3,4,5).skip(2) // 跳过前两个数

        // ==========================华丽丽的分割线==========================

//        Flowable.fromArray(1,2,3,4,5).take(2) // 表示只接收前两个数据

        // ==========================华丽丽的分割线==========================

//        Observable.create().debounce(500,TimeUnit.MILLISECONDS) //去除发送间隔时间小于 500 毫秒的发射事件

        // ==========================华丽丽的分割线==========================

//        Observable<Integer> observable = Observable.defer(new Callable<ObservableSource<Integer>>() {
//            @Override
//            public ObservableSource<Integer> call() throws Exception {
//                return Observable.just(1, 2, 3);
//            }
//        });
//
//        StringBuilder mRxOperatorsText = new StringBuilder();
//
//        observable.subscribe(new Observer<Integer>() {
//            @Override
//            public void onSubscribe(@NonNull Disposable d) {
//
//            }
//
//            @Override
//            public void onNext(@NonNull Integer integer) {
//                mRxOperatorsText.append("defer : " + integer + "\n");
//                Log.e(TAG, "defer : " + integer + "\n");
//            }
//
//            @Override
//            public void onError(@NonNull Throwable e) {
//                mRxOperatorsText.append("defer : onError : " + e.getMessage() + "\n");
//                Log.e(TAG, "defer : onError : " + e.getMessage() + "\n");
//            }
//
//            @Override
//            public void onComplete() {
//                mRxOperatorsText.append("defer : onComplete\n");
//                Log.e(TAG, "defer : onComplete\n");
//            }
//        });

//        简单地时候就是每次订阅都会创建一个新的 Observable，并且如果没有被订阅，就不会产生新的 Observable。

        // ==========================华丽丽的分割线==========================

//        last
//        last 操作符仅取出可观察到的最后一个值，或者是满足某些条件的最后一项。
//        Observable.just(1, 2, 3)
//                .last(4)
//                .subscribe(new Consumer<Integer>() {
//                    @Override
//                    public void accept(@NonNull Integer integer) throws Exception {
//                        mRxOperatorsText.append("last : " + integer + "\n");
//                        Log.e(TAG, "last : " + integer + "\n");
//                    }
//                });

        // ==========================华丽丽的分割线==========================

//        注意它和 concat 的区别在于，不用等到 发射器 A 发送完所有的事件再进行发射器 B 的发送。
//        Observable.merge(Observable.just(1, 2), Observable.just(3, 4, 5))
//                .subscribe(new Consumer<Integer>() {
//                    @Override
//                    public void accept(@NonNull Integer integer) throws Exception {
//                        mRxOperatorsText.append("merge :" + integer + "\n");
//                        Log.e(TAG, "accept: merge :" + integer + "\n" );
//                    }
//                });

        // ==========================华丽丽的分割线==========================

//        Observable.just(1, 2, 3)
//                .reduce(new BiFunction<Integer, Integer, Integer>() {
//                    @Override
//                    public Integer apply(@NonNull Integer integer, @NonNull Integer integer2) throws Exception {
//                        return integer + integer2;
//                    }
//                }).subscribe(new Consumer<Integer>() {
//            @Override
//            public void accept(@NonNull Integer integer) throws Exception {
//                mRxOperatorsText.append("reduce : " + integer + "\n");
//                Log.e(TAG, "accept: reduce : " + integer + "\n");
//            }
//        });
        // 1+2=3+3=6

        // ==========================华丽丽的分割线==========================

//        Observable.just(1, 2, 3)
//                .scan(new BiFunction<Integer, Integer, Integer>() {
//                    @Override
//                    public Integer apply(@NonNull Integer integer, @NonNull Integer integer2) throws Exception {
//                        return integer + integer2;
//                    }
//                }).subscribe(new Consumer<Integer>() {
//            @Override
//            public void accept(@NonNull Integer integer) throws Exception {
//                mRxOperatorsText.append("scan " + integer + "\n");
//                Log.e(TAG, "accept: scan " + integer + "\n");
//            }
//        });
        // 1,3,6 依次输出

        // ==========================华丽丽的分割线==========================

        // 先读取缓存，如果缓存没数据再通过网络请求获取数据后更新UI
//        Observable<FoodList> cache = Observable.create(new ObservableOnSubscribe<FoodList>() {
//            @Override
//            public void subscribe(@NonNull ObservableEmitter<FoodList> e) throws Exception {
//                Log.e(TAG, "create当前线程:"+Thread.currentThread().getName() );
//                FoodList data = CacheManager.getInstance().getFoodListData();
//
//                // 在操作符 concat 中，只有调用 onComplete 之后才会执行下一个 Observable
//                if (data != null){ // 如果缓存数据不为空，则直接读取缓存数据，而不读取网络数据
//                    isFromNet = false;
//                    Log.e(TAG, "\nsubscribe: 读取缓存数据:" );
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            mRxOperatorsText.append("\nsubscribe: 读取缓存数据:\n");
//                        }
//                    });
//
//                    e.onNext(data);
//                }else {
//                    isFromNet = true;
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            mRxOperatorsText.append("\nsubscribe: 读取网络数据:\n");
//                        }
//                    });
//                    Log.e(TAG, "\nsubscribe: 读取网络数据:" );
//                    e.onComplete();
//                }
//
//
//            }
//        });
//
//        Observable<FoodList> network = Rx2AndroidNetworking.get("http://www.tngou.net/api/food/list")
//                .addQueryParameter("rows",10+"")
//                .build()
//                .getObjectObservable(FoodList.class);
//
//
//        // 两个 Observable 的泛型应当保持一致
//
//        Observable.concat(cache,network)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<FoodList>() {
//                    @Override
//                    public void accept(@NonNull FoodList tngouBeen) throws Exception {
//                        Log.e(TAG, "subscribe 成功:"+Thread.currentThread().getName() );
//                        if (isFromNet){
//                            mRxOperatorsText.append("accept : 网络获取数据设置缓存: \n");
//                            Log.e(TAG, "accept : 网络获取数据设置缓存: \n"+tngouBeen.toString() );
//                            CacheManager.getInstance().setFoodListData(tngouBeen);
//                        }
//
//                        mRxOperatorsText.append("accept: 读取数据成功:" + tngouBeen.toString()+"\n");
//                        Log.e(TAG, "accept: 读取数据成功:" + tngouBeen.toString());
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(@NonNull Throwable throwable) throws Exception {
//                        Log.e(TAG, "subscribe 失败:"+Thread.currentThread().getName() );
//                        Log.e(TAG, "accept: 读取数据失败："+throwable.getMessage() );
//                        mRxOperatorsText.append("accept: 读取数据失败："+throwable.getMessage()+"\n");
//                    }
//                });

        // ==========================华丽丽的分割线==========================

//        多个网络请求依次依赖
//        想必这种情况也在实际情况中比比皆是，例如用户注册成功后需要自动登录，我们只需要先通过注册接口注册用户信息，
//        注册成功后马上调用登录接口进行自动登录即可。
//        Rx2AndroidNetworking.get("http://www.tngou.net/api/food/list")
//                .addQueryParameter("rows", 1 + "")
//                .build()
//                .getObjectObservable(FoodList.class) // 发起获取食品列表的请求，并解析到FootList
//                .subscribeOn(Schedulers.io())        // 在io线程进行网络请求
//                .observeOn(AndroidSchedulers.mainThread()) // 在主线程处理获取食品列表的请求结果
//                .doOnNext(new Consumer<FoodList>() {
//                    @Override
//                    public void accept(@NonNull FoodList foodList) throws Exception {
//                        // 先根据获取食品列表的响应结果做一些操作
//                        Log.e(TAG, "accept: doOnNext :" + foodList.toString());
//                        mRxOperatorsText.append("accept: doOnNext :" + foodList.toString()+"\n");
//                    }
//                })
//                .observeOn(Schedulers.io()) // 回到 io 线程去处理获取食品详情的请求
//                .flatMap(new Function<FoodList, ObservableSource<FoodDetail>>() {
//                    @Override
//                    public ObservableSource<FoodDetail> apply(@NonNull FoodList foodList) throws Exception {
//                        if (foodList != null && foodList.getTngou() != null && foodList.getTngou().size() > 0) {
//                            return Rx2AndroidNetworking.post("http://www.tngou.net/api/food/show")
//                                    .addBodyParameter("id", foodList.getTngou().get(0).getId() + "")
//                                    .build()
//                                    .getObjectObservable(FoodDetail.class);
//                        }
//                        return null;
//
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<FoodDetail>() {
//                    @Override
//                    public void accept(@NonNull FoodDetail foodDetail) throws Exception {
//                        Log.e(TAG, "accept: success ：" + foodDetail.toString());
//                        mRxOperatorsText.append("accept: success ：" + foodDetail.toString()+"\n");
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(@NonNull Throwable throwable) throws Exception {
//                        Log.e(TAG, "accept: error :" + throwable.getMessage());
//                        mRxOperatorsText.append("accept: error :" + throwable.getMessage()+"\n");
//                    }
//                });

        // ==========================华丽丽的分割线==========================

//        结合多个接口的数据更新 UI
//        在实际应用中，我们极有可能会在一个页面显示的数据来源于多个接口，这时候我们的 zip 操作符为我们排忧解难。
//        Observable<MobileAddress> observable1 = Rx2AndroidNetworking.get("http://api.avatardata.cn/MobilePlace/LookUp?key=ec47b85086be4dc8b5d941f5abd37a4e&mobileNumber=13021671512")
//                .build()
//                .getObjectObservable(MobileAddress.class);
//
//        Observable<CategoryResult> observable2 = Network.getGankApi()
//                .getCategoryData("Android",1,1);
//
//        Observable.zip(observable1, observable2, new BiFunction<MobileAddress, CategoryResult, String>() {
//            @Override
//            public String apply(@NonNull MobileAddress mobileAddress, @NonNull CategoryResult categoryResult) throws Exception {
//                return "合并后的数据为：手机归属地："+mobileAddress.getResult().getMobilearea()+"人名："+categoryResult.results.get(0).who;
//            }
//        }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(@NonNull String s) throws Exception {
//                        Log.e(TAG, "accept: 成功：" + s+"\n");
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(@NonNull Throwable throwable) throws Exception {
//                        Log.e(TAG, "accept: 失败：" + throwable+"\n");
//                    }
//                });

        // ==========================华丽丽的分割线==========================

//        间隔任务实现心跳
//        想必即时通讯等需要轮训的任务在如今的 APP 中已是很常见，而 RxJava 2.x 的 interval 操作符可谓完美地解决了我们的疑惑。
//        private Disposable mDisposable;
//        @Override
//        protected void doSomething() {
//            mDisposable = Flowable.interval(1, TimeUnit.SECONDS)
//                    .doOnNext(new Consumer<Long>() {
//                        @Override
//                        public void accept(@NonNull Long aLong) throws Exception {
//                            Log.e(TAG, "accept: doOnNext : "+aLong );
//                        }
//                    })
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Consumer<Long>() {
//                        @Override
//                        public void accept(@NonNull Long aLong) throws Exception {
//                            Log.e(TAG, "accept: 设置文本 ："+aLong );
//                            mRxOperatorsText.append("accept: 设置文本 ："+aLong +"\n");
//                        }
//                    });
//        }
//
//        /**
//         * 销毁时停止心跳
//         */
//        @Override
//        protected void onDestroy() {
//            super.onDestroy();
//            if (mDisposable != null){
//                mDisposable.dispose();
//            }
//        }

        // ==========================华丽丽的分割线==========================


















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
            Log.i("MyInterceptor1", String.format("Sending request %s on %s%n%s",
                    request.url(), chain.connection(), request.headers()));

            Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            Log.i("MyInterceptor1", String.format("Received response for %s in %.1fms%n%s%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers(), response.body().string()));

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
     * 三个参数 Tag就是log中的tag，msg为具体信息
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
