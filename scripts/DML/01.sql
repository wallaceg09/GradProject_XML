create view winning_bid as
select Bid.BEmail, Bid.IEmail, Bid.ItemNumber, Bid.BidAmount
from
(
	select IEmail, ItemNumber, max(BidAmount) MaxBidPrice
	from
	(
		select IEmail, Bid.ItemNumber, BidAmount
		from Bid, Item
		where 	BidTime < Deadline and
			Bid.ItemNumber = Item.ItemNumber and
			Deadline < sysdate
	)
	group by IEmail, ItemNumber
) MaxBid join Bid on (MaxBid.ItemNumber = Bid.ItemNumber and MaxBid.IEmail = Bid.IEmail)
where BidAmount = MaxBidPrice;

create view max_trans_id as
select Max(IDNumber) max_id
from transaction;


declare
	BEmail varchar2(14);
	IEmail varchar2(14);
	ItemNumber number(4);
	BidAmount number(4);

	initialID number(4);

	Cursor c is
		select * from winning_bid;
begin
	select max_id into initialID from max_trans_id;
	if initialID is null
	then initialID := -1;
	end if;

	initialID := initialID + 1;

	open c;
	loop
		fetch c into BEmail, IEmail, ItemNumber, BidAmount;
		exit WHEN c %NOTFOUND;

		insert 	into Transaction (IDNumber, Type, Amount, TDate, Email)
			values(initialID, 'bought', BidAmount, sysdate, BEmail);
		insert into ItemTransaction (ItemNumber, Email, IDNumber)
			values(ItemNumber, IEmail, initialID);

		initialID := initialID + 1;

		insert 	into Transaction (IDNumber, Type, Amount, TDate, Email)
			values(initialID, 'sold', BidAmount, sysdate, IEmail);
		insert into ItemTransaction (ItemNumber, Email, IDNumber)
			values(ItemNumber, IEmail, initialID);

		initialID := initialID + 1;
		
	end loop;
	
end;
/

select * from transaction;
select * from ItemTransaction;

drop view max_trans_id;
drop view winning_bid;

