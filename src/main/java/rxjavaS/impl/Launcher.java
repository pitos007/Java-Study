package rxjavaS.impl;

import LambdaStreams.Streams.Collector;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class Launcher {
    public static void main(String[] args) {
        //justObs();
        //createObs();
        onsub();
    }

    public static void justObs(){
        // just
        Observable<String> source1 = Observable.just("Task One", "Task Two", "Task Three", "Task Four", "Task Five");
        source1.map(String::length).subscribe(System.out::println);


        // fromIterable
        List<Integer> ageList = Arrays.asList(1982, 1983, 1997, 1993, 2000, 1970, 1963, 1948, 1955, 1958);
        Observable<Integer> source2 = Observable.fromIterable(ageList)
                .map(a -> 2018 - a)
                .filter(a -> a >= 18);
        source2.subscribe(System.out::println);


        // compare RxJava Observable with Java streams
        List<String> strL = Arrays.asList("Task One", "Task Two", "Task Three", "Task Four", "Task Five");
        List<Integer> intL = strL
                .stream()
                .map(s -> s.length() - 5)
                .collect(Collectors.toList());
    }

    public static void createObs(){
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                try {
                    emitter.onNext("Task One");
                    emitter.onNext("Task Two");
                    //...
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        });

        Observable<String> source = Observable.create(emitter -> {
            try {
                emitter.onNext("Task One");
                emitter.onNext("Task Two");
                emitter.onNext("Task Three");
                emitter.onNext("Task Four");
                emitter.onNext("Task Five");
                emitter.onComplete();
            } catch (Throwable e) {
                emitter.onError(e);
            }
        });
        source.map(s -> s.length() - 5).subscribe(System.out::println);

    }


    public static void onsub(){
        Observable<Integer> source3 = Observable.just(35, 23, 19, 53, 20, 17, 63, 48, 55, 38);

        Observer<Integer> obs1 = new Observer<Integer>(){

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer val) {
                System.out.println("received: " + val);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                System.out.println("Done");
            }
        };

        source3.map(b -> 2018 - b)
                .filter(y -> y < 1998)
                .subscribe(obs1);

        // passing arguments to onNext, onError, onComplete
        source3.map(b -> 2018 - b)
                .filter(y -> y < 1998)
                .subscribe(
                        val -> System.out.println("recived " + val),
                        Throwable::printStackTrace,
                        () -> System.out.println("done"));

    }




    //Observable<String> str = Observable.just("Task One", "Task Two", "Task Three", "Task Four", "Task Five");
    // return fromArray(item1, item2, item3, item4, item5); ==> public static <T> Observable<T> fromArray(T... items)
    // return RxJavaPlugins.onAssembly(new ObservableFromArray<T>(items)); ==> public static <T> Observable<T> onAssembly(@NonNull Observable<T> source)
    // return apply(f, source); ==> static <T, R> R apply(@NonNull Function<T, R> f, @NonNull T t)
    // return f.apply(t); ==> R apply(@NonNull T t) throws Exception;

    //str.map(s -> s.length()).subscribe(i -> System.out.println(i));
    // return subscribe(onNext, Functions...) ==> public final Disposable subscribe(Consumer<? super T> onNext)
    // subscribe(ls); ==> public final void subscribe(Observer<? super T> observer)
    // return ls ==> new LambdaObserver<T>(onNext, onError, onComplete, onSubscribe);
}
