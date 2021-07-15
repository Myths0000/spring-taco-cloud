package tacos.data;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;

import tacos.entity.Order;
import tacos.entity.Taco;

@Repository
public class JdbcOrderRepository implements OrderRepository {

	// SimpleJdbcInsert !!!!
	private SimpleJdbcInsert orderInserter;
	private SimpleJdbcInsert orderTacoInserter;
	private ObjectMapper objectMapper;
 
	/**
	 * 与Taco不同的是，不需要把jdbc作为实例赋值
	 * SimpleJdbcInsert 对jdbc进行了包装
	 * order 需要数据库提供返回主键的值
	 * @param jdbc
	 */
	@Autowired
	public JdbcOrderRepository(JdbcTemplate jdbc) {
		this.orderInserter = new SimpleJdbcInsert(jdbc)
				.withTableName("Taco_Order")
				.usingGeneratedKeyColumns("id");

		this.orderTacoInserter = new SimpleJdbcInsert(jdbc)
				.withTableName("Taco_Order_Tacos");
				this.objectMapper = new ObjectMapper();
	}
	
	/**
	 * 版本命名区别
	 * save（）方法并没保存任何内容，而是调用其他private
	 */
	@Override
	public Order save(Order order) {
	    order.setPlacedAt(new Date());
	    //获取订单id，
	    long orderId = saveOrderDetails(order);
	    order.setId(orderId);
	    List<Taco> tacos = order.getTacos();
	    //储存taco到订单
	    for (Taco taco : tacos) {
	      saveTacoToOrder(taco, orderId);
	    }
	    
	    return order;
	}
	
    private long saveOrderDetails(Order order) {
    	
    	// 把Order中的值 复制到Map
        @SuppressWarnings("unchecked")
        Map<String, Object> values =
            objectMapper.convertValue(order, Map.class);

        //Date会被 objectMapper 转换成long
        values.put("placedAt", order.getPlacedAt());
//      executeAndReturnkey() Map的key对应表中要插入的数据列名
        long orderId = orderInserter.executeAndReturnKey(values).longValue();
        return orderId;
      }

      private void saveTacoToOrder(Taco taco, long orderId) {
    	  Map<String, Object> values = new HashMap<>();
	      values.put("tacoOrder", orderId);
	      values.put("taco", taco.getId());
	      orderTacoInserter.execute(values);
      }
}
