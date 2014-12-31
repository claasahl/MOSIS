package de.claas.mosis.io;

import de.claas.mosis.util.Utils;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * The JUnit test for class {@link QueueHandler}. It is intended to collect and
 * document a set of test cases for the tested class. Please refer to the
 * individual tests for more detailed information.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class QueueHandlerTest extends DataHandlerTest<Long, QueueHandler<Long>> {

    @Override
    protected QueueHandler<Long> build() throws Exception {
        return new QueueHandler<>();
    }

    @Override
    public void shouldRead() throws Exception {
        _H.setParameter(DataHandler.MODE, DataHandler.MODE_READ);

        assertTrue(_H.getQueue().isEmpty());
        _H.getQueue().offer(23L);
        _H.getQueue().offer(42L);
        assertEquals((Long) 23L, Utils.process(_H, 1L, 2L));
        assertEquals((Long) 42L, Utils.process(_H, 23L, null));
        assertNull(Utils.process(_H));
        _H.getQueue().offer(9L);
        assertEquals((Long) 9L, Utils.process(_H));
        assertTrue(_H.getQueue().isEmpty());
    }

    @Override
    public void shouldWrite() throws Exception {
        _H.setParameter(DataHandler.MODE, DataHandler.MODE_WRITE);

        assertNull(Utils.process(_H, (Long) null));
        assertNull(_H.getQueue().poll());
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
        _H.setParameter(DataHandler.MODE, DataHandler.MODE_AUTO);

        assertTrue(_H.getQueue().isEmpty());
        _H.getQueue().offer(10L);
        assertEquals((Long) 10L, Utils.process(_H));
        assertEquals(Arrays.asList(20L, 6454L),
                Utils.processAll(_H, 20L, 6454L));
        assertEquals((Long) 20L, _H.getQueue().poll());
        assertEquals((Long) 6454L, _H.getQueue().poll());
        assertTrue(_H.getQueue().isEmpty());
    }

}
