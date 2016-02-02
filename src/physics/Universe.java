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
		
		public int t=0; //dimensions
		public int w=0;
		public int p=0;
		
		public int n=3;
		public int N=n*n*n; //3x3x3 = 27 SpaceJunks
		SpaceJunk[][][] cube = new SpaceJunk[n][n][n]; // // discrete representation of space
		
		public Universe() throws IOException {
			
			Earth earth_new = new Earth();
			
			//fill earth's debris array with spacejunks at certain intervals with certain velocity
			for(int i=0; i<N; i++) {
				earth_new.debris_array.add(new SpaceJunk(1000, 1));
				
			}
			
			
			/*
			for(int b=0; b<100; b++) {
			SpaceJunk iceball = new SpaceJunk(100000, 10);
			earth_new.debris_array.add(iceball);
			iceball.flag = true;
			iceball.velocity = new Point3D(iceball.velocity.getX()*0.1, iceball.velocity.getY()*0.1, iceball.velocity.getZ()*0.1);
			}
			*/
			
			/* net or baloon solution test
				SpaceJunk net = new SpaceJunk(100000, 10);
				earth_new.debris_array.add(net);
				net.flag = 2;
				net.com = new Point3D(0,0,7000000);
				net.velocity = new Point3D(0,0,-500);
			*/
			
			
			//earth_new.debris_array.add(new SpaceJunk(1000000, 1000000));
			
			int counter=12000;//simulate counter seconds
			int init_counter=counter;
			double step = 1;// 1 second step size
			
			
			double aclmag; //acceleration vector magnitude
			Point3D accel; // acceleration vector
			String csv = "c:/users/tamer/desktop/coord.csv";
		    CSVWriter writer = new CSVWriter(new FileWriter(csv));
		    
		   
		   
		  //map the entire space into an NxNxN cube to make it discrete
		    sortz(earth_new.debris_array); t=0;w=0;p=0; //reset the variables for collision checking
		    

		  //Let the simulation run!
		    while(counter!=0) {
		    	
			    for(SpaceJunk junk : earth_new.debris_array) {
			    	
			    	/* For each SpaceJunk in our list, calculate its new acceleration mag,
		    		acceleration vector, velocity vector and the resulting position. Record it
		    		every 60 seconds into a csv file */
			    	
						aclmag = (Earth.G * Earth.M)/(Math.pow(junk.altitude,2));
						
						accel = new Point3D(-junk.com.getX()*aclmag/junk.altitude, -junk.com.getY()*aclmag/junk.altitude, -junk.com.getZ()*aclmag/junk.altitude);
						//System.out.println("accel vector: "+accel.toString());
						junk.oldcom = junk.com;
						junk.com = new Point3D(junk.com.getX()+junk.velocity.getX()*step+0.5*accel.getX()*step*step, junk.com.getY()+junk.velocity.getY()*step+0.5*accel.getY()*step*step, junk.com.getZ()+junk.velocity.getZ()*step+0.5*accel.getZ()*step*step);
						junk.altitude = junk.com.distance(new Point3D(0,0,0));
						junk.velocity = new Point3D(junk.velocity.getX()+accel.getX()*step,junk.velocity.getY()+accel.getY()*step,junk.velocity.getZ()+accel.getZ()*step);
						//StdOut.println("Location "+ counter);
						
						if (counter%60 == 0) {
							
							String entry = junk.com.getX()+" "+junk.com.getY()+" "+junk.com.getZ();
							String [] record = entry.split(" ");
							writer.writeNext(record);
					        
							
						}
						
						
						
						/*
						 * Laser solution testing
						 * if((init_counter-counter)%(3600/step)==0)
						  		junk.velocity = new Point3D(junk.velocity.getX()*0.1, junk.velocity.getY()*0.1, junk.velocity.getZ()*0.1);
						 */
						
				}
			    
			    
			    /* Collision detection: After updating every SpaceJunk, look for collisions. 
				To avoid checking every SpaceJunk against all the other SpaceJunks (and therefore avoiding
				exponential runtime), use the discrete space cube to only check neighbors against each
				other. Update the discrete cube after completion. Go back and run the simulation again */
			    while(t!=n && p!=n && w!=n) {
				
				
				
				if(t-1>=0) {
					if(cube[t][w][p].detect(cube[t-1][w][p])!=-1) {
						cube[t][w][p].collide(cube[t-1][w][p]);
						String entry = cube[t][w][p].com.getX()+" "+cube[t][w][p].com.getY()+" "+cube[t][w][p].com.getZ();
						String [] record = entry.split(" ");
						writer.writeNext(record);
						entry = cube[t-1][w][p].com.getX()+" "+cube[t-1][w][p].com.getY()+" "+cube[t-1][w][p].com.getZ();
						record = entry.split(" ");
						writer.writeNext(record);
					}
				}
				
				if(t+1<n) {
					if(cube[t][w][p].detect(cube[t+1][w][p])!=-1) {
						cube[t][w][p].collide(cube[t+1][w][p]);
						String entry = cube[t][w][p].com.getX()+" "+cube[t][w][p].com.getY()+" "+cube[t][w][p].com.getZ();
						String [] record = entry.split(" ");
						writer.writeNext(record);
						entry = cube[t+1][w][p].com.getX()+" "+cube[t+1][w][p].com.getY()+" "+cube[t+1][w][p].com.getZ();
						record = entry.split(" ");
						writer.writeNext(record);
					}
				}
				
				if(w-1>=0) {
					if(cube[t][w][p].detect(cube[t][w-1][p])!=-1) {
						cube[t][w][p].collide(cube[t][w-1][p]);
						String entry = cube[t][w][p].com.getX()+" "+cube[t][w][p].com.getY()+" "+cube[t][w][p].com.getZ();
						String [] record = entry.split(" ");
						writer.writeNext(record);
						entry = cube[t][w-1][p].com.getX()+" "+cube[t][w-1][p].com.getY()+" "+cube[t][w-1][p].com.getZ();
						record = entry.split(" ");
						writer.writeNext(record);
					}
				}
				
				if(w+1<n) {
					if(cube[t][w][p].detect(cube[t][w+1][p])!=-1) {
						cube[t][w][p].collide(cube[t][w+1][p]);
						String entry = cube[t][w][p].com.getX()+" "+cube[t][w][p].com.getY()+" "+cube[t][w][p].com.getZ();
						String [] record = entry.split(" ");
						writer.writeNext(record);
						entry = cube[t][w+1][p].com.getX()+" "+cube[t][w+1][p].com.getY()+" "+cube[t][w+1][p].com.getZ();
						record = entry.split(" ");
						writer.writeNext(record);
					}
				}
				
				if(p-1>=0) {
					if(cube[t][w][p].detect(cube[t][w][p-1])!=-1) {
						cube[t][w][p].collide(cube[t][w][p-1]);
						String entry = cube[t][w][p].com.getX()+" "+cube[t][w][p].com.getY()+" "+cube[t][w][p].com.getZ();
						String [] record = entry.split(" ");
						writer.writeNext(record);
						entry = cube[t][w][p-1].com.getX()+" "+cube[t][w][p-1].com.getY()+" "+cube[t][w][p-1].com.getZ();
						record = entry.split(" ");
						writer.writeNext(record);
					}
				}
				
				if(p+1<n) {
					if(cube[t][w][p].detect(cube[t][w][p+1])!=-1) {
						cube[t][w][p].collide(cube[t][w][p+1]);
						String entry = cube[t][w][p].com.getX()+" "+cube[t][w][p].com.getY()+" "+cube[t][w][p].com.getZ();
						String [] record = entry.split(" ");
						writer.writeNext(record);
						entry = cube[t][w][p+1].com.getX()+" "+cube[t][w][p+1].com.getY()+" "+cube[t][w][p+1].com.getZ();
						record = entry.split(" ");
						writer.writeNext(record);
					}
				}
				
				
				
				
				/*necessary updates to iterate through the cube */
				  if(w==n-1 && p==n-1) t++;
					if(p==n-1) w++;
					if(p==n-1) p=0;
					else
						p++;
					if(w==n) w=0;
			}
			
			  t=0;w=0;p=0;
			  
			  sortz(earth_new.debris_array); //reconstruct the cube after collisions as SpaceJunks change their location
			  t=0;w=0;p=0;
			  //if(counter%1000==0)StdOut.println(counter);
			  
			    
		    /*
		    sortz(earth_new.debris_array); t=0;w=0;p=0;
		    if(cube[0][0][0].detect(cube[0][0][1])>0)
		    	cube[0][0][0].collide(cube[0][0][1]);
		    */
		    counter--;
			//StdOut.println(net.velocity.toString());
			}
		    
		    
		    int burnin = 0;
		    int burnout = 0;
		    for(SpaceJunk junk : earth_new.debris_array) {
		    	if(junk.altitude>7571000)
		    		burnout++;
		    	if(junk.altitude <6531000)
		    		burnin++;
		    }
		    //StdOut.println(burnin);
		    //StdOut.println(burnout);
		    
		    
		    
			writer.close();
			
			
		}

		/*Following methods are required to make the space (aka universe) discrete.
		First sort all the SpaceJunks according to their z-coordinates. Split the list
		into n pieces of n*n elements. Call the sorty function n times with the appropriate pieces.
		Sorty function will sort individual n pieces of n*n elements according to their y-coordinates and
		split the individual n pieces into another n pieces of n elements and call sortx function
		n times. By this time, individual pieces only contain single SpaceJunks which sortx will sort and place
		them into the cube */

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


		//Main method where we create the universe - basically just an Earth and debris
		public static void main(String[] args) throws IOException {
			Universe myUniverse = new Universe();
			StdOut.println("Universe created.");
			
			// TODO Auto-generated method stub

		}
		
		

	}
