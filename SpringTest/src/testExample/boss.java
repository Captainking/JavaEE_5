package testExample;


public class boss {
	 
	private office office;
	 
	 private car car;
 

  public office getOffice() {
		return office;
	}


	public void setOffice(office office) {
		this.office = office;
	}


	public car getCar() {
		return car;
	}


	public void setCar(car car) {
		this.car = car;
	}


public String tostring(){
	  return "this boss has "+car.toString()+"and in "+office.tostring();
  }
}
