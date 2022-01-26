package com.gmy.sky2.helper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

@Slf4j
@Component
public class TransactionHelper {

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = RuntimeException.class)
    public <T> T propagationWithNew(Supplier<T> supplier) {
        return supplier.get();
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public <T> T propagationDefault(Supplier<T> supplier) {
        return supplier.get();
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void propagationDefault(Runnable runnable) {
        runnable.run();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = RuntimeException.class)
    public void propagationWithNew(Runnable runnable) {
        runnable.run();
    }

    @Transactional(propagation = Propagation.NESTED, rollbackFor = RuntimeException.class)
    public void propagationWithNested(Runnable runnable) {
        runnable.run();
    }

    @Transactional(propagation = Propagation.NESTED, rollbackFor = RuntimeException.class)
    public <T> void propagationWithNested(Supplier<T> supplier) {
        supplier.get();
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = RuntimeException.class)
    public <T> T propagationWithNotSupported(Supplier<T> supplier) {
        return supplier.get();
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = RuntimeException.class)
    public void propagationWithNotSupported(Runnable runnable) {
        runnable.run();
    }
}