package physics;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.FileWriter;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVWriter;
 
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javafx.geometry.*;


public class Universe {
	
	public int t=0;
	public int w=0;
	public int p=0;
	
	public int n=20;
	public int N=n*n*n;
	SpaceJunk[][][] cube = new SpaceJunk[n][n][n];
	
	public Universe() throws IOException {
		
		Earth earth_new = new Earth();
		
		
		for(int i=0; i<N; i++) {
			earth_new.debris_array.add(new SpaceJunk(10.0, 10.0));
			
		}
		
		
		int t=12000;
		double step = 1;
		int counter=0;
		double aclmag;
		Point3D accel;
		String csv = "c:/users/tamer/desktop/coord.csv";
	    CSVWriter writer = new CSVWriter(new FileWriter(csv));
	    
	    
	    
	   
	    sortz(earth_new.debris_array);
	   // StdOut.println(cube[39][39][39].com.toString());
	    
	    
	
	    
	    
	    
	    while(t!=0) {
	    	
	    for(SpaceJunk junk : earth_new.debris_array) {
	    	
		    
			
				
				aclmag = (Earth.G * Earth.M)/(Math.pow(junk.altitude,2));
				
				accel = new Point3D(-junk.com.getX()*aclmag/junk.altitude, -junk.com.getY()*aclmag/junk.altitude, -junk.com.getZ()*aclmag/junk.altitude);
				//System.out.println("accel vector: "+accel.toString());
				junk.oldcom = junk.com;
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
	    if(cube[0][0][0].detect(cube[0][0][1])>0)
	    	cube[0][0][0].collide(cube[0][0][1]);
	    if(cube[0][0][0].detect(cube[0][1][0])>0)
	    	cube[0][0][0].collide(cube[0][1][0]);
	    if(cube[0][0][0].detect(cube[0][0][3])>0)
	    	cube[0][0][0].collide(cube[0][0][3]);
	    if(cube[0][0][0].detect(cube[0][0][4])>0)
	    	cube[0][0][0].collide(cube[0][0][4]);
	    if(cube[0][1][0].detect(cube[0][0][1])>0)
	    	cube[0][0][0].collide(cube[0][0][1]);
	    if(cube[0][1][0].detect(cube[0][0][2])>0)
	    	cube[0][0][0].collide(cube[0][0][2]);
	    if(cube[0][1][0].detect(cube[0][0][3])>0)
	    	cube[0][1][0].collide(cube[0][0][3]);
	    if(cube[0][2][1].detect(cube[0][2][0])>0)
	    	cube[0][2][1].collide(cube[0][2][0]);
	    if(cube[0][2][1].detect(cube[0][2][0])>0)
	    	cube[0][0][0].collide(cube[0][2][0]);
	    if(cube[3][0][0].detect(cube[3][0][0])>0)
	    	cube[3][0][0].collide(cube[3][0][0]);
	    if(cube[3][0][1].detect(cube[3][0][1])>0)
	    	cube[3][0][1].collide(cube[3][0][1]);
	    
	    t--;
			
		}
		writer.close();
		
	}

	

	private void sortz(List<SpaceJunk> debris_array) {
		//StdOut.println("sorting z...");
		HashMap<Double, SpaceJunk> map = new HashMap<Double, SpaceJunk>();
		double[] z = new double[N];
		int i=0;
		for(SpaceJunk junk : debris_array) {
			map.put(junk.com.getZ(), junk);
			z[i]=junk.com.getZ();
			i++;
		}
		Arrays.sort(z);
		List<SpaceJunk> zsorted = new ArrayList<SpaceJunk>();
		for(int x=0; x<z.length; x++) {
			//StdOut.println(map.get(z[x]).com.toString());
			zsorted.add(map.get(z[x]));
		}
		int call = n;
		
		int lo=0;
		int hi=(debris_array.size()/(n));
		
		for(int i1=0; i1<call; i1++) {
			
			sorty(zsorted.subList(lo,hi));
			lo+=debris_array.size()/(n);
			hi+=debris_array.size()/(n);
		}
		// TODO Auto-generated method stub
		
	}



	private void sorty(List<SpaceJunk> zsorted_half) {
		//StdOut.println("sorting y...");
		HashMap<Double, SpaceJunk> map = new HashMap<Double, SpaceJunk>();
		double[] y = new double[n*n];
		int i=0;
		for(SpaceJunk junk : zsorted_half) {
			map.put(junk.com.getY(), junk);
			y[i]=junk.com.getY();
			i++;
		}
		Arrays.sort(y);
		List<SpaceJunk> ysorted = new ArrayList<SpaceJunk>();
		for(int x=0; x<y.length; x++) {
			//StdOut.println(map.get(y[x]).com.toString());
			ysorted.add(map.get(y[x]));
		}
		
		int call = n;
		int lo=0;
		int hi=(ysorted.size()/(n));
		
		for(int i1=0; i1<call; i1++) {
			
			sortx(ysorted.subList(lo,hi));
			lo+=ysorted.size()/(n);
			hi+=ysorted.size()/(n);
		}
		
	}



	private void sortx(List<SpaceJunk> ysorted_quarter) {
		//StdOut.println("sorting x...");
		HashMap<Double, SpaceJunk> map = new HashMap<Double, SpaceJunk>();
		double[] x = new double[n];
		int i=0;
		for(SpaceJunk junk : ysorted_quarter) {
			map.put(junk.com.getX(), junk);
			//StdOut.println(junk.com.toString());
			x[i]=junk.com.getX();
			i++;
		}
		Arrays.sort(x);
		List<SpaceJunk> xsorted = new ArrayList<SpaceJunk>();
		for(int a=0; a<x.length; a++) {
			xsorted.add(map.get(x[a]));
		}
		
		
		for(SpaceJunk junk : xsorted) {
			//StdOut.println(junk.com.toString());
			cube[t][w][p] = junk;
			if(w==n-1 && p==n-1) t++;
			if(p==n-1) w++;
			if(p==n-1) p=0;
			else
				p++;
			if(w==n) w=0;
			
		}
		
	}



	public static void main(String[] args) throws IOException {
		Universe myUniverse = new Universe();
		StdOut.println("Universe created.");
		
		// TODO Auto-generated method stub

	}
	
	

}
