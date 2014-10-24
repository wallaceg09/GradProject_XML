select seller.country, COUNT(itemnumber) from trade inner join customer
seller on trade.SEMail = seller. Email inner join customer buyer
on trade.BEMail= Buyer.Email
where seller.country <> Buyer.country
group by seller.country;