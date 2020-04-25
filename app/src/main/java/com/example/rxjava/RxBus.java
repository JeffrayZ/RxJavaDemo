package com.example.rxjava;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * @ProjectName: RxJava2Test
 * @Package: com.example.rxjava
 * @ClassName: RxBus
 * @Description: java类作用描述
 * @Author: Jeffray
 * @CreateDate: 2020/4/25 16:33
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/4/25 16:33
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class RxBus {
    private final Subject<Object> bus;

    private RxBus() {
        bus = PublishSubject.create().toSerialized();
    }

    public static RxBus getInstance() {
        return RxBusHolder.mInstance;
    }

    private static class RxBusHolder {
        private static RxBus mInstance = new RxBus();
    }

    public void post(Object object) {
        bus.onNext(object);
    }

    public <T> Observable<T> toObservable(Class<T> eventType) {
        return bus.ofType(eventType);
    }
}

// Subject有两种用途:
//        做为observable向其他的observable发送事件
//        做为observer接收其他的observable发送的事件
