
public class Customer {
	String acc;
	String name;
	String add;
	String phone;
	
	Customer(String acc, String name, String add, String phone){
		this.acc=acc;
		this.name=name;
		this.add=add;
		this.phone =phone;
	}
	
	boolean isSame(Customer otherCustomer) {
		//System.out.print(this.add+otherCustomer.add+this.phone+otherCustomer.phone);
		return this.add.equals(otherCustomer.add)  && this.phone.equals(otherCustomer.phone);
	}
	
}
