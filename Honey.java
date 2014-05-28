import java.util.ArrayList;
import java.util.List;
 

class PotWithHoney{
  String potName;
  public int count;
  public static final int maxCount = 100;
 
  public PotWithHoney(String potName){
      this.potName = potName;
  }
 
  synchronized public void put(){
 
      while(isFull())
          try{
              System.out.println(potName +" заполнен!");
              notify();
              wait();
          } catch (InterruptedException e) {
              System.out.println(e);
          }
 
      
      count ++;
      if(count%10 == 0)
          System.out.println("Пчёлы наносили " + count + "% мёда  в горшок " + potName + " ...");
  }
 
 
  synchronized public void eat(){
      while(!isFull())
          try{
              wait();
          } catch (InterruptedException e) {
              System.out.println(e);
          }
 
      System.out.println("Медведь проснулся и съел весь мёд в горшке " + potName +"!Ням-ням-ням!!!");
      count = 0;
      notifyAll();
  }
 
  synchronized public void waitForFull(){
      while(!isFull())
          try{
              wait();
          } catch (InterruptedException e) {
              System.out.println(e);
          }
  }
 

  public boolean isFull(){
      return count == maxCount;
  }
}
 
 

class Bee extends Thread{
  private PotWithHoney pot;
  private int timeSleep;
 
 
  public Bee(PotWithHoney p, int timeSleep){
      pot = p;
      this.timeSleep = timeSleep;
  }
 
  Bee(PotWithHoney p){
      this(p,50);
  }
 
  @Override

  public void run() {
      while(true){
          pot.put();
          try {
              Thread.sleep(timeSleep);
          } catch (InterruptedException e) { }
      }
  }
}
 

class Bear extends Thread{
  List<PotWithHoney> pots;
  public Bear(List<PotWithHoney> p){
      pots = p;
  }
 
  @Override

  public void run() {
      while (true){
          
          for(PotWithHoney pot : pots)
              pot.waitForFull();
          
          for(PotWithHoney pot : pots)
              pot.eat();
 
          
      
              System.out.println("Ох и наелся же медведь. Сразу " + pots.size() + " горшка скушал, чуть не лопнул!\n");
 
      }
 
  }
}
 
 
public class Honey {
  public static void main(String[] args){
     
      PotWithHoney pot1 = new PotWithHoney("Горшок 1");
      PotWithHoney pot2 = new PotWithHoney("Горшок 2");
      PotWithHoney pot3 = new PotWithHoney("Горшок 3");
 
      
      Bee bee1 = new Bee(pot1,135);
      Bee bee2 = new Bee(pot2,200);
      Bee bee3 = new Bee(pot3,65);
 
      ArrayList<PotWithHoney> pots = new ArrayList<PotWithHoney>();
      pots.add(pot1);
      pots.add(pot2);
      pots.add(pot3);
 
      Bear bear = new Bear(pots);
 
      bear.start();
      bee1.start();
      bee2.start();
      bee3.start();
  }        
}