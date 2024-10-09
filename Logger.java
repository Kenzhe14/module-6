import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Logger {

    public enum LogLevel {
        INFO, WARNING, ERROR; // Уровни логирования
    }

    private static Logger logger = null; // Singleton instance
    private String filePath = "C:/logs/file.txt"; // Путь к файлу для логов

    private Logger() {
        try {
            Path directoryPath = Paths.get("C:/logs"); // Путь к папке логов
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath); // Создание папки, если её нет
            }
        } catch (IOException e) {
            e.printStackTrace(); // Обработка исключений
        }
    }

    public static Logger getInstance() {
        if (logger == null) {
            synchronized (Logger.class) { // Синхронизация для потокобезопасности
                if (logger == null) {
                    logger = new Logger(); // Инициализация логгера
                }
            }
        }
        return logger;
    }

    private LogLevel currentLogLevel = LogLevel.INFO; // Текущий уровень логирования

    public synchronized void log(String message, LogLevel level) { // Метод для логирования
        if (shouldLog(level)) { // Проверка уровня логирования
            try {
                Files.writeString(Paths.get(filePath), level + " ! " + message + System.lineSeparator(),
                        StandardOpenOption.CREATE, StandardOpenOption.APPEND); // Запись сообщения в файл
            } catch (IOException e) {
                e.printStackTrace(); // Обработка исключений при записи
            }
        }
    }

    public void setLogLevel(LogLevel level) { // Установка уровня логирования
        this.currentLogLevel = level;
    }

    private boolean shouldLog(LogLevel level) { // Метод для проверки, нужно ли логировать сообщение
        return level.ordinal() >= currentLogLevel.ordinal();
    }

}

class Main {
    public static void main(String[] args) {
        Logger logger = Logger.getInstance(); // Получение экземпляра логгера
        logger.setLogLevel(Logger.LogLevel.INFO); // Установка уровня INFO
        logger.log("INFO ", Logger.LogLevel.INFO); // Логирование сообщения уровня INFO

        logger.setLogLevel(Logger.LogLevel.WARNING); // Установка уровня WARNING
        logger.log("WARNING ", Logger.LogLevel.WARNING); // Логирование сообщения уровня WARNING

        logger.setLogLevel(Logger.LogLevel.ERROR); // Установка уровня ERROR
        logger.log("ERROR ", Logger.LogLevel.ERROR); // Логирование сообщения уровня ERROR
    }
}
