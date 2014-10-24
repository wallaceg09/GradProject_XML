insert into GoldCustomer (Email, GUDate, Discount)
(
	select c_n_v.Email, sysdate, cnt
	from count_not_vip c_n_v join max_transaction_customer m_t_c on (c_n_v.Email = m_t_c.Email)
);