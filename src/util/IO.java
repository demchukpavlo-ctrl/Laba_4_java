package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Допоміжний клас для буферизованого консольного та файлового введення-виведення.
 * Відкриття вхідних і вихідних файлів з перевірками згідно з додатковими зауваженнями
 * до лабораторної роботи.
 */
public final class IO {

    /** Кодування текстових файлів. */
    public static final Charset FILE_CHARSET = StandardCharsets.UTF_8;

    /** Буферизоване введення з консолі. */
    public static final BufferedReader in =
            new BufferedReader(new InputStreamReader(System.in, consoleInputCharset()));

    /** Буферизоване виведення на консоль (println скидає буфер автоматично). */
    public static final PrintWriter out =
            new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out, System.out.charset())), true);

    private IO() {
    }

    private static Charset consoleInputCharset() {
        String enc = System.getProperty("stdin.encoding");
        try {
            if (enc != null) {
                return Charset.forName(enc);
            }
        } catch (RuntimeException ignored) {
            // невідоме кодування — використовуємо стандартне
        }
        return System.console() != null ? System.console().charset() : Charset.defaultCharset();
    }

    /** Виводить підказку і зчитує рядок з консолі; null — якщо введення закінчилось. */
    public static String readLine(String prompt) throws IOException {
        out.print(prompt);
        out.flush();
        String line = in.readLine();
        return line == null ? null : line.trim();
    }

    /** Відповідь так/ні. */
    public static boolean askYes(String prompt) throws IOException {
        while (true) {
            String a = readLine(prompt + " (так/ні): ");
            if (a == null) {
                return false;
            }
            a = a.toLowerCase();
            if (a.equals("так") || a.equals("т") || a.equals("y") || a.equals("yes") || a.equals("1")) {
                return true;
            }
            if (a.equals("ні") || a.equals("н") || a.equals("n") || a.equals("no") || a.equals("0")) {
                return false;
            }
            out.println("  Введіть \"так\" або \"ні\".");
        }
    }

    /**
     * Запитує ім'я вхідного файлу. Якщо файлу немає — виводить повідомлення
     * і повторює запит. Повертає буферизований потік для послідовного читання.
     */
    public static BufferedReader openInputFile(String prompt) throws IOException {
        while (true) {
            String name = readLine(prompt);
            if (name == null) {
                throw new IOException("Введення перервано");
            }
            if (name.isEmpty()) {
                continue;
            }
            File file = new File(name);
            if (!file.isFile()) {
                out.println("  Файл \"" + file.getAbsolutePath() + "\" не знайдено. Спробуйте ще раз.");
                continue;
            }
            try {
                return new BufferedReader(new InputStreamReader(new FileInputStream(file), FILE_CHARSET));
            } catch (FileNotFoundException e) {
                out.println("  Не вдалося відкрити файл: " + e.getMessage());
            }
        }
    }

    /**
     * Запитує ім'я вихідного файлу.
     * Якщо файлу немає — повідомляє про це і пропонує його створити.
     * Якщо файл існує — пропонує перезаписати його або дописати в кінець.
     */
    public static BufferedWriter openOutputFile(String prompt) throws IOException {
        while (true) {
            String name = readLine(prompt);
            if (name == null) {
                throw new IOException("Введення перервано");
            }
            if (name.isEmpty()) {
                continue;
            }
            File file = new File(name);
            if (file.isDirectory()) {
                out.println("  \"" + name + "\" — це каталог, а не файл.");
                continue;
            }

            boolean append = false;
            if (!file.exists()) {
                out.println("  Файл \"" + file.getAbsolutePath() + "\" не існує.");
                if (!askYes("  Створити файл?")) {
                    continue;
                }
                File parent = file.getAbsoluteFile().getParentFile();
                if (parent != null && !parent.exists() && !parent.mkdirs()) {
                    out.println("  Не вдалося створити каталог " + parent);
                    continue;
                }
                if (!file.createNewFile()) {
                    out.println("  Не вдалося створити файл.");
                    continue;
                }
                out.println("  Файл створено.");
            } else {
                while (true) {
                    String c = readLine("  Файл існує. 1 - перезаписати, 2 - дописати в кінець: ");
                    if ("1".equals(c)) {
                        break;
                    }
                    if ("2".equals(c)) {
                        append = true;
                        break;
                    }
                    if (c == null) {
                        throw new IOException("Введення перервано");
                    }
                }
            }
            try {
                return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, append), FILE_CHARSET));
            } catch (FileNotFoundException e) {
                out.println("  Не вдалося відкрити файл для запису: " + e.getMessage());
            }
        }
    }
}
