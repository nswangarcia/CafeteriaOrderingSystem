-- Drop and create the database
DROP DATABASE IF EXISTS `cafeteriaDB`;
CREATE DATABASE IF NOT EXISTS cafeteriaDB;
USE cafeteriaDB;

-- Create UserData table
CREATE TABLE UserData
(
    userID      INT AUTO_INCREMENT,
    name        VARCHAR(50),
    email       VARCHAR(50),
    PRIMARY KEY (userID)
);

-- Create EmployeeData table
CREATE TABLE EmployeeData
(
    empID       INT AUTO_INCREMENT,
    name        VARCHAR(50),
    email       VARCHAR(50),
    role        VARCHAR(50),
    username    VARCHAR(50) UNIQUE,
    password    VARCHAR(50),
    userID      INT,
    PRIMARY KEY (empID),
    FOREIGN KEY (userID) REFERENCES UserData (userID) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Create Menus table
CREATE TABLE MenuData
(
    menuID      INT AUTO_INCREMENT,
    name        VARCHAR(50) UNIQUE,
    status      VARCHAR(50),
    PRIMARY KEY (menuID)
);

-- Create MenuItems table
CREATE TABLE MenuItemData
(
    menuItemID  INT AUTO_INCREMENT,
    name        VARCHAR(50) UNIQUE,
    status      VARCHAR(50),
    description VARCHAR(240),
    menuID      INT,    
    PRIMARY KEY (menuItemID),
    FOREIGN KEY (menuID) REFERENCES MenuData (menuID) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Create Orders table
CREATE TABLE OrderData
(
    orderID     INT AUTO_INCREMENT,
    name        VARCHAR(50),
    email       VARCHAR(50),
    menuName    VARCHAR(50),
    menuItems   VARCHAR(240),
    status		VARCHAR(50),
    userID		INT,
	timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (orderID),
    FOREIGN KEY (userID) REFERENCES UserData (userID) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Create OrderMenuItems table for the many-to-many relationship between Orders and MenuItems
CREATE TABLE OrderMenuItemData
(
    orderID     INT,
    menuItemID  INT,
    FOREIGN KEY (orderID) REFERENCES OrderData (orderID) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (menuItemID) REFERENCES MenuItemData (menuItemID) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (orderID, menuItemID)
);

-- Insert data into UserData table
INSERT INTO UserData (name, email)
VALUES 
    ('John Doe', 'john@example.com'),
    ('Jane Doe', 'jane@example.com'),
    ('Sunny', 'sunny@example.com'),
    ('Cher', 'cher@example.com');

-- Insert data into EmployeeData table by selecting userID from UserData
INSERT INTO EmployeeData (name, email, role, Username, password, userID)
VALUES 
    ('Sunny', 'sunny@asu.edu', 'operator', 'operator', '321', (SELECT userID FROM UserData WHERE name = 'Sunny')),
    ('Cher', 'cher@asu.edu', 'manager', 'manager', '123', (SELECT userID FROM UserData WHERE name = 'Cher'));
    
-- Insert data into MenuData
INSERT INTO MenuData (name, status)
VALUES 
	('Breakfast', 'Available'),
    ('Lunch', 'Available'),
    ('Dinner', 'Available'),
    ('Beverages', 'Available');
    
-- Insert data into MenuItemData
INSERT INTO MenuItemData (name, status, description, menuID)
VALUES 
	('Pancakes', 'Available', 'Fluffy pancakes served with butter and maple syrup, often accompanied by bacon or sausage.',  (SELECT menuID FROM MenuData WHERE name = 'Breakfast')),
    ('Eggs Benedict', 'Available', 'Poached eggs served on English muffins with Canadian bacon or ham, topped with hollandaise sauce.',  (SELECT menuID FROM MenuData WHERE name = 'Breakfast')),
    ('Omelets', 'Available', 'Fluffy eggs folded over a variety of fillings such as cheese, vegetables, bacon, ham, or mushrooms.',  (SELECT menuID FROM MenuData WHERE name = 'Breakfast')),
    ('French Toast', 'Available', 'Slices of bread dipped in a mixture of eggs and milk, then grilled until golden brown and served with syrup and powdered sugar.',  (SELECT menuID FROM MenuData WHERE name = 'Breakfast')),
    ('Breakfast Burrito', 'Available', 'Tortilla filled with scrambled eggs, cheese, potatoes, and a choice of bacon, sausage, or chorizo, often served with salsa and sour cream.',  (SELECT menuID FROM MenuData WHERE name = 'Breakfast')),
    ('Bagel with Lox', 'Available', 'Toasted bagel topped with cream cheese, smoked salmon (lox), capers, red onion, and sometimes tomato slices.',  (SELECT menuID FROM MenuData WHERE name = 'Breakfast')),
    ('Biscuits and Gravy', 'Available', 'Flaky biscuits topped with creamy sausage gravy, served hot and often accompanied by eggs.',  (SELECT menuID FROM MenuData WHERE name = 'Breakfast')),
    ('Belgian Waffles', 'Available', 'Crispy waffles topped with whipped cream, fresh berries, and maple syrup. Breakfast Sandwich: English muffin, biscuit, or croissant filled with eggs, cheese, and a choice of sausage, bacon, or ham.',  (SELECT menuID FROM MenuData WHERE name = 'Breakfast')),
    ('Greek Yogurt Parfait', 'Available', 'Layers of Greek yogurt, granola, and fresh berries or fruit, sometimes drizzled with honey or maple syrup.',  (SELECT menuID FROM MenuData WHERE name = 'Breakfast')),
    ('Cheeseburger', 'Available', 'Classic American favorite, typically served with lettuce, tomato, onion, pickles, and condiments on a bun.',  (SELECT menuID FROM MenuData WHERE name = 'Lunch')),
    ('Club Sandwich', 'Available', 'Triple-decker sandwich with layers of turkey or chicken, bacon, lettuce, tomato, and mayo on toasted bread.',  (SELECT menuID FROM MenuData WHERE name = 'Lunch')),
    ('Caesar Salad', 'Available', 'Romaine lettuce tossed with Caesar dressing, croutons, and Parmesan cheese, often topped with grilled chicken or shrimp.',  (SELECT menuID FROM MenuData WHERE name = 'Lunch')),
    ('BBQ Ribs', 'Available', 'Slow-cooked pork or beef ribs slathered in barbecue sauce, served with coleslaw and cornbread.',  (SELECT menuID FROM MenuData WHERE name = 'Lunch')),
    ('Philly Cheesesteak', 'Available', 'Sliced steak, often with grilled onions and peppers, topped with melted cheese on a hoagie roll.',  (SELECT menuID FROM MenuData WHERE name = 'Lunch')),
    ('New England Clam Chowder', 'Available', 'Cream-based soup with clams, potatoes, onions, and celery, seasoned with herbs and spices.',  (SELECT menuID FROM MenuData WHERE name = 'Lunch')),
    ('Cobb Salad', 'Available', 'Mixed greens topped with grilled chicken, bacon, avocado, hard-boiled eggs, tomatoes, blue cheese, and vinaigrette dressing.',  (SELECT menuID FROM MenuData WHERE name = 'Lunch')),
    ('Pulled Pork Sandwich', 'Available', 'Slow-cooked pork shoulder shredded and mixed with barbecue sauce, served on a bun with pickles and coleslaw.',  (SELECT menuID FROM MenuData WHERE name = 'Lunch')),
    ('Fish Tacos', 'Available', 'Soft corn tortillas filled with grilled or fried fish, cabbage slaw, avocado, salsa, and lime.',  (SELECT menuID FROM MenuData WHERE name = 'Lunch')),
    ('Shrimp Scampi', 'Available', 'Shrimp sautéed in garlic butter and white wine sauce, served over linguine pasta with a sprinkle of Parmesan cheese and parsley.',  (SELECT menuID FROM MenuData WHERE name = 'Dinner')),
    ('BBQ Brisket', 'Available', 'Slow-smoked beef brisket served with coleslaw, baked beans, cornbread, and pickles.',  (SELECT menuID FROM MenuData WHERE name = 'Dinner')),
    ('Vegetarian Lasagna', 'Available', 'Layers of pasta filled with ricotta cheese, spinach, mushrooms, and marinara sauce, topped with mozzarella cheese and baked until golden.',  (SELECT menuID FROM MenuData WHERE name = 'Dinner')),
    ('Stuffed Bell Peppers', 'Available', 'Bell peppers stuffed with a mixture of ground beef or turkey, rice, tomatoes, and cheese, baked until tender.',  (SELECT menuID FROM MenuData WHERE name = 'Dinner')),
    ('Lobster Tail Dinner', 'Available', 'Broiled or grilled lobster tail served with drawn butter, roasted potatoes or rice, steamed vegetables, and a wedge of lemon.',  (SELECT menuID FROM MenuData WHERE name = 'Dinner')),
    ('Taco Night', 'Available', 'Build-your-own taco dinner with seasoned ground beef or chicken, soft or crispy taco shells, lettuce, tomatoes, cheese, salsa, sour cream, and guacamole.',  (SELECT menuID FROM MenuData WHERE name = 'Dinner')),
    ('Buffalo Wings', 'Available', 'Deep-fried chicken wings coated in spicy buffalo sauce, served with celery sticks and blue cheese dressing.',  (SELECT menuID FROM MenuData WHERE name = 'Dinner')),
    ('Steakhouse Dinner', 'Available', 'A classic American favorite featuring a grilled steak (such as ribeye, filet mignon, or New York strip) served with sides like mashed potatoes, steamed vegetables, and a salad.',  (SELECT menuID FROM MenuData WHERE Name = 'Dinner')),
    ('Grilled Salmon', 'Available', 'Fresh salmon fillet grilled and served with rice pilaf or roasted potatoes, steamed asparagus, and a lemon wedge.',  (SELECT menuID FROM MenuData WHERE name = 'Dinner')),
    ('Roast Chicken', 'Available', 'Herb-roasted or rotisserie chicken served with mashed potatoes, gravy, roasted vegetables, and a side of cranberry sauce.',  (SELECT menuID FROM MenuData WHERE name = 'Dinner')),
    ('Spaghetti and Meatballs', 'Available', 'Spaghetti pasta topped with marinara sauce and meatballs, served with garlic bread and a side salad.',  (SELECT menuID FROM MenuData WHERE name = 'Dinner')),
    ('Coffee', 'Available', 'Whether black, with cream and sugar, or as specialty drinks like lattes and cappuccinos.',  (SELECT menuID FROM MenuData WHERE name = 'Beverages')),
    ('Iced Tea', 'Available', 'Served with sweetened or unsweetened with lemon.',  (SELECT menuID FROM MenuData WHERE name = 'Beverages')),
    ('Soda (Soft Drinks)', 'Available', 'Varieties like cola (Coca-Cola, Pepsi), lemon-lime (Sprite, 7UP), root beer.',  (SELECT menuID FROM MenuData WHERE Name = 'Beverages')),
    ('Craft Beer', 'Available', 'With a growing craft brewery scene.',  (SELECT menuID FROM MenuData WHERE name = 'Beverages')),
    ('Bottled Water', 'Available', 'Both still or sparkling.',  (SELECT menuID FROM MenuData WHERE name = 'Beverages'));
    
-- Insert data into OrderData
INSERT INTO OrderData (status, name, email, menuName, userID)
SELECT 'Pending', name, email, 'Lunch', userID
FROM UserData
WHERE name = 'John Doe';

INSERT INTO OrderData (status, name, email, menuName, userID)
SELECT 'Pending', name, email, 'Breakfast', userID
FROM UserData
WHERE name = 'Jane Doe';
   
-- Insert data into OrderMenuItemData
INSERT INTO OrderMenuItemData (orderID, menuItemID)
VALUES 
    ((SELECT orderID FROM OrderData WHERE name = 'John Doe'), (SELECT menuItemID FROM MenuItemData WHERE name = 'Pancakes')),
    ((SELECT orderID FROM OrderData WHERE name = 'John Doe'), (SELECT menuItemID FROM MenuItemData WHERE name = 'Omelets')),
    ((SELECT orderID FROM OrderData WHERE name = 'Jane Doe'), (SELECT menuItemID FROM MenuItemData WHERE name = 'Caesar Salad')),
    ((SELECT orderID FROM OrderData WHERE name = 'Jane Doe'), (SELECT menuItemID FROM MenuItemData WHERE name = 'BBQ Brisket'));
