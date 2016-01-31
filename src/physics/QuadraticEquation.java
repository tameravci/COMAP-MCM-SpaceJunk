// QuadraticEquation: calculate roots of a quadratic eq
package physics;
public class QuadraticEquation {
   private double myA;    // the 'a' in equation
   private double myB;    // the 'b' in equation
   private double myC;    // the 'c' in equation
   private double myDisc; // b*b - 4*a*c
   
   // constructor
   public QuadraticEquation(double a, double b, double c) {
      myA = a;
      myB = b;
      myC = c;
      calculateDisc();
   }
   
   // calculate first root
   public double calculateSolution1() {
      return (-myB + Math.sqrt(myDisc))/(2.0*myA);
   }
   
   // calculate second root
   public double calculateSolution2() {
      return (-myB - Math.sqrt(myDisc))/(2.0*myA);
   }
   
   // check if there are real roots
   public boolean hasSolution() {
      if (myDisc < 0) return false;
      else return true;
   }
   
   // calculate discriminant
   private void calculateDisc() {
      myDisc = myB*myB - 4.0*myA*myC;
   }
}