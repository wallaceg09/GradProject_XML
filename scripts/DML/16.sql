select c.Email, c.Lastname, c.RegistrationDate, 
(case when gc.email <> null then 'Gold'
when pc.email <> null then 'Platinum' else 'Regular' end) Status
from customer c left join goldcustomer gc on
c.Email = gc.Email
left join PlatinumCustomer pc on
c.Email = pc.Email
where c.Email in (select email from query16_b
where totalbids> item_bought)
order by Status, c.RegistrationDate;
