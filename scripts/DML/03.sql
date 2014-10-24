--Finished...ish--

select distinct Item.ItemNumber, Weight, ExpectedPrice, FirstName, midinit, LastName, country, filename
from item, keywords, customer
where item.ItemNumber = keywords.ItemNumber and
Customer.Email = item.email and
(description like ('%'||&descript||'%') --Ask about using two prompts...
or keyword = &inputkw);