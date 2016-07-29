package de.claas.mosis.io;

import de.claas.mosis.util.Utils;
import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * The JUnit test for class {@link de.claas.mosis.io.BlockingQueueHandler}. It
 * is intended to collect and document a set of test cases for the tested class.
 * Please refer to the individual tests for more detailed information.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class BlockingQueueHandlerTest extends DataHandlerTest<Long, BlockingQueueHandler<Long>> {

    @Override
    protected BlockingQueueHandler<Long> build() throws Exception {
        return new BlockingQueueHandler<>();
    }

    @Test
    public void assumptionsOnClass() throws Exception {
        assertEquals(LinkedBlockingQueue.class.getName(),
                _H.getParameter(BlockingQueueHandler.CLASS));
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterClassMayNotBeNull() throws Exception {
        Utils.updateParameter(_H, BlockingQueueHandler.CLASS, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterClassMustExist() throws Exception {
        Utils.updateParameter(_H, BlockingQueueHandler.CLASS, BlockingQueueHandler.class.getName() + "SomePostfix");
    }

    @Test
    public void shouldInstantiateNewClass() throws Exception {
        Utils.updateParameter(_H, BlockingQueueHandler.CLASS, LinkedTransferQueue.class.getName());
        assertNotNull(_H.getQueue());
        assertEquals(LinkedTransferQueue.class, _H.getQueue().getClass());
    }

    @Test
    public void shouldHaveInitializedQueue() {
        assertNotNull(_H.getQueue());
    }

    @Override
    public void shouldRead() throws Exception {
        Utils.updateParameter(_H, DataHandler.MODE, DataHandler.MODE_READ);

        assertTrue(_H.getQueue().isEmpty());
        _H.getQueue().offer(23L);
        _H.getQueue().offer(42L);
        assertEquals((Long) 23L, Utils.process(_H, 1L, 2L));
        assertEquals((Long) 42L, Utils.process(_H, 23L, null));
        assertTrue(_H.getQueue().isEmpty());
        _H.getQueue().offer(9L);
        assertEquals((Long) 9L, Utils.process(_H));
        assertTrue(_H.getQueue().isEmpty());
    }

    @Override
    public void shouldWrite() throws Exception {
        Utils.updateParameter(_H, DataHandler.MODE, DataHandler.MODE_WRITE);

        assertEquals(Arrays.asList(23L, 1L), Utils.processAll(_H, 23L, 1L));
        assertEquals((Long) 23L, _H.getQueue().poll());
        assertEquals((Long) 1L, _H.getQueue().poll());
        assertEquals(Arrays.asList(42L, 444L), Utils.processAll(_H, 42L, 444L));
        assertEquals((Long) 42L, _H.getQueue().poll());
        assertEquals((Long) 444L, _H.getQueue().poll());
        assertTrue(_H.getQueue().isEmpty());
    }

    @Override
    public void shouldDetermineMode() throws Exception {
        Utils.updateParameter(_H, DataHandler.MODE, DataHandler.MODE_AUTO);

        assertTrue(_H.getQueue().isEmpty());
        _H.getQueue().offer(10L);
        assertEquals((Long) 10L, Utils.process(_H));
        assertEquals(Arrays.asList(20L, 6454L),
                Utils.processAll(_H, 20L, 6454L));
        assertEquals((Long) 20L, _H.getQueue().poll());
        assertEquals((Long) 6454L, _H.getQueue().poll());
        assertTrue(_H.getQueue().isEmpty());
    }

    @Test
    public void shouldBlock() throws InterruptedException {
        Utils.updateParameter(_H, DataHandler.MODE, DataHandler.MODE_WRITE);

        assertEquals((Long) 23L, Utils.process(_H, 23L));
        assertEquals((Long) 23L, _H.getQueue().poll());
        assertNull(_H.getQueue().poll(500, TimeUnit.MILLISECONDS));
    }

    @Test(expected = NullPointerException.class)
    public void shouldNotAcceptNullValues() {
        Utils.updateParameter(_H, DataHandler.MODE, DataHandler.MODE_WRITE);
        Utils.process(_H, (Long[])null);
    }

}
