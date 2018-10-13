package b.com.myapplication.utils;

public class MessageEvent {
    private int what;
    private Object obj;

    public MessageEvent(int what) {
        this.what = what;
    }

    public MessageEvent(int what,Object obj) {
        this.obj = obj;
        this.what = what;
    }

    public Object getMessage() {
        return obj;
    }

    public void setMessage(Object obj) {
        this.obj = obj;
    }

    public int getWhat() {
        return what;
    }

    public void setWhat(int what) {
        this.what = what;
    }
}
