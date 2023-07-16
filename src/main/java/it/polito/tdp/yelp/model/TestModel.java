package it.polito.tdp.yelp.model;

public class TestModel {

	public static void main(String[] args) {
		
		Model m = new Model();
		Business b = new Business("kM7mfXS8LgBPjRrqGb6i6g", "9945 W McDowell Rd↵Ste 108↵Avondale, AZ 85323", "true", "Musical Instruments & Teachers, Shopping", "Avondale", 5, "Guitar Center", "", 33.430535, -112.345359, "AZ", 1.5);
		Business bb = new Business("MagW3D2UDQaP6KHzhauUHw", "20511 N Hayden Rd↵Ste 110↵Scottsdale, AZ 85255",	"false", "Pizza, Restaurants", "Scottsdale", 34, "Zpizza", "",	33.67179, -111.906001 , "AZ", 4);
		String msg = m.creaGrafo(b);
		System.out.println(msg+"\n");
		
		String mmg = m.getRMax();
		System.out.println(mmg);

	}

}
