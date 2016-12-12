import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import java.util.Arrays;
import java.util.List;

public class Service5 {
    private static final Logger logger = LoggerFactory.getLogger(Service5.class);

//    public Observable<MyBuffer> operation(MyBuffer b) {
//        return Observable.<MyBuffer>create(s -> {
//            logger.info("Start: Executing slow task in Service 4");
//            Util.delay(5000);
//            b.append(" data4 ");
//            logger.info("End: Executing slow task in Service 4");
//            s.onNext(b);
//            s.onCompleted();
//        }).subscribeOn(Schedulers.computation());
//    }

    public Observable<List<MyProduct>> fetchProducts() {
        return Observable.<List<MyProduct>>create(s -> {
            logger.info("Start: Fetch products");
            Util.delay(5000);
            logger.info("End: Executing slow task in Service 4");

            s.onNext(Arrays.asList(new MyProduct("Granulat"), new MyProduct("Filler"), new MyProduct("Bitume")));
            s.onCompleted();
        }).subscribeOn(Schedulers.computation());
    }

    public Observable<MyProduct> populateProduct(MyProduct product) {
        return Observable.<MyProduct>create(s -> {
            logger.info("Start: Fetch product");
            Util.delay(2000);
            logger.info("End: Fetch product");

            product.setType("T1");
            s.onNext(product);
            s.onCompleted();
        }).subscribeOn(Schedulers.computation());
    }
}