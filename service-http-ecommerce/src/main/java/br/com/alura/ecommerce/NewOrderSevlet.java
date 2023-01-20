package br.com.alura.ecommerce;

import br.com.alura.ecommerce.configs.KafkaDispatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

public class NewOrderSevlet extends HttpServlet {

    private final KafkaDispatcher<Order> orderDispatcher = new KafkaDispatcher<>();
    private final KafkaDispatcher<Email> emailDispatcher = new KafkaDispatcher<>();

    @Override
    public void destroy() {
        super.destroy();
        orderDispatcher.close();
        emailDispatcher.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            var email = req.getParameter("email");
            var orderId = UUID.randomUUID().toString();
            var amount = BigDecimal.valueOf(Long.parseLong(req.getParameter("amount"))).setScale(2, RoundingMode.HALF_UP);

            var order = new Order(orderId, amount, email);
            orderDispatcher.send("ECOMMERCE_NEW_ORDER", email, order);

            var emailCode = new Email("Teste", "Thank you for your order! We are processing your order!");
            emailDispatcher.send("ECOMMERCE_SEND_EMAIL", email, emailCode);

            System.out.println("Nova ordem enviada com sucesso!!");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().println("Nova ordem enviada com sucesso!!");

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}

