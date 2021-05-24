DROP TABLE IF EXISTS CLIENT; 
CREATE TABLE client (
    id INT AUTO_INCREMENT NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    id_number VARCHAR(255) NOT NULL,
    mobile_number VARCHAR(255),
    physical_address VARCHAR(255),
    PRIMARY KEY (id)
);  
INSERT into client values (1, 'Sowjanya', 'Gonuguntla', '8901182565084','9595959595','Bangalore'); 
INSERT into client values (2,'Suma', 'Gonuguntla', '8805182565184','8585858585','Mumbai'); 
INSERT into client values (3,'Mamatha', 'Gonuguntla', '9011022525184','7575757575','Pune'); 
INSERT into client values (4,'Shrihan', 'Kunameneni','1809275689085', '6565656565','Delhi'); 
INSERT into client values (5,'kaushik','Gorthi', '1505095678125', '9696969696','Andhara Pradesh'); 
