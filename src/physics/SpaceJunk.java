package physics;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javafx.geometry.*;
import java.lang.Enum;

public class SpaceJunk {
	public Point3D oldcom;
	public Point3D com;
	private double radius;
	private double mass;
	private double volume;
	public Point3D velocity;
	public double altitude;
	
	public SpaceJunk(double rad, double m) {
		
		List<Double> list = genLoc();
		this.com = new Point3D(list.get(0)*1000,list.get(1)*1000,list.get(2)*1000);
		this.radius = rad;
		this.mass = m;
		this.volume = 4*(Math.PI)*Math.pow(rad, 3)/3;
		this.altitude = com.distance(new Point3D(0.0, 0.0, 0.0));
		int i=0;
		while(i==0)
			i=genSpeed();
		
		
	}
	
	
	
	private List<Double> genLoc() {
		
		double x1 = StdRandom.uniform(-7521, 7571);
		double x2 = StdRandom.uniform(-Math.sqrt(7571*7571-x1*x1), Math.sqrt(7571*7571-x1*x1));
		
		double x3;
		if(Math.sqrt(x1*x1+x2*x2)>6531) {
			x3 = StdRandom.uniform(-Math.sqrt(7571*7571-x1*x1-x2*x2), Math.sqrt(7571*7571-x1*x1-x2*x2));
		}
		else {
			int flip = StdRandom.uniform(0, 1);
			if(flip==1) x3 = StdRandom.uniform(-Math.sqrt(6531*6531-x1*x1-x2*x2), -Math.sqrt(7571*7571-x1*x1-x2*x2));
			else x3 = StdRandom.uniform(Math.sqrt(6531*6531-x1*x1-x2*x2), Math.sqrt(7571*7571-x1*x1-x2*x2));
		}
		List<Double> list = new ArrayList<>();
		list.add(x1); list.add(x2); list.add(x3);
		Collections.shuffle(list);
		return list;
		
	}
	
	
	private int genSpeed(){
		double speed = Math.sqrt((Earth.G * Earth.M) / (altitude));
		double vx, vy, vz;
		int flip = StdRandom.uniform(0, 2);
		
		//if(flip==0) {
			vx = StdRandom.uniform(-speed, speed);
			QuadraticEquation quad = new QuadraticEquation(com.getY()*com.getY()+com.getZ()*com.getZ(),2*com.getX()*com.getZ()*vx, vx*vx*com.getX()*com.getX()-com.getY()*com.getY()*(speed*speed-vx*vx));
			if (quad.hasSolution() == false) {
		         //System.out.println("No real solutions");
				 return 0;
			}  // finally calculate and print solutions
		    else {
		    	 
		         double solution1 = quad.calculateSolution1();
		         double solution2 = quad.calculateSolution2();
		         vz=solution1;
		         //System.out.println("Solution 1 = " + solution1 +
		          //  ", Solution 2 = " + solution2);
		         vy = (-vx*com.getX()-vz*com.getZ())/com.getY();
		         velocity = new Point3D(vx,vy,vz);
		         //System.out.println(velocity.getX());
		         
		    }  
		//}
		/*
		else if(flip==1) {
			vy = StdRandom.uniform(-speed, speed);
		}
		
		else {
			//vz = 
		}*/
		
		return 1;
		
	}
	
	public void collide(SpaceJunk enemy) {
		
		double v1x = this.velocity.getX()*(this.mass-enemy.mass)+2*enemy.mass*enemy.velocity.getX()/(this.mass+enemy.mass);
		double v1y = this.velocity.getY()*(this.mass-enemy.mass)+2*enemy.mass*enemy.velocity.getY()/(this.mass+enemy.mass);
		double v1z = this.velocity.getZ()*(this.mass-enemy.mass)+2*enemy.mass*enemy.velocity.getZ()/(this.mass+enemy.mass);
		
		double v2x = enemy.velocity.getX()*(enemy.mass-this.mass)+2*this.mass*this.velocity.getX()/(enemy.mass+this.mass);
		double v2y = enemy.velocity.getY()*(enemy.mass-this.mass)+2*this.mass*this.velocity.getY()/(enemy.mass+this.mass);
		double v2z = enemy.velocity.getZ()*(enemy.mass-this.mass)+2*this.mass*this.velocity.getZ()/(enemy.mass+this.mass);
		
		this.velocity = new Point3D(v1x, v1y, v1z);
		enemy.velocity = new Point3D(v2x, v2y, v2z);
		
		
	}
	
