package physics;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVWriter;
 
import java.util.Collections;
import java.util.List;
import javafx.geometry.*;


public class Universe {
	
	public Universe() throws IOException {
		
		Earth earth_new = new Earth();
		
		
		for(int i=0; i<27; i++) {
			earth_new.debris_array.add(new SpaceJunk(10.0, 10.0));
		}
		
		int t=12000;
		double step = 1;
		int counter=0;
		double aclmag;
		Point3D accel;
		String csv = "c:/users/tamer/desktop/coord.csv";
	    CSVWriter writer = new CSVWriter(new FileWriter(csv));
	    
	    while(t!=0) {
	    	
	    for(SpaceJunk junk : earth_new.debris_array) {
	    	
		    //t = 12000;
		    
			
				
				aclmag = (Earth.G * Earth.M)/(Math.pow(junk.altitude,2));
				
				accel = new Point3D(-junk.com.getX()*aclmag/junk.altitude, -junk.com.getY()*aclmag/junk.altitude, -junk.com.getZ()*aclmag/junk.altitude);
				//System.out.println("accel vector: "+accel.toString());
				junk.com = new Point3D(junk.com.getX()+junk.velocity.getX()*step+0.5*accel.getX()*step*step, junk.com.getY()+junk.velocity.getY()*step+0.5*accel.getY()*step*step, junk.com.getZ()+junk.velocity.getZ()*step+0.5*accel.getZ()*step*step);
				junk.altitude = junk.com.distance(new Point3D(0,0,0));
				junk.velocity = new Point3D(junk.velocity.getX()+accel.getX()*step,junk.velocity.getY()+accel.getY()*step,junk.velocity.getZ()+accel.getZ()*step);
				//StdOut.println("Location "+ counter);
				
				if (t%60 == 0) {
					
					String entry = junk.com.getX()+" "+junk.com.getY()+" "+junk.com.getZ();
					String [] record = entry.split(" ");
					writer.writeNext(record);
			        
					counter++;
				}
				
			}
	    t--;
			
		}
		writer.close();
		
	}

	

	public static void main(String[] args) throws IOException {
		Universe myUniverse = new Universe();
		StdOut.println("Universe created.");
		
		// TODO Auto-generated method stub

	}

}
