import org.junit.Test;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Created by stef on 09/12/16.
 */
public class myrx {


    @Test
    public void rx1() throws Exception {
        System.out.println("hello");
        Observable.from(Arrays.asList("toto","tata")).subscribe(new Action1<String>() {

            @Override
            public void call(String s) {
                System.out.println("Hello " + s + "!");
            }

        });
    }

    @Test
    public void rx2() throws Exception {
        Observable<String> myObservable = Observable.create(
                new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> sub) {
                        sub.onNext("Hello, world!");
                        sub.onCompleted();
                    }
                }
        );

        Subscriber<String> mySubscriber = new Subscriber<String>() {
            @Override
            public void onNext(String s) { System.out.println(s); }

            @Override
            public void onCompleted() { }

            @Override
            public void onError(Throwable e) { }
        };

        myObservable.subscribe(mySubscriber);
    }

    @Test
    public void rx3() throws Exception {
        Observable<String> myObservable =
                Observable.just("Hello, world!");
        Action1<String> onNextAction = new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println("call..." + s);
            }
        };
        myObservable.subscribe(onNextAction);

    }

    @Test
    public void rx4() {
        Observable.just("Hello, world!")
                .subscribe(s -> System.out.println(s));
    }

    @Test
    public void rx5() {
        Observable.just("Hello, world!")
                .map(s -> s + " -Dan")
                .subscribe(s -> System.out.println(s));
    }

    @Test
    public void rx6() {
        Observable.just("Hello, world!")
                .map(s -> s + " -Dan")
                .map(s -> s.hashCode())
                .map(i -> Integer.toString(i))
                .subscribe(s -> System.out.println(s));
    }

}
