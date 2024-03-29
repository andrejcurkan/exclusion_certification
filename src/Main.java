import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Введите данные в формате: Фамилия Имя Отчество дата_рождения номер_телефона пол");
            String inputData = scanner.nextLine();
            processData(inputData);
        } catch (InvalidDataFormatException | IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public static void processData(String inputData) throws InvalidDataFormatException, IOException {
        String[] parts = inputData.split("\\s+");
        if (parts.length != 6) {
            throw new InvalidDataFormatException("Неверное количество данных. Введите Фамилию Имя Отчество " +
                    "дата_рождения номер_телефона пол, разделенные пробелом.");
        }

        String surname = parts[0];
        String name = parts[1];
        String patronymic = parts[2];
        String birthDateStr = parts[3];
        String phoneNumberStr = parts[4];
        String genderStr = parts[5];

        LocalDate birthDate;
        try {
            birthDate = LocalDate.parse(birthDateStr, Constants.DATE_FORMAT);
        } catch (DateTimeParseException e) {
            throw new InvalidDataFormatException("Неверный формат даты рождения. Используйте формат dd.mm.yyyy.");
        }

        long phoneNumber;
        try {
            phoneNumber = Long.parseLong(phoneNumberStr);
        } catch (NumberFormatException e) {
            throw new InvalidDataFormatException("Неверный формат номера телефона. Введите целое беззнаковое число.");
        }

        if (!genderStr.equals("m") && !genderStr.equals("f")) {
            throw new InvalidDataFormatException("Неверное значение пола. Используйте 'm' или 'f'.");
        }

        String filename = surname + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            String dataLine = String.format("%s %s %s %s %d %s%n", surname, name, patronymic, birthDateStr, phoneNumber, genderStr);
            writer.write(dataLine);
        } catch (IOException e) {
            throw new IOException("Ошибка при записи данных в файл: " + filename, e);
        }
    }
}

class Constants {
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");
}

class InvalidDataFormatException extends Exception {
    public InvalidDataFormatException(String message) {
        super(message);
    }
}
