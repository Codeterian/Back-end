package com.codeterian.order.domain.repository;

import com.codeterian.order.domain.entity.order.Orders;

public interface OrderRepository {

	Orders save(Orders Order);

}
