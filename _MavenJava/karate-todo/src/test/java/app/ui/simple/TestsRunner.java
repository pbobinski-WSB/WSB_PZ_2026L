package app.ui.simple;

import com.intuit.karate.junit5.Karate;

class TestsRunner {
    
    @Karate.Test
    Karate testGoogle() {
        return Karate.run("google").relativeTo(getClass());
    }

    @Karate.Test
    Karate testSimple() {
        return Karate.run("simple").relativeTo(getClass());
    }


}
