package br.com.alura.ecommerce;

import br.com.alura.ecommerce.configs.KafkaService;
import br.com.alura.ecommerce.vo.Order;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class CreateUserService {

    private final Connection connection;

    CreateUserService() throws SQLException {
        String url = "jdbc:sqlite:target/users_database.db";
        this.connection = DriverManager.getConnection(url);
        try {
            connection.createStatement().execute("create table User (" + "uuid varchar(200) primary key," + "email varchar(200))");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

    }

    public static void main(String[] args) throws SQLException {
        var createUserService = new CreateUserService();
        try (var service = new KafkaService<>(CreateUserService.class.getSimpleName(), "ECOMMERCE_NEW_ORDER", createUserService::parse, Order.class, new HashMap<>())) {
            service.run();
        }
    }

    private void parse(ConsumerRecord<String, Order> record) throws ExecutionException, InterruptedException, SQLException {
        System.out.println("---------------------------------------- ");
        System.out.println("Processing new order, checking for new user ");
        System.out.println(record.key());
        System.out.println(record.value());
        var order = record.value();

        if (isNewUser(order.getEmail())) {
            System.out.println("Criando Usuário");
            insertNewUser(order.getEmail());
        }

    }

    private void insertNewUser(String email) throws SQLException {
        var insert = connection.prepareStatement("insert into User (uuid, email) values (?, ?)");
        insert.setString(1, UUID.randomUUID().toString());
        insert.setString(2, email);
        insert.execute();
        System.out.println("Usuário uuid e " + email + "adicionado");
    }

    private boolean isNewUser(String email) throws SQLException {
        var consulta = connection.prepareStatement("Select uuid from user where email = ? limit 1");
        consulta.setString(1, email);
        var rs = consulta.executeQuery();
        var isNew = !rs.next();
        System.out.println("Pesquisa se exist na base true  inclui e falso já existe -> " + isNew);
        return isNew;
    }

}
