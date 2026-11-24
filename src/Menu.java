import java.io.IOException;

import util.IO;

/**
 * Головне меню лабораторної роботи №4.
 * Дозволяє обрати, яке завдання запустити.
 */
public class Menu {

    public static void main(String[] args) throws IOException {
        while (true) {
            IO.out.println();
            IO.out.println("   Лабораторна робота №4. Файли.");
            IO.out.println(" 1 - Завдання 1 (варіант 1): обробка рядків файлу");
            IO.out.println(" 2 - Завдання 2 (варіант 5): посимвольний запис з консолі у файл");
            IO.out.println(" 0 - Вихід");

            String choice = IO.readLine("Ваш вибір: ");
            if (choice == null) {
                break;
            }
            IO.out.println();

            switch (choice) {
                case "1":
                    task1.Main.run();
                    break;
                case "2":
                    task2.Main.run();
                    break;
                case "0":
                    IO.out.println("До побачення!");
                    return;
                default:
                    IO.out.println("Невірний вибір, введіть 0, 1 або 2.");
            }
        }
        IO.out.flush();
    }
}
