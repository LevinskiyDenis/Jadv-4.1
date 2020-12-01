public class Call {

    String name;
    static int numberOfCalls;

    public Call() {
        numberOfCalls += 1;
        this.name = "Пользователь " + numberOfCalls;
    }

    public String getName() {
        return name;
    }
}
