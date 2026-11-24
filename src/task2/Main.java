package task2;

import java.io.BufferedWriter;
import java.io.IOException;

import util.IO;

/**
 * Завдання 2, варіант 5.
 * Додаток, що виконує посимвольний запис з консолі у файл.
 * Закінчення введення — Ctrl + Z.
 */
public class Main {

    public static void main(String[] args) {
        run();
        IO.out.flush();
    }

    public static void run() {
        IO.out.println("=== Завдання 2. Варіант 5: посимвольний запис з консолі у файл ===");
        try (BufferedWriter writer = IO.openOutputFile("Введіть назву файлу для запису: ")) {
            IO.out.println("Вводьте текст. Для завершення натисніть Ctrl+Z на початку рядка, потім Enter.");
            IO.out.println("(у консолі IntelliJ IDEA замість Ctrl+Z використовується Ctrl+D)");

            int count = 0;
            int c;
            // читаємо по одному символу, доки не отримаємо кінець потоку (Ctrl+Z)
            while ((c = IO.in.read()) != -1) {
                if (c == '\r') {
                    continue;           // переведення рядка записуємо через newLine()
                }
                if (c == '\n') {
                    writer.newLine();
                } else {
                    writer.write(c);
                }
                count++;
            }
            IO.out.println("Введення завершено. Записано символів: " + count);
        } catch (IOException e) {
            IO.out.println("Помилка введення-виведення: " + e.getMessage());
        }
    }
}
