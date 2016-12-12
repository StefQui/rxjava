import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.functions.Func1;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by stef on 10/12/16.
 */
public class asyncrx {


    private static final Logger logger = LoggerFactory.getLogger(asyncrx.class);

    private Service1 service1 = new Service1();
    private Service2 service2 = new Service2();
    private Service3 service3 = new Service3();
    private Service4 service4 = new Service4();
    private Service5 service5 = new Service5();

    @Test
    public void rx4() {

        Observable<String> ob1 = service1.operation();
        ob1.subscribe(s -> System.out.println("Subst: " + s));
    }

    @Test
    public void testBasicIntervalsObs() throws Exception {
        long start = System.nanoTime();

        Observable<String> op1 = service1.operation();
        Observable<String> op2 = service2.operation();
        Observable<String> op3 = service3.operation("kkk");

        Observable<List<String>> lst = Observable.merge(op1, op2, op3).toList();

        lst.toBlocking().forEach(l -> logger.info("for each :" + l.toString()));
        long end = System.nanoTime();
        logger.info("time taken: " + (end - start) / (1000 * 1000) + " ms");
    }

    @Test
    public void testConcat() throws Exception {
        long start = System.nanoTime();

        Observable<String> op1 = service1.operation();
        Observable<String> op2 = service2.operation();
        Observable<String> op3 = service3.operation("llll");

        Observable<String> all = Observable.concat(op1, op2, op3);
        all.toBlocking().subscribe(s -> logger.info("s=" + s));

//        lst.toBlocking().forEach(l -> logger.info("for each :" + l.toString()));
        long end = System.nanoTime();
        logger.info("time taken: " + (end - start) / (1000 * 1000) + " ms");
    }

    @Test
    public void testConcat2() throws Exception {
        long start = System.nanoTime();

        Observable<String> op1 = service1.operation();
        Observable<String> op2 = service2.operation();
        Observable<String> op3 = service3.operation("ppp");

        op1.concatMap(new Func1<String, Observable<?>>() {
            @Override
            public Observable<?> call(String s) {
                return op2
                        .map(a -> s + " and " + a)
                        .concatMap(new Func1<String, Observable<?>>() {
                            @Override
                            public Observable<?> call(String s) {
                                return op3.map(a -> s + " and also " + a);
                            }
                        });
            }
        }).toBlocking().subscribe(s -> logger.info("s=" + s));

//        lst.toBlocking().forEach(l -> logger.info("for each :" + l.toString()));
        long end = System.nanoTime();
        logger.info("time taken: " + (end - start) / (1000 * 1000) + " ms");
    }


    @Test
    public void testConcat3() throws Exception {
        long start = System.nanoTime();

        String s1 = "before ";
        Observable<String> op1 = service1.operation(s1);
        Observable<String> op2 = service2.operation(s1);
        Observable<String> op3 = service3.operation(s1);
        Observable<String> op4 = service4.operation(s1);


        Observable<List<String>> lst = Observable.merge(op3, op4).toList();


        service1.operationBuf()
                .map(b2 -> b2== null ? new MyBuffer() : b2)
                .doOnNext(b2 -> logger.info(" SEND S1 s=" + b2.getMessage()))
                .concatMap(b3 -> service2.operation(b3))
                .doOnNext(b4 -> logger.info(" SEND S2 s=" + b4.getMessage()))
                .concatMap(
                        b3 -> Observable.merge(
                                service3.operation(b3)
                                        .doOnNext(b4 -> logger.info(" SEND S3 s=" + b4.getMessage())),
                                service4.operation(b3)
                                        .doOnNext(b4 -> logger.info(" SEND S4 s=" + b4.getMessage())),
                                Observable.just(b3)
                                        .concatMap(new Func1<MyBuffer, Observable<MyBuffer>>() {
                                            @Override
                                            public Observable<MyBuffer> call(MyBuffer buf) {
                                                service5.fetchProducts()
                                                        .doOnNext(myProducts -> buf.setProducts(myProducts))
                                                        .flatMap(myProducts -> Observable.from(myProducts))
                                                        .flatMap(myProduct -> service5.populateProduct(myProduct)).toBlocking().subscribe();

                                                logger.info("  products s=" + buf.getProducts());

                                                return Observable.just(buf);
                                            }
                                        }).doOnNext(b4 -> logger.info(" SEND S5 s=" + b4.getMessage() + " " + b4.getProducts()))
                                )
                                .toList()
                )
                .doOnNext(b3 -> {
                    StringBuilder buf = new StringBuilder();
                    b3.stream().map(bu -> bu.getMessage()).forEach(buf::append);
                    logger.info(" HI " + b3.get(0).getMessage() + " products-> " + b3.get(0).getProducts());
                })
                .toBlocking()
                .subscribe(s -> {
                    s.stream().map(bu -> bu.getMessage()).forEach(a -> logger.info("  finally s=" + a));
                });

//        lst.toBlocking().forEach(l -> logger.info("for each :" + l.toString()));
        long end = System.nanoTime();
        logger.info("time taken: " + (end - start) / (1000 * 1000) + " ms");
    }

