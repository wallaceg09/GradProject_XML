select itemnumber inumber, bemail buyeremail, iemail selleremail, to_char(bidtime,'mm-yyyy') time, sum(bidamount) total
from bid
group by cube( itemnumber, bemail, iemail, to_char(bidtime,'mm-yyyy'));
