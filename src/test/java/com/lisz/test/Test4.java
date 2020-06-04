package com.lisz.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.validation.constraints.AssertTrue;
import java.util.concurrent.*;

//@RunWith(PowerMockRunner.class)
public class Test4 {
    private TraceContextAwareExecutorService service = new TraceContextAwareExecutorService(null, null);

    @Mock
    private TraceContextAwareExecutorService mockService;

    public Test4() {
        mockService = PowerMockito.mock(TraceContextAwareExecutorService.class);
        Mockito.when(mockService.getCurrentSegment()).thenThrow(new RuntimeException("ERROR"));
    }


    @Test
    public void testGetCurrentSegment() throws ExecutionException, InterruptedException {
        Callable<String> callable = service.getCurrentSegment();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<String> future = executorService.submit(callable);
        Assert.assertEquals("abc", future.get());
    }

    @Test
    public void testGetCurrentSegmentWithException() throws ExecutionException, InterruptedException {

        try {
            mockService.getCurrentSegment();
        } catch (Exception e) {
            Assert.assertTrue(e.getClass().getSimpleName().equals("RuntimeException"));
            Assert.assertEquals("ERROR", e.getMessage());
        }
    }
}
