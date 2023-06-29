import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static volatile int variableForThree = 0;
    public static volatile int variableForFour = 0;
    public static volatile int variableForFive = 0;

    public static void main(String[] args) throws InterruptedException {

        Random random = new Random();
        String[] texts = new String[100000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        CountName countName1 = new CountName();
        CountName countName2 = new CountName();
        CountName countName3 = new CountName();

        //variableForThree
        int lenth3 = 3;
        Thread thread1 = new Thread(() -> countName1.beautifulWords(texts, lenth3));
        thread1.start();

        //variableForFour
        int lenth4 = 4;
        Thread thread2 = new Thread(() -> countName2.beautifulWords(texts, lenth4));
        thread2.start();

        //variableForFive
        int lenth5 = 5;
        Thread thread3 = new Thread(() -> countName3.beautifulWords(texts, lenth5));
        thread3.start();

        thread1.join();
        thread2.join();
        thread3.join();

        variableForThree = countName1.getWords();
        variableForFour = countName2.getWords();
        variableForFive = countName3.getWords();

        System.out.format("Красивых слов с длиной 3: %d шт\n" +
                        "Красивых слов с длиной 4: %d шт\n" +
                        "Красивых слов с длиной 5: %d шт",
                variableForThree,
                variableForFour,
                variableForFive);
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}

class CountName {
    AtomicInteger countName = new AtomicInteger(0);

    public void beautifulWords(String[] texts, int length) {
        for (String name : texts) {
            if (name.length() == length) {

                if (name.chars().distinct().count() == 1) {
                    countName.incrementAndGet();
                } else if (name.charAt(0) == name.charAt(length - 1)) {
                    for (int j = 0; j < (name.length() / 2); j++) {
                        if (name.charAt(j) != name.charAt(name.length() - j - 1)) {
                            countName.incrementAndGet();
                        }
                    }
                } else {
                    for (int i = 1; i < name.length(); i++) {
                        if (name.charAt(i) == name.charAt(i - 1) || name.charAt(i) < name.charAt(i - 1)) {
                            countName.incrementAndGet();
                        }
                    }
                }
            }
        }
    }

    public int getWords() {
        return countName.get();
    }
}