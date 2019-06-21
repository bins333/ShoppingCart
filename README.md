# ShoppingCart

<b>An app to scan the product and calculate the total amount.</b>

https://github.com/bins333/ShoppingCart/blob/master/supermarket/dist/supermarket-0.0.1-SNAPSHOT-jar-with-dependencies.jar


#Usecase: Implement the code for a checkout system that handles pricing schemes such as “apples cost 50 pence, three apples cost £1.30.”
Implement the code for a supermarket checkout that calculates the total price of a number of items. In a normal supermarket, things are identified using Stock Keeping Units, or SKUs. In our store, we’ll use individual letters of the alphabet (A, B, C, and so on). Our goods are priced individually. In addition, some items are multi-priced: buy ‘n’ of them, and they’ll cost you ‘y’ pence. For example, item ‘A’ might cost 50 pence individually, but this week we have a special offer: buy three ‘A’s and they’ll cost you £1.30. 

<b>Use these prices for the sample:</b><br/>
A | 50 | 3 for 130  <br/>
B | 30 | 2for 45 <br/>
C | 20<br/>
D | 15<br/>

Our checkout accepts items in any order, so that if we scan a B, an A, and another B, we’ll recognize the two B’s and price them at 45 (for a total price so far of 95). Because the pricing changes frequently, we need to be able to pass in a set of pricing rules each time we start handling a checkout transaction.

The solution should allow for items to input at the command line, and allow for a final total to be calculated and for a running total after each item is ‘scanned’.

#How to run

<b>Open CMD/Terminal: java -jar supermarket-0.0.1-SNAPSHOT-jar-with-dependencies.jar </b>

Java, Maven
