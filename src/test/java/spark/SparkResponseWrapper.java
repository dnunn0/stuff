package spark;

public class SparkResponseWrapper extends Response {

    public SparkResponseWrapper() {
        super(new FakeHttpServletRequest());
    }

}

