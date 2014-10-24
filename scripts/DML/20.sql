UPDATE MessageRating SET Rating='like' 
WHERE (MessageID,ThreadID) in (SELECT p1.MessageID,p1.ThreadID FROM p1,p2 WHERE p1.Email=p2.Email);