import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.schedulers.Schedulers;

public class Service1 {
    private static final Logger logger = LoggerFactory.getLogger(Service1.class);
    public Observable<String> operation() {
        return Observable.<String>create(s -> {
            logger.info("Start: Executing slow task in Service 1");
            Util.delay(3000);
            s.onNext("data 1");
            logger.info("End: Executing slow task in Service 1");
            s.onCompleted();
        });
    }
    public Observable<String> operation(String st) {
        return Observable.<String>create(s -> {
            logger.info("Start: Executing slow task in Service 1");
            Util.delay(3000);
            s.onNext(st + "data 1");
            logger.info("End: Executing slow task in Service 1");
            s.onCompleted();
        });
    }
    public Observable<MyBuffer> operationBuf() {
        return Observable.<MyBuffer>create(s -> {
            logger.info("Start: Executing slow task in Service 1");
            Util.delay(3000);
            MyBuffer b0 = new MyBuffer();
            b0.append(" data1 ");
            logger.info("End: Executing slow task in Service 1");
            s.onNext(b0);
            s.onCompleted();
        }).subscribeOn(Schedulers.computation());
    }
}