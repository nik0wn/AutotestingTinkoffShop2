package org.example.components;

import org.example.entity.Order;
import org.example.enums.ProductType;
import org.example.repositories.AccountRepository;
import org.example.repositories.OrderRepository;
import org.example.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderComponent {
    @Autowired
    UserComponent userComponent;

    @Autowired
    ProductComponent productComponent;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductRepository productRepository;
    @Autowired
    AccountComponent accountComponent ;
    @Autowired
    AccountRepository accountRepository;


    public List<Order> getListOfOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getListOfOrdersByUser(String phone) {
        var user = userComponent.getUserByPhone(phone);
        return orderRepository.findByAuthorId(user.getId());

    }

    public void deleteOrderById(Long id) {
        orderRepository.deleteById(id);

    }

    public Order createOrder(String userName,
                             String userPhone,
                             String productName) {
        var user = userComponent.getOrCreateUser(userName, userPhone);
        var product = productComponent.getProductByName(productName);
        var account = accountComponent.getOrCreateAccount(userPhone);

        if (product.getProductType() == ProductType.GOOD) {
            if (product.getPrice() > account.getBalance()){
                throw new IllegalStateException(
                        "Операция не может быть выполнена, недостаточно средств на счете."
                );
            }
            account.setBalance(account.getBalance() - product.getPrice());
            accountRepository.save(account);

            if (product.getRemainder() < 1) {
                throw new IllegalStateException(
                        String.format(
                                "Товаров '%s' не осталось", productName
                        )
                );
            }
            product.setRemainder(product.getRemainder() - 1);
            productRepository.save(product);

        }
        account.setBalance(account.getBalance() - product.getPrice());
        accountRepository.save(account);
        var order = new Order(user.getId(), product.getId());
        orderRepository.save(order);
        return order;
    }
}
