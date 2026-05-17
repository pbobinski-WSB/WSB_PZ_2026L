package mockito;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HelloTest {

    Hello hello;

    @BeforeEach
        void setUp() {
         hello = new Hello();
    }

    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Mock
    private List<String> mockList;

    @Test
    public void test() {
        String expected = "mockito.Hello world !";
        when(mockList.get(0)).thenReturn(expected);

        String actual = mockList.get(0);

        Assertions.assertEquals(hello.hello(),actual);
    }

    @Mock
    private Iterator<String> iterator;

    @Test
    public void testIterator() {

        when(iterator.next()).thenReturn("mockito.Hello").thenReturn("world").thenReturn("!");

        String result=iterator.next()+" "+iterator.next()+" "+iterator.next();
        Assertions.assertEquals(hello.hello(),result);

    }



}