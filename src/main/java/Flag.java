public class Flag {
    private boolean flag;

    public Flag(boolean initial) {
        this.flag = initial;
    }
    public boolean isTure() {
        return flag;
    }

    public void toggleTure() {
        flag = true;
    }

    public void toggleFalse() {
        flag = false;
    }
}