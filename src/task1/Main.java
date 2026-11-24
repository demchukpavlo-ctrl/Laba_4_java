package task1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import util.IO;

/**
 * Завдання 1, варіант 1.
 * Зчитати рядки з файлу, вилучити усі порожні рядки (ті, які не містять символів
 * або складаються лише з пробілів). Записати у файл, що задається користувачем
 * з консолі, зчитані рядки, попередньо вилучивши з усіх рядків символи, які не є
 * маленькими латинськими літерами, і відсортувати їх.
 */
public class Main {

    /** Залишає в рядку лише маленькі латинські літери a..z. */
    static String keepLowercaseLatin(String line) {
        StringBuilder sb = new StringBuilder(line.length());
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c >= 'a' && c <= 'z') {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        run();
        IO.out.flush();
    }

    public static void run() {
        IO.out.println("=== Завдання 1. Варіант 1: обробка рядків файлу ===");
        try {
            // 1. Послідовне читання рядків у контейнер ArrayList
            List<String> lines = new ArrayList<>();
            try (BufferedReader reader = IO.openInputFile("Введіть назву вхідного файлу: ")) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            }
            IO.out.println("Прочитано рядків: " + lines.size());

            // 2. Вилучення порожніх рядків (порожні або лише з пробілів)
            lines.removeIf(String::isBlank);
            IO.out.println("Непорожніх рядків: " + lines.size());

            // 3. Вилучення з рядків усіх символів, крім a..z
            List<String> result = new ArrayList<>();
            int becameEmpty = 0;
            for (String line : lines) {
                String cleaned = keepLowercaseLatin(line);
                if (cleaned.isEmpty()) {
                    becameEmpty++;      // рядок не містив жодної маленької латинської літери
                } else {
                    result.add(cleaned);
                }
            }
            if (becameEmpty > 0) {
                IO.out.println("Рядків без маленьких латинських літер (не записуються): " + becameEmpty);
            }

            // 4. Сортування
            Collections.sort(result);

            IO.out.println("\nРезультат:");
            for (String s : result) {
                IO.out.println("  " + s);
            }

            // 5. Послідовний запис у файл
            try (BufferedWriter writer = IO.openOutputFile("Введіть назву вихідного файлу: ")) {
                for (String s : result) {
                    writer.write(s);
                    writer.newLine();
                }
            }
            IO.out.println("Записано рядків у файл: " + result.size());
        } catch (IOException e) {
            IO.out.println("Помилка введення-виведення: " + e.getMessage());
        }
    }
}
