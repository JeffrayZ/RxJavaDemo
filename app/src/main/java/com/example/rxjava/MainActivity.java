package com.example.rxjava;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.jakewharton.rxbinding2.view.RxView;

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
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

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
    }

    private void showBitmap(Bitmap bitmap) {
        Log.d(TAG, "showBitmap 展示图片");
    }

    private Bitmap getBitmapFromPath(String s) {
        return Bitmap.createBitmap(200, 200, Bitmap.Config.RGB_565);
    }
}
