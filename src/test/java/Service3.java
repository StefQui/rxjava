import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.schedulers.Schedulers;

public class Service3 {
    private static final Logger logger = LoggerFactory.getLogger(Service3.class);

    public Observable<String> operation(String s1) {
        return Observable.<String>create(s -> {
            logger.info("Start: Executing slow task in Service 3");
            Util.delay(1500);
            s.onNext(s1 + "data 3");
            logger.info("End: Executing slow task in Service 3");
            s.onCompleted();
        }).subscribeOn(Schedulers.computation());
    }


    public Observable<MyBuffer> operation(MyBuffer b) {
        return Observable.<MyBuffer>create(s -> {
            logger.info("Start: Executing slow task in Service 3");
            Util.delay(3500);
            b.append(" data3 ");
            logger.info("End: Executing slow task in Service 3");
            s.onNext(b);
            s.onCompleted();
        }).subscribeOn(Schedulers.computation());
    }


}