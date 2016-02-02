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
		public int flag;
		
		
		/*constructor for spacejunk */
		
		public SpaceJunk(double rad, double m) {
			
			List<Double> list = genLoc();
			this.com = new Point3D(list.get(0)*1000,list.get(1)*1000,list.get(2)*1000);
			this.radius = rad;
			this.mass = m;
			this.oldcom = this.com;
			this.volume = 4*(Math.PI)*Math.pow(rad, 3)/3;
			this.altitude = com.distance(new Point3D(0.0, 0.0, 0.0));
			int i=0;
			this.flag = 0; /*for different solution objects*/
			while(i==0)
				i=genSpeed();
			
			
		}
		
		
		/*generate random location in a certain interval */
		private List<Double> genLoc() {
			
			double x1 = StdRandom.uniform(-7571, 7571); /*intervals for debris*/
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
		
		/* generate random speed based on gravitational acceleration */
		private int genSpeed(){
			double speed = Math.sqrt((Earth.G * Earth.M) / (altitude));
			double vx, vy, vz;

			
			
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
			         
			         vy = (-vx*com.getX()-vz*com.getZ())/com.getY();
			         velocity = new Point3D(vx,vy,vz);
			         
			         
			    }  
			return 1;
			
		}
		
		/*collide two objects*/
		public void collide(SpaceJunk enemy) {
			
			
			/*laser vs space junk 1-0 */
			
			if(this.flag==1 && enemy.flag==0) {
				StdOut.println("iceball collision");
				enemy.velocity = new Point3D(enemy.velocity.getX()*0.70, enemy.velocity.getY()*0.7, enemy.velocity.getZ()*0.7);
				double aclmag = (Earth.G * Earth.M)/(Math.pow(enemy.altitude,2));
				Point3D accel = new Point3D(-enemy.com.getX()*aclmag/enemy.altitude, -enemy.com.getY()*aclmag/enemy.altitude, -enemy.com.getZ()*aclmag/enemy.altitude);
				//System.out.println("accel vector: "+accel.toString());
				
				enemy.com = new Point3D(enemy.com.getX()+enemy.velocity.getX()*0.5+0.5*accel.getX()*0.5*0.5, enemy.com.getY()+enemy.velocity.getY()*0.5+0.5*accel.getY()*0.5*0.5, enemy.com.getZ()+enemy.velocity.getZ()*0.5+0.5*accel.getZ()*0.5*0.5);
				enemy.altitude = enemy.com.distance(new Point3D(0,0,0));
				enemy.velocity = new Point3D(enemy.velocity.getX()+accel.getX()*0.5,enemy.velocity.getY()+accel.getY()*0.5,enemy.velocity.getZ()+accel.getZ()*0.5);
				return;	
			}
			
			
			/* net(balloon) vs space junk */
			else if(this.flag==2 || enemy.flag==0) {
				StdOut.println("net collision");
				//this.velocity = new Point3D(enemy.velocity.getX()*0.02, enemy.velocity.getY()*0.02, enemy.velocity.getZ()*0.02);
				enemy.velocity = this.velocity;
				enemy.flag = 3;
				return;
			}
			
			/* net (baloon) vs net (balloon) */
			else if(this.flag==3 || enemy.flag==3) {
				return;
			}
			
			
			/* otherwise two space junks colliding against each other - regular collision laws */
			
			
			double v1x = this.velocity.getX()*(this.mass-enemy.mass)+2*enemy.mass*enemy.velocity.getX()/(this.mass+enemy.mass);
			double v1y = this.velocity.getY()*(this.mass-enemy.mass)+2*enemy.mass*enemy.velocity.getY()/(this.mass+enemy.mass);
			double v1z = this.velocity.getZ()*(this.mass-enemy.mass)+2*enemy.mass*enemy.velocity.getZ()/(this.mass+enemy.mass);
			/*
			double v2x = enemy.velocity.getX()*(enemy.mass-this.mass)+2*this.mass*this.velocity.getX()/(enemy.mass+this.mass);
			double v2y = enemy.velocity.getY()*(enemy.mass-this.mass)+2*this.mass*this.velocity.getY()/(enemy.mass+this.mass);
			double v2z = enemy.velocity.getZ()*(enemy.mass-this.mass)+2*this.mass*this.velocity.getZ()/(enemy.mass+this.mass);
			*/
			this.velocity = new Point3D(v1x, v1y, v1z);
			
			//enemy.velocity = new Point3D(v2x, v2y, v2z);
			
			double aclmag = (Earth.G * Earth.M)/(Math.pow(this.altitude,2));
			Point3D accel = new Point3D(-this.com.getX()*aclmag/this.altitude, -this.com.getY()*aclmag/this.altitude, -this.com.getZ()*aclmag/this.altitude);
			//System.out.println("accel vector: "+accel.toString());
			
			this.com = new Point3D(this.com.getX()+this.velocity.getX()*0.5+0.5*accel.getX()*0.5*0.5, this.com.getY()+this.velocity.getY()*0.5+0.5*accel.getY()*0.5*0.5, this.com.getZ()+this.velocity.getZ()*0.5+0.5*accel.getZ()*0.5*0.5);
			this.altitude = this.com.distance(new Point3D(0,0,0));
			this.velocity = new Point3D(this.velocity.getX()+accel.getX()*0.5,this.velocity.getY()+accel.getY()*0.5,this.velocity.getZ()+accel.getZ()*0.5);
			/*
			
			aclmag = (Earth.G * Earth.M)/(Math.pow(enemy.altitude,2));
			accel = new Point3D(-enemy.com.getX()*aclmag/enemy.altitude, -enemy.com.getY()*aclmag/enemy.altitude, -enemy.com.getZ()*aclmag/enemy.altitude);
			//System.out.println("accel vector: "+accel.toString());
			
			enemy.com = new Point3D(enemy.com.getX()+enemy.velocity.getX()*0.5+0.5*accel.getX()*0.5*0.5, enemy.com.getY()+enemy.velocity.getY()*0.5+0.5*accel.getY()*0.5*0.5, enemy.com.getZ()+enemy.velocity.getZ()*0.5+0.5*accel.getZ()*0.5*0.5);
			enemy.altitude = enemy.com.distance(new Point3D(0,0,0));
			enemy.velocity = new Point3D(enemy.velocity.getX()+accel.getX()*0.5,enemy.velocity.getY()+accel.getY()*0.5,enemy.velocity.getZ()+accel.getZ()*0.5);
			*/
		}
		
		public double detect(SpaceJunk neighbor) {
				  
			  int flag;
			  if(f(0, neighbor)<=0) flag=0;
			  else flag=1;
			  
			  int funct;
			  double x=0.04; 
			  while(x<=1.0) {
				 				 
				  if(f(x, neighbor)<=0) funct = 0;
				  else funct = 1;
				  if((funct-flag)!=0) {
					  return x;
				  }

				  else
					  x+=0.04;
				  
				  
			  }
			  
			  return -1;

		}
		
		
			public double f(double x, SpaceJunk neighbor) {
				return Math.sqrt(Math.pow(this.oldcom.getX()-neighbor.oldcom.getX()+ x * (this.com.getX()- this.oldcom.getX() - neighbor.com.getX() + neighbor.oldcom.getX()), 2) + Math.pow(this.oldcom.getY()-neighbor.oldcom.getY()+ x * (this.com.getY()- this.oldcom.getY() - neighbor.com.getY() + neighbor.oldcom.getY()), 2)+Math.pow(this.oldcom.getZ()-neighbor.oldcom.getZ()+ x * (this.com.getZ()- this.oldcom.getZ() - neighbor.com.getZ() + neighbor.oldcom.getZ()), 2))-(this.radius+neighbor.radius);
		    }

		    public double fprime(double x, SpaceJunk neighbor) {
		        return (2*(this.com.getX()-this.oldcom.getX()-neighbor.com.getX()+neighbor.oldcom.getX())*this.oldcom.getX()-neighbor.oldcom.getX()+ x * (this.com.getX()- this.oldcom.getX() - neighbor.com.getX() + neighbor.oldcom.getX()) + (2*(this.com.getY()- this.oldcom.getY() - neighbor.com.getY() + neighbor.oldcom.getY())*this.oldcom.getY()-neighbor.oldcom.getY()+ x * (this.com.getY()- this.oldcom.getY() - neighbor.com.getY() + neighbor.oldcom.getY()) + (2* (this.com.getZ()- this.oldcom.getZ() - neighbor.com.getZ() + neighbor.oldcom.getZ())*(this.oldcom.getZ()-neighbor.oldcom.getZ()+ x * (this.com.getZ()- this.oldcom.getZ() - neighbor.com.getZ() + neighbor.oldcom.getZ())))/(2*(Math.sqrt(Math.pow(this.oldcom.getX()-neighbor.oldcom.getX()+ x * (this.com.getX()- this.oldcom.getX() - neighbor.com.getX() + neighbor.oldcom.getX()), 2) + Math.pow(this.oldcom.getY()-neighbor.oldcom.getY()+ x * (this.com.getY()- this.oldcom.getY() - neighbor.com.getY() + neighbor.oldcom.getY()), 2)+Math.pow(this.oldcom.getZ()-neighbor.oldcom.getZ()+ x * (this.com.getZ()- this.oldcom.getZ() - neighbor.com.getZ() + neighbor.oldcom.getZ()), 2))))));

		    }

		   

			  

		
		



		
		
		       
		
	}
