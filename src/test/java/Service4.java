import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.schedulers.Schedulers;

public class Service4 {
    private static final Logger logger = LoggerFactory.getLogger(Service4.class);

    public Observable<String> operation(String s1) {
        return Observable.<String>create(s -> {
            logger.info("Start: Executing slow task in Service 4");
            Util.delay(1000);
            s.onNext(s1 + "data 4");
            logger.info("End: Executing slow task in Service 4");
            s.onCompleted();
        }).subscribeOn(Schedulers.computation());
    }


    public Observable<MyBuffer> operation(MyBuffer b) {
        return Observable.<MyBuffer>create(s -> {
            logger.info("Start: Executing slow task in Service 4");
            Util.delay(5000);
            b.append(" data4 ");
            logger.info("End: Executing slow task in Service 4");
            s.onNext(b);
            s.onCompleted();
        }).subscribeOn(Schedulers.computation());
    }

}