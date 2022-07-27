import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Logic {
    private Scanner in;
    private ConnectDB db;

    Logic() {
        in = new Scanner(System.in);
        db = new ConnectDB();
    }

    private int readInt(int min, int max) {
        int num = 0;
        String buf = "";
        buf = in.nextLine();
        try {
            num = Integer.parseInt(buf);
            if (num < min || num > max) {
                throw new InputMismatchException();
            }
        } catch (Exception ex) {
            System.out.println("Некорректный ввод! Попробуйте снова.");
            return readInt(min, max);
        }
        return num;
    }

    public int printMenu() {
        System.out.flush();
        System.out.println("1. Показать таблицу");
        System.out.println("2. Добавить запись");
        System.out.println("3. Удалить запись");
        System.out.println("4. Отредактировать запись");
        System.out.println("5. Выйти из программы");
        return readInt(1, 5);
    }

    public void showTable() {
        db.displayTeacherTable();
    }

    public void addRecord() {
        String name = "", surname = "", patronymic = "";
        System.out.print("Введите имя учителя: ");
        name = in.next();
        System.out.print("Введите фамилию учителя: ");
        surname = in.next();
        System.out.print("Введите отчество учителя: ");
        patronymic = in.next();
        in.nextLine();
        db.addTeacherRecord(name, surname, patronymic);
    }

    public void deleteRecord() {
        db.displayTeacherTable();
        System.out.print("Введите id удаляемого учителя: ");
        int choice = readInt(Integer.MIN_VALUE, Integer.MAX_VALUE);
        db.deleteTeacher(choice);
    }

    public void editRecord() {
        db.displayTeacherTable();
        System.out.print("Введите id редактируемого учителя: ");
        int choice = readInt(Integer.MIN_VALUE, Integer.MAX_VALUE);
        String name = "", surname = "", patronymic = "";
        System.out.print("Введите имя учителя (Enter чтобы оставить без изменений): ");
        name = in.nextLine().trim();
        System.out.print("Введите фамилию учителя (Enter чтобы оставить без изменений): ");
        surname = in.nextLine().trim();
        System.out.print("Введите отчество учителя (Enter чтобы оставить без изменений): ");
        patronymic = in.nextLine().trim();
        db.editTeacher(choice, name, surname, patronymic);
    }

    public void executeMenu() {
        int choice = 0;

        while((choice = printMenu()) != 5) {
            switch(choice) {
                case 1:
                    showTable();
                    break;
                case 2:
                    addRecord();
                    break;
                case 3:
                    deleteRecord();
                    break;
                case 4:
                    editRecord();
                    break;
            }
        }
    }
}