    @Test
    public void testProduct() throws Exception {

        MyBuffer buf = new MyBuffer();

        service5.fetchProducts()
                .flatMap(products -> {
                    buf.setProducts(products);
                    return Observable.from(products)
                            .flatMap(product -> service5.populateProduct(product));
                })
                .toBlocking()
                .subscribe(s -> {
                    logger.info("  finally s=" + s);
                });;


        logger.info("MyBuffer.products : " + buf.getProducts());



    }

    @Test
    public void testProduct2() throws Exception {

        MyBuffer buf1 = new MyBuffer();

        Observable.just(buf1)
                .map(buf -> {
                    buf.setMessage("toto");

                            return Observable.just(buf);
                        })
                .subscribe(s -> {
                    logger.info("  finally s=" + buf1.getMessage());
                });
        Observable.just(buf1)
                .concatMap(new Func1<MyBuffer, Observable<MyBuffer>>() {
                    @Override
                    public Observable<MyBuffer> call(MyBuffer buf) {
                        service5.fetchProducts()
                                .doOnNext(myProducts -> buf.setProducts(myProducts))
                                .flatMap(myProducts -> Observable.from(myProducts))
                                .map(myProduct -> service5.populateProduct(myProduct))
                                .toBlocking()
                                .subscribe();
                        logger.info("  products s=" + buf.getProducts());

                        return Observable.just(buf);
                    }
                }).subscribe(s -> {
            logger.info("  finally s=" + buf1.getProducts());
        });

//        Observable.just(buf1).switchOnNext(service5.fetchProducts());
//                service5.fetchProducts())
//                .reduce((buf, list) -> buf1.setProducts((List<MyProduct>) list));
//                .map(buf -> {
//                    return Observable.just(buf).switchMap(service5.fetchProducts());
//
//
////                    return Observable.just(buf).concatWith(service5.fetchProducts()
////                            .doOnNext(myProducts -> buf.setProducts(myProducts))
////                            .flatMap(myProducts -> Observable.from(myProducts))
////                            .map(myProduct -> service5.populateProduct(myProduct)));
//                })
//                .toBlocking()
//                .subscribe(s -> {
//                    logger.info("  finally s=" + buf1.getProducts());
//                });

//        Observable.just(buf)
//                .switchMap(new Func1<MyBuffer, Observable<MyBuffer>>() {
//                    @Override
//                    public Observable<MyBuffer> call(MyBuffer myBuffer) {
//                        service5.fetchProducts()
//                                .doOnNext(myProducts -> myBuffer.setProducts(myProducts))
//                                .flatMap(myProducts -> Observable.from(myProducts))
//                                .map(myProduct -> service5.populateProduct(myProduct));
//                            Observable.just(myBuffer)
//                            .map(buf -> service5.fetchProducts()
//                                    .doOnNext(myProducts -> myBuffer.setProducts(myProducts))
//                                    .flatMap(myProducts -> Observable.from(myProducts))
//                                    .map(myProduct -> service5.populateProduct(myProduct)));
//                        });
//                    }
//                }).subscribe(s -> {
//                    logger.info("  finally s=" + buf.getProducts());
//                });


//                .flatMap(new Func1<MyBuffer, Observable<MyBuffer>>() {
//                    @Override
//                    public Observable<MyBuffer> call(MyBuffer myBuffer) {
//                        service5.fetchProducts()
//                                .flatMap(products -> {
//                                    myBuffer.setProducts(products);
//                                    return Observable.from(products)
//                                            .flatMap(product -> service5.populateProduct(product));
//                                });
//                        return Observable.just(myBuffer);
//                    }
//                }).subscribe(s -> {
//            logger.info("  finally s=" + buf.getProducts());
//        });
//        service5.fetchProducts()
//                .flatMap(products -> {
//                    buf.setProducts(products);
//                    return Observable.from(products)
//                            .flatMap(product -> service5.populateProduct(product));
//                })
//                .toBlocking()
//                .subscribe(s -> {
//                    logger.info("  finally s=" + s);
//                });;


        logger.info("MyBuffer.products : " + buf1.getProducts());



    }


        @Test
    public void testFlatmap() throws Exception {
        Observable.range(1, 10)
                .flatMap(v -> Observable.just(v).delay(11 - v, TimeUnit.SECONDS))
                .toBlocking()
                .subscribe(System.out::println);


    }

}
