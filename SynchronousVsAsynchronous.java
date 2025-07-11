//Synchronous
public String getUserData() {
    // Simulate waiting for server response
    Thread.sleep(3000); // Waits for 3 seconds
    return "User data";
}

public static void main(String[] args) {
    System.out.println("Fetching data...");
    String data = getUserData(); // Program waits here
    System.out.println("Received: " + data);
}
// output:-
// Fetching data...
// (3-second pause)
// Received: User data

//Asynchronous
import reactor.core.publisher.Mono;

public class AsyncExample {
    public static void main(String[] args) {
        Mono<String> userData = Mono.fromSupplier(() -> {
            try {
                Thread.sleep(3000); // Simulate delay
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "User data";
        });

        System.out.println("Fetching data...");
        userData.subscribe(data -> System.out.println("Received: " + data));
        System.out.println("Program continues...");
    }
}
// output:-
// Fetching data...
// Program continues...
// (3-second pause)
// Received: User data


// 🧠 Key Difference

// | Behavior        | Synchronous               | Asynchronous / Reactive         |
// |---------------- |---------------------------|---------------------------------|
// | Waits for result | Yes                      | No                              |
// | Program flow    | Pauses                    | Continues immediately           |
// | When result appears | After pause           | Whenever it's ready             |


