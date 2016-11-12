package test.edu.javaee.spring;

public class Atest {

	private String name;
	private Btest btest;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Btest getBtest() {
		return btest;
	}

	public void setBtest(Btest btest) {
		this.btest = btest;
	}
	public void show(){
		System.out.println("Atest÷–show∑Ω∑®: "+name);
	}
}
