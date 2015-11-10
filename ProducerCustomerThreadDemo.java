class Product{
	String name;
	double price;
	boolean generator = false;  //flag for create product
}

class Producer extends Thread{
	Product product;  //product
	
	public Producer(Product product){
		this.product = product;
	}
	
	@Override
	public void run(){
		int i = 0;
		while(true){
			synchronized (product) {
				if(!product.generator)
				{
					
					if(i % 2 == 0){
						product.name = "apple";
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						product.price = 6.5;
					}
					else{
						product.name = "banana";
						product.price = 2.0;
					}
					System.out.println("Product: " + product.name + "...|...Price: " + product.price);
					i++;
					product.generator = true;
					product.notify();  //producer use notify to wake up customer wait() method
				}
				else{
					try {
						product.wait();    //in this time, producer already have product, so use wait method to wait for customer buy product until customer use notify method to wake it up
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}

class Customer extends Thread{
	Product product;
	
	public Customer(Product product) {
		this.product = product;
	}
	
	@Override
	public void run(){
		while(true){
			synchronized (product) {
				if(product.generator){
					System.out.println("Customer buy Product: " + product.name + "...|...Price: " + product.price);	
					product.notify();  //customer use notify to wake up producer wait()
					product.generator = false;
				}
				else {
					try {
						product.wait(); //this time, producer haven't product for customer, so customer have to use wait method to wait product
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
public class ProducerCustomerThreadDemo {

	public static void main(String[] args) {
		Product product = new Product();  // use this object as mutex for syncronize code block
		
		Producer producer = new Producer(product);
		Customer customer = new Customer(product);
		
		producer.start();
		customer.start();
	}
}