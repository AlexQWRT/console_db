import consoletable.ConsoleTable;
import consoletable.table.Cell;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConnectDB {
    private static final String USERNAME = "root";
    private static final String PASSWORD = "12345678";
    private static final String URL = "jdbc:mysql://localhost:3306/mydb";
    private static Statement statement;
    private static Connection connection;

    ConnectDB() {
        try {

            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            statement = connection.createStatement();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException();
        }
    }

    public Statement getStatement() {
        return statement;
    }

    public Connection  getConnection() {
        return connection;
    }

    private void printTable(ResultSet result) throws SQLException {
        ResultSetMetaData rsmd = result.getMetaData();
        int columnCount = rsmd.getColumnCount();
        List<Cell> header = new ArrayList<Cell>(){{
            for (int i = 1; i <= columnCount; i++) {
                add(new Cell(rsmd.getColumnName(i)));
            }
        }};
        List<List<Cell>> body = new ArrayList<List<Cell>>(){{
            while(result.next()) {
                add(new ArrayList<Cell>(){{
                    for (int i = 1; i <= columnCount; i++) {
                        add(new Cell(result.getString(i)));
                    }
                }});
            }
        }};
        new ConsoleTable.ConsoleTableBuilder().addHeaders(header).addRows(body).restrict(false).build().print();
    }

    public void displayTeacherTable() {
        try {
            ResultSet result = statement.executeQuery("SELECT * FROM `teachers`;");
            printTable(result);
        } catch (SQLException ex) {
            System.out.println("Ошибка базы данных!");
        }
    }

    public void addTeacherRecord(String name, String surname, String patronymic) {
        try {
            String buf = String.format("INSERT INTO `teachers` (teacher_name, teacher_surname, teacher_patronymic) " +
                            "VALUES ('%s', '%s', '%s');", name, surname, patronymic);
            if (statement.executeUpdate(buf) == 0) {
                throw new SQLException();
            }
        } catch (SQLException ex) {
            System.out.println("Ошибка добавления!");
            return;
        }
        System.out.println("Запись добавлена успешно!");
    }

    public void deleteTeacher(int id) {
        try {
            String buf = String.format("DELETE FROM `teachers` where teacher_id=%s;", id);
            if (statement.executeUpdate(buf) == 0) {
                throw new SQLException();
            }
        } catch (SQLException ex) {
            System.out.println("Ошибка удаления!");
            return;
        }
        System.out.println("Учитель успешно удалён!");
    }

    public void editTeacher(int id, String name, String surname, String patronymic) {
        try {
            if (name.isEmpty() && surname.isEmpty() && patronymic.isEmpty()) {
                System.out.println("Редактирование отменено!");
                return;
            }
            String buf = "UPDATE `teachers` SET ";
            if (!name.isEmpty()) {
                buf += String.format("teacher_name='%s', ", name);
            }
            if (!surname.isEmpty()) {
                buf += String.format("teacher_surname='%s', ", surname);
            }
            if (!patronymic.isEmpty()) {
                buf += String.format("teacher_patronymic='%s', ", patronymic);
            }
            buf = buf.substring(0, buf.length()-2);
            buf += String.format(" where teacher_id=%s;", id);
            if (statement.executeUpdate(buf) == 0) {
                throw new SQLException();
            }
        } catch (SQLException ex) {
            System.out.println("Ошибка редактирования!");
            return;
        }
        System.out.println("Учитель успешно отредактирован!");
    }
}
