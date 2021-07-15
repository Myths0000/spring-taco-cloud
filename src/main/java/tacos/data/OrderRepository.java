package tacos.data;

import tacos.entity.Order;

public interface OrderRepository {

  Order save(Order order);
}