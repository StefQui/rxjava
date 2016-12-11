import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.schedulers.Schedulers;

public class Service2 {
    private static final Logger logger = LoggerFactory.getLogger(Service2.class);

    public Observable<String> operation() {
        return Observable.<String>create(s -> {
            logger.info("Start: Executing slow task in Service 2");
            Util.delay(2000);
            logger.info("End: Executing slow task in Service 2");
            s.onNext("data 2");
            s.onCompleted();
        }).subscribeOn(Schedulers.computation());
    }
    public Observable<String> operation(String st) {
        return Observable.<String>create(s -> {
            logger.info("Start: Executing slow task in Service 2");
            Util.delay(2000);
            logger.info("End: Executing slow task in Service 2");
            s.onNext(st + "data 2");
            s.onCompleted();
        }).subscribeOn(Schedulers.computation());
    }

    public Observable<MyBuffer> operation(MyBuffer b) {
        return Observable.<MyBuffer>create(s -> {
            logger.info("Start: Executing slow task in Service 2");
            Util.delay(4000);
            b.append(" data2 ");
            logger.info("End: Executing slow task in Service 2");
            s.onNext(b);
            s.onCompleted();
        }).subscribeOn(Schedulers.computation());
    }

}