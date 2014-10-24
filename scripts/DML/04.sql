select LastName, FirstName, MidInit, Cust.Email
from
(
	select Max(cnt) tmax
	from count_not_vip
) M,
(
	select Email, cnt
	from count_not_vip
) C,
Customer cust
where cnt = tmax and C.Email = Cust.Email;