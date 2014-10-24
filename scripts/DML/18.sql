SELECT item.itemnumber, bid.bidamount, bid.bidtime,bid.BEMail, customer. Firstname, customer.Midinit,
customer.Lastname, customer.Country
FROM item inner join bid
on bid.ItemNumber=item.itemnumber inner join customer on
bid.BEMail= customer.Email
WHERE expectedprice =(SELECT MAX(expectedprice) FROM item inner join bid on 
bid.ItemNumber= item.itemnumber);