	public double detect(SpaceJunk neighbor) {
		
				
		  double tolerance = .00000001; // Our approximation of zero
		  int max_count = 200; // Maximum number of Newton's method iterations

	

		  double x = 0.01;


	         for( int count=1; (Math.abs(f(x, neighbor)) > tolerance) && ( count < max_count);count++)  {
	        	 x= x-(f(x, neighbor)/fprime(x, neighbor));
	        	 //System.out.println("Step: "+count+" x:"+x+" Value:"+f(x, neighbor));
		  }            

		  if( Math.abs(f(x, neighbor)) <= tolerance) {
		   System.out.println("Zero found at x="+x);
		   return x;
		  }
		  else {
		   //System.out.println("Failed to find a zero");
		  }
		
		
		
		return 0;
		
	}
	
	
		public double f(double x, SpaceJunk neighbor) {
		return Math.sqrt(Math.pow(this.oldcom.getX()-neighbor.oldcom.getX()+ x * (this.com.getX()- this.oldcom.getX() - neighbor.com.getX() + neighbor.oldcom.getX()), 2) + Math.pow(this.oldcom.getY()-neighbor.oldcom.getY()+ x * (this.com.getY()- this.oldcom.getY() - neighbor.com.getY() + neighbor.oldcom.getY()), 2)+Math.pow(this.oldcom.getZ()-neighbor.oldcom.getZ()+ x * (this.com.getZ()- this.oldcom.getZ() - neighbor.com.getZ() + neighbor.oldcom.getZ()), 2))-(this.radius+neighbor.radius);
	    }

	    public double fprime(double x, SpaceJunk neighbor) {
	        return (2*(this.com.getX()-this.oldcom.getX()-neighbor.com.getX()+neighbor.oldcom.getX())*this.oldcom.getX()-neighbor.oldcom.getX()+ x * (this.com.getX()- this.oldcom.getX() - neighbor.com.getX() + neighbor.oldcom.getX()) + (2*(this.com.getY()- this.oldcom.getY() - neighbor.com.getY() + neighbor.oldcom.getY())*this.oldcom.getY()-neighbor.oldcom.getY()+ x * (this.com.getY()- this.oldcom.getY() - neighbor.com.getY() + neighbor.oldcom.getY()) + (2* (this.com.getZ()- this.oldcom.getZ() - neighbor.com.getZ() + neighbor.oldcom.getZ())*(this.oldcom.getZ()-neighbor.oldcom.getZ()+ x * (this.com.getZ()- this.oldcom.getZ() - neighbor.com.getZ() + neighbor.oldcom.getZ())))/(2*(Math.sqrt(Math.pow(this.oldcom.getX()-neighbor.oldcom.getX()+ x * (this.com.getX()- this.oldcom.getX() - neighbor.com.getX() + neighbor.oldcom.getX()), 2) + Math.pow(this.oldcom.getY()-neighbor.oldcom.getY()+ x * (this.com.getY()- this.oldcom.getY() - neighbor.com.getY() + neighbor.oldcom.getY()), 2)+Math.pow(this.oldcom.getZ()-neighbor.oldcom.getZ()+ x * (this.com.getZ()- this.oldcom.getZ() - neighbor.com.getZ() + neighbor.oldcom.getZ()), 2))))));

	    }

	   

		  

	
	



	
	
	       
	
}
